package cc.c0ldcat.autorun.modules.real;

import cc.c0ldcat.autorun.BuildConfig;
import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.models.SimpleLocation;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.CommonUtils;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.AMapWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.LatLngWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.PolylineOptionsWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.PolylineWrapper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class WalkingPlan extends Module {
    private Queue<Location> wakingSteps = new LinkedList<>();
    private RequestQueue requestQueue;
    private List<OnWalkingPlanChangeListener> onWakingPlanChangeListeners = new ArrayList<>();
    private PolylineWrapper polyline;
    private ClassLoader classLoader;
    private GetRunningMap getRunningMap;
    private Location currentLocation;

    public WalkingPlan(
            ClassLoader classLoader,
            final GetMyRuningActivity getMyRuningActivity, final CheckPointPlan checkPointPlan,
            final Location currentLocation, GetRunningMap getRunningMap
    ) {
        this.classLoader = classLoader;
        this.getRunningMap = getRunningMap;
        this.currentLocation = currentLocation;

        checkPointPlan.addOnCheckPointPlanChangeListener(new CheckPointPlan.OnCheckPointPlanChangeListener() {
            @Override
            public void onCheckPointPlanChange(List<Location> locations) {
                if (requestQueue != null) {
                    LogUtils.d("cancel all old request");
                    requestQueue.stop();
                }

                requestQueue = Volley.newRequestQueue(getMyRuningActivity.getMyRuningActivity());

                wakingSteps.clear();
                searchSteps(currentLocation, locations);
            }
        });
    }

    private void drawPolyline() {
        if (polyline != null) {
            polyline.remove();
        }

        List<LatLngWrapper> points = new ArrayList<>();
        for (Location l: wakingSteps) {
            points.add(new LatLngWrapper().newInstance(classLoader, l.getLongitude(), l.getLatitude()));
        }

        polyline = getRunningMap.getRunningMap().addPolyline(new PolylineOptionsWrapper().newInstance(classLoader).addAll(points));
    }

    private void notifyNewWalkingPlan() {
        drawPolyline();

        for (OnWalkingPlanChangeListener listener: onWakingPlanChangeListeners) {
            listener.onWalkingPlanChange();
        }
    }

    private void searchSteps(Location from, final List<Location> locs) {
        if (locs.size() == 0) {
            notifyNewWalkingPlan();
            return;
        }

        final Location target = CommonUtils.findNearestLocation(from, locs);
        locs.remove(target);

        String url ="http://restapi.amap.com/v3/direction/walking?origin="
                + from.getLongitude() + "," + from.getLatitude()
                + "&destination="
                + target.getLongitude() + "," + target.getLatitude()
                + "&key=" + BuildConfig.AMAP_WEB_KEY;

        LogUtils.d("request " + url);
        requestQueue.add(
            new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONArray paths = jsonObject.getJSONObject("route").getJSONArray("paths");
                        JSONObject path = paths.getJSONObject(0);
                        JSONArray steps = path.getJSONArray("steps");
                        for (int i = 0; i < steps.length(); i++) {
                            String[] locationStrings = steps.getJSONObject(i).getString("polyline").split(";");
                            for (String locationString: locationStrings) {
                                String latLngStrings[] = locationString.split(",");
                                wakingSteps.add(new SimpleLocation(Double.parseDouble(latLngStrings[0]), Double.parseDouble(latLngStrings[1])));
                            }
                        }

                        searchSteps(target, locs);
                    } catch (JSONException e) {
                        LogUtils.e(e);
                        LogUtils.e(jsonObject.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtils.e("Request failed");
                }
            })
        );
    }

    public interface OnWalkingPlanChangeListener {
        void onWalkingPlanChange();
    }

    public void addOnWalkingPlanChangeListener(OnWalkingPlanChangeListener listener) {
        onWakingPlanChangeListeners.add(listener);
    }

    public Location next() {
        Location next = wakingSteps.element();
        if (CommonUtils.near(currentLocation, next)) {
            wakingSteps.remove();
            drawPolyline();
            return next();
        } else {
            return next;
        }
    }
}

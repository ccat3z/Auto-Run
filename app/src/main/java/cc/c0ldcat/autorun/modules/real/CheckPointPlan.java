package cc.c0ldcat.autorun.modules.real;

import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.CommonUtils;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.AMapWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.LatLngWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.MarkerOptionsWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.MarkerWrapper;

import java.util.ArrayList;
import java.util.List;

public class CheckPointPlan extends Module implements
        AMapWrapper.OnMapClickListener, AMapWrapper.OnMarkerClickListener {
    static public String CUSTOM_CHECK_POINT = "CUSTOM_CP";

    private ClassLoader classLoader;

    private AMapWrapper runningMap;
    private ArrayList<MarkerWrapper> checkPointMarkers = new ArrayList<>();

    private List<OnCheckPointPlanChangeListener> onCheckPointPlanChangeListeners = new ArrayList<>();

    public CheckPointPlan(ClassLoader classLoader, GetRunningMap runningMap, GetCheckPoint cp) {
        this.classLoader = classLoader;

        this.runningMap = runningMap.getRunningMap();
        runningMap.addOnRunningMapChangeListener(new GetRunningMap.OnRunningMapChangeListener() {
            @Override
            public void onRunningMapChange(AMapWrapper aMap) {
                LogUtils.d("new check point plan");
                checkPointMarkers.clear();
                notifyCheckPointPlanChange();

                CheckPointPlan.this.runningMap = aMap;

                // reset listener
                CheckPointPlan.this.runningMap.setOnMapClickListener(CheckPointPlan.this);
                CheckPointPlan.this.runningMap.setOnMarkerClickListener(CheckPointPlan.this);
            }
        });

        cp.addOnNewCheckPointListener(new GetCheckPoint.OnNewCheckPointListener() {
            @Override
            public void onNewCheckPoint(LatLngWrapper latLng) {
                addCheckPoint(latLng);
            }
        });
    }

    public List<Location> getCheckPoints() {
        List<Location> locs = new ArrayList<>();

        for (MarkerWrapper markers: checkPointMarkers) {
            locs.add(markers.getOptions().getPosition());
        }

        return locs;
    }

    private void addCheckPoint(LatLngWrapper point) {
        MarkerOptionsWrapper opts = new MarkerOptionsWrapper().newInstance(classLoader)
                .title(CUSTOM_CHECK_POINT)
                .position(point);
        MarkerWrapper marker = new MarkerWrapper();
        marker.setObject(runningMap.addMarker(opts));

        checkPointMarkers.add(marker);
        notifyCheckPointPlanChange();
    }

    private boolean destroyCheckPoint(MarkerWrapper markerWrapper) {
        if (checkPointMarkers.contains(markerWrapper)) {
            checkPointMarkers.remove(markerWrapper);

            markerWrapper.destroy();
            notifyCheckPointPlanChange();

            return true;
        } else {
            return false;
        }
    }

    public boolean destroyRecentCheckPoint(Location location) {
        for (MarkerWrapper marker: checkPointMarkers) {
            if (CommonUtils.near(marker.getOptions().getPosition(), location)) {
                LogUtils.d("destroy check point " + marker.getOptions().getPosition());
                return destroyCheckPoint(marker);
            }
        }
        return false;
    }

    private void notifyCheckPointPlanChange() {
        for (OnCheckPointPlanChangeListener listener: onCheckPointPlanChangeListeners) {
            listener.onCheckPointPlanChange(getCheckPoints());
        }
    }

    @Override
    public void onMapClick(LatLngWrapper point) {
        addCheckPoint(point);
    }

    @Override
    public boolean onMarkerClick(MarkerWrapper marker) {
        return destroyCheckPoint(marker);
    }

    interface OnCheckPointPlanChangeListener {
        void onCheckPointPlanChange(List<Location> locations);
    }

    void addOnCheckPointPlanChangeListener(OnCheckPointPlanChangeListener listener) {
        onCheckPointPlanChangeListeners.add(listener);
    }
}

package cc.c0ldcat.autorun.modules.real;

import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.models.SimpleLocation;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.CommonUtils;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.AMapWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.LatLngWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.MarkerOptionsWrapper;
import de.robv.android.xposed.XC_MethodHook;

import java.util.*;

public class GetCheckPoint extends Module {
    private ClassLoader classLoader;
    private AMapWrapper aMapWrapper = new AMapWrapper();
    private List<Location> latLngs = new ArrayList<>();
    private boolean haveEnd = false;

    public GetCheckPoint(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Object getAMap() {
        return aMapWrapper.getObject();
    }

    public AMapWrapper getaMapWrapper() {
        return aMapWrapper;
    }

    public Location getNextCheckPoint(Location location) throws NoSuchElementException {
        Location min = null;
        for (Location latLng : latLngs) {
            if (min == null || CommonUtils.distance(location, latLng) < CommonUtils.distance(location, min)) {
                min = latLng;
            }
        }

        if (min != null) latLngs.remove(min);

        return min;
    }

    @Override
    public void load() {
        super.load();
        new AMapWrapper().hookAddMarker(classLoader,
                new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                MarkerOptionsWrapper markerOption = new MarkerOptionsWrapper();
                markerOption.setObject(param.args[0]);

                String title = markerOption.getTitle();

                // if change map
                if(aMapWrapper.getObject() != param.thisObject) {
                    LogUtils.i("new run plan");
                    latLngs.clear();
                    aMapWrapper.setObject(param.thisObject);
                }

                if (title == null || title.equals("必经点") || title.equals("途经点")) {
                    LatLngWrapper latLng = markerOption.getPosition();

                    if (title == null && ! haveEnd) {
                        haveEnd = true;
                        title = "起点";
                        Location end = new SimpleLocation(latLng.getLongitude() + 0.01, latLng.getLatitude() + 0.01);
                        aMapWrapper.addMarker(end);
                        latLngs.add(end);

                        LogUtils.i( "END: " + end);
                    } else {
                        latLngs.add(latLng);
                    }

                    LogUtils.i(title + ": " + latLng);
                }
            }
        });
    }
}

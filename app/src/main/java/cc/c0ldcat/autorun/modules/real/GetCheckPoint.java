package cc.c0ldcat.autorun.modules.real;

import android.os.Bundle;
import android.util.Log;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.utils.RefectHelper;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class GetCheckPoint extends Module {
    private ClassLoader classLoader;

    private Class<?> aMapMarkerOptionClass;
    private Class<?> aMapLatLngClass;

    public GetCheckPoint(ClassLoader classLoader) {
        this.classLoader = classLoader;

        aMapMarkerOptionClass = XposedHelpers.findClassIfExists("com.amap.api.maps.model.MarkerOptions", classLoader);
        aMapLatLngClass = XposedHelpers.findClassIfExists("com.amap.api.maps.model.LatLng", classLoader);
    }

    @Override
    public void load() {
        super.load();
        XposedHelpers.findAndHookMethod("com.amap.api.maps.AMap", classLoader, "addMarker", aMapMarkerOptionClass, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object markerOption = param.args[0];
                String title = (String) RefectHelper.getPrivateMethod(aMapMarkerOptionClass, "getTitle").invoke(markerOption);

                if (title.equals("必经点") || title.equals("途经点")) {
                    Object latLng = RefectHelper.getPrivateMethod(aMapMarkerOptionClass, "getPosition").invoke(markerOption);

                    double latitude = (double) RefectHelper.getPrivateObject(aMapLatLngClass, "latitude", latLng);
                    double longitude = (double) RefectHelper.getPrivateObject(aMapLatLngClass, "longitude", latLng);

                    LogUtils.i(title + ":" + latitude + ":" + longitude);
                }
            }
        });
    }
}

package cc.c0ldcat.autorun.modules.real;

import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.LatLngWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.MarkerOptionsWrapper;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class GetCheckPoint extends Module {
    private ClassLoader classLoader;

    public GetCheckPoint(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void load() {
        super.load();
        XposedHelpers.findAndHookMethod("com.amap.api.maps.AMap", classLoader, "addMarker", new MarkerOptionsWrapper(null).getObjectClass(classLoader), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                MarkerOptionsWrapper markerOption = new MarkerOptionsWrapper(param.args[0]);
                String title = markerOption.getTitle();

                if (title.equals("必经点") || title.equals("途经点")) {
                    LatLngWrapper latLng = markerOption.getPosition();

                    LogUtils.i(title + ":" + latLng.getLatitude() + ":" + latLng.getLongitude());
                }
            }
        });
    }
}

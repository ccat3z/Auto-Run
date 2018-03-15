package cc.c0ldcat.autorun.modules.real;

import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.AMapWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.LatLngWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.MarkerOptionsWrapper;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.MyRuningActivityWrapper;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCheckPoint extends Module {
    private ClassLoader classLoader;
    private Map<Object, List<LatLngWrapper>> latLngs = new HashMap<>();

    public GetCheckPoint(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void load() {
        super.load();
        XposedHelpers.findAndHookMethod(AMapWrapper.CLASS, classLoader,
                AMapWrapper.METHOD_ADD_MARKER, XposedHelpers.findClassIfExists(MarkerOptionsWrapper.CLASS, classLoader), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                MarkerOptionsWrapper markerOption = new MarkerOptionsWrapper(param.args[0]);
                String title = markerOption.getTitle();

                Object amap = param.thisObject;

                if (title == null || title.equals("必经点") || title.equals("途经点")) {
                    if (! latLngs.containsKey(amap))
                        latLngs.put(amap, new ArrayList<LatLngWrapper>());
                    LatLngWrapper latLng = markerOption.getPosition();
                    latLngs.get(amap).add(latLng);

                    if (title == null) title = "起点";

                    LogUtils.i(title + ":" + latLng.getLatitude() + ":" + latLng.getLongitude());
                }
            }
        });

        XposedHelpers.findAndHookMethod(XposedHelpers.findClassIfExists(MyRuningActivityWrapper.CLASS, classLoader),
                MyRuningActivityWrapper.METHOD_UPDATE_CHECKPOINT,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        MyRuningActivityWrapper myRuningActivity = new MyRuningActivityWrapper(param.thisObject);
                        LogUtils.i(latLngs.get(myRuningActivity.getMap()).toString());
                    }
                }
        );
    }
}

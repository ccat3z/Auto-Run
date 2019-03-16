package cc.c0ldcat.autorun.modules.real;

import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.AMapWrapper;
import de.robv.android.xposed.XC_MethodHook;

import java.util.ArrayList;
import java.util.List;

public class GetRunningMap extends Module {
    private AMapWrapper runningMap;
    private ClassLoader classLoader;
    private List<OnRunningMapChangeListener> onRunningMapChangeListeners = new ArrayList<>();

    public GetRunningMap(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void load() {
        super.load();
        new AMapWrapper().hookAddMarker(classLoader, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (runningMap == null || !param.thisObject.equals(runningMap.getObject())) {
                    runningMap = new AMapWrapper().setObject(param.thisObject);

                    for (OnRunningMapChangeListener listener: onRunningMapChangeListeners) {
                        listener.onRunningMapChange(runningMap);
                    }
                }
            }
        });
    }

    public AMapWrapper getRunningMap() {
        return runningMap;
    }

    public void addOnRunningMapChangeListener(OnRunningMapChangeListener listener) {
        onRunningMapChangeListeners.add(listener);
    }

    interface OnRunningMapChangeListener {
        void onRunningMapChange(AMapWrapper aMap);
    }
}

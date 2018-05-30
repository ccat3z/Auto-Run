package cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.service.BaseService;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.location.AMapLocationWrapper;

import java.util.ArrayList;

public class AMapLocationListenerWrapper extends ReflectWrapper {
    @Override
    public String getClassName() {
        return "com.example.gita.gxty.ram.service.BaseService$2";
    }

    public void hookOnLocationChanged(final ClassLoader classLoader, Object callback) {
        hookMethod(classLoader, "onLocationChanged",
                new ArrayList<Class>() {{add(new AMapLocationWrapper().getObjectClass(classLoader));}},
                callback);
    }
}

package cc.c0ldcat.autorun.wrappers.com.amap.api.maps;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.MarkerOptionsWrapper;

import java.util.ArrayList;

public class AMapWrapper extends ReflectWrapper<AMapWrapper> {
    @Override
    public String getClassName() {
        return "com.amap.api.maps.AMap";
    }

    public void hookAddMarker(final ClassLoader classLoader, Object callback) {
        hookMethod(classLoader, "addMarker",
                new ArrayList<Class>() {{add(new MarkerOptionsWrapper().getObjectClass(classLoader));}},
                callback);
    }
}

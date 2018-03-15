package cc.c0ldcat.autorun.wrappers.com.amap.api.maps;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class AMapWrapper extends ReflectWrapper {
    public static String CLASS = "com.amap.api.maps.AMap";
    public static String METHOD_ADD_MARKER = "addMarker";

    public AMapWrapper(Object object) {
        super(object);
    }
}

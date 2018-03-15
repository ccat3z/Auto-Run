package cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class MarkerOptionsWrapper extends ReflectWrapper {
    public static String CLASS = "com.amap.api.maps.model.MarkerOptions";
    public MarkerOptionsWrapper(Object object) {
        super(object);
    }

    public String getTitle() {
        return (String) invokeMethodIfAccessable("getTitle");
    }

    public LatLngWrapper getPosition() {
        return new LatLngWrapper(invokeMethodIfAccessable("getPosition"));
    }
}

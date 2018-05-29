package cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class MarkerOptionsWrapper extends ReflectWrapper {
    @Override
    public String getClassName() {
        return  "com.amap.api.maps.model.MarkerOptions";
    }

    public String getTitle() {
        return (String) invokeMethodIfAccessable("getTitle");
    }

    public LatLngWrapper getPosition() {
        LatLngWrapper latLngWrapper = new LatLngWrapper();
        latLngWrapper.setObject(invokeMethodIfAccessable("getPosition"));
        return latLngWrapper;
    }
}

package cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class MarkerOptionsWrapper extends ReflectWrapper<MarkerOptionsWrapper> {
    @Override
    public String getClassName() {
        return  "com.amap.api.maps.model.MarkerOptions";
    }

    public String getTitle() {
        return (String) invokeMethodIfAccessable("getTitle");
    }

    public MarkerOptionsWrapper newInstance(ClassLoader classLoader, LatLngWrapper latLngWrapper) {
        setObject(super.newInstance(classLoader).invokeMethodIfAccessable("position", latLngWrapper.getObject()));
        return this;
    }

    public LatLngWrapper getPosition() {
        LatLngWrapper latLngWrapper = new LatLngWrapper();
        latLngWrapper.setObject(invokeMethodIfAccessable("getPosition"));
        return latLngWrapper;
    }
}

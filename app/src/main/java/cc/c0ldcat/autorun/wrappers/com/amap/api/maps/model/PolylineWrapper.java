package cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class PolylineWrapper extends ReflectWrapper<PolylineWrapper> {
    @Override
    public String getClassName() {
        return "com.amap.api.maps.model.Polyline";
    }

    public void remove() {
        invokeMethodIfAccessable("remove");
    }
}

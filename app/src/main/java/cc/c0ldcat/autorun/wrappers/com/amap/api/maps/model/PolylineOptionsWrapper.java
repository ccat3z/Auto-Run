package cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

import java.util.ArrayList;
import java.util.List;

public class PolylineOptionsWrapper extends ReflectWrapper<PolylineOptionsWrapper> {
    @Override
    public String getClassName() {
        return "com.amap.api.maps.model.PolylineOptions";
    }

    public PolylineOptionsWrapper addAll(Iterable<LatLngWrapper> points) {
        List<Object> pts = new ArrayList<>();
        for (LatLngWrapper p: points) {
            pts.add(p.getObject());
        }

        invokeMethodIfAccessable("addAll", pts);
        return this;
    }
}

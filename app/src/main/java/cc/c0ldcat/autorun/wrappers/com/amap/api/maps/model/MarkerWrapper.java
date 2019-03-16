package cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class MarkerWrapper extends ReflectWrapper<MarkerWrapper> {
    @Override
    public String getClassName() {
        return "com.amap.api.maps.model.Marker";
    }

    public MarkerOptionsWrapper getOptions() {
        MarkerOptionsWrapper opts = new MarkerOptionsWrapper();
        opts.setObject(invokeMethodIfAccessable("getOptions"));
        return opts;
    }

    public void destroy() {
        invokeMethodIfAccessable("destroy");
    }
}

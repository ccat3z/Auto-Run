package cc.c0ldcat.autorun.wrappers.com.amap.api.maps;

import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.ReflectWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.LatLngWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.MarkerOptionsWrapper;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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

    public Object addMarker(MarkerOptionsWrapper markerOptionsWrapper) {
        return invokeMethodIfAccessable("addMarker", markerOptionsWrapper.getObject());
    }

    public Object addMarker(double longitude, double latitude) {
       ClassLoader classLoader = getObjectClass().getClassLoader();
       return addMarker(new MarkerOptionsWrapper().newInstance(classLoader, new LatLngWrapper().newInstance(classLoader, longitude, latitude)));
    }

    public Object addMarker(Location location) {
        return addMarker(location.getLongitude(), location.getLatitude());
    }

    public void setOnMapClickListener(final OnMapClickListener onMapClickListener) {
        try {
            Class<?> realOnMapCLickListenerInterface = XposedHelpers.findClass(getObjectClass().getName() + "$OnMapClickListener", getObjectClass().getClassLoader());
            Class<?>[] interfaces = {realOnMapCLickListenerInterface};
            invokeMethodIfAccessable("setOnMapClickListener", interfaces, Proxy.newProxyInstance(getObjectClass().getClassLoader(), interfaces, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    if (method.getName().equals("onMapClick")) {
                        LatLngWrapper latLng = new LatLngWrapper();
                        latLng.setObject(objects[0]);

                        onMapClickListener.onMapClick(latLng);

                        return null;
                    }

                    return null;
                }
            }));
        } catch (XposedHelpers.ClassNotFoundError e) {
            LogUtils.e(e);
        }
    }

    public interface OnMapClickListener {
        void onMapClick(LatLngWrapper point);
    }
}

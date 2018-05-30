package cc.c0ldcat.autorun.wrappers.com.amap.api.location;

import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class AMapLocationWrapper extends ReflectWrapper implements Location {
    @Override
    public String getClassName() {
        return "com.amap.api.location.AMapLocation";
    }

    public double getLatitude() {
        return (double) invokeMethodIfAccessable("getLatitude");
    }

    public double getLongitude() {
        return (double) invokeMethodIfAccessable("getLongitude");
    }

    public void setLatitude(double latitude) {
        invokeMethodIfAccessable("setLatitude", new Class[] {double.class}, new Object[] {latitude});
    }

    public void setLongitude(double longitude) {
        invokeMethodIfAccessable("setLongitude", new Class[] {double.class}, new Object[] {longitude});
    }

    @Override
    public String toString() {
        return getLongitude() + "," + getLatitude();
    }
}

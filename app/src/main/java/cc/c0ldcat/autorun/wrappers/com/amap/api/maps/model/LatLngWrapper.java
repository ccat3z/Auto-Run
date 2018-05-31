package cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model;

import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class LatLngWrapper extends ReflectWrapper<LatLngWrapper> implements Location {
    @Override
    public String getClassName() {
        return "com.amap.api.maps.model.LatLng";
    }

    public double getLatitude() {
        return (double) getAttributeIfExist("latitude");
    }

    public double getLongitude() {
        return (double) getAttributeIfExist("longitude");
    }

    @Override
    public String toString() {
        return getLongitude() + "," + getLatitude();
    }
}

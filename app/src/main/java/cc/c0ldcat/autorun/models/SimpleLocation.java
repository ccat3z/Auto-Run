package cc.c0ldcat.autorun.models;

public class SimpleLocation implements Location {
    private double longitude;
    private double latitude;

    public SimpleLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return getLongitude() + "," + getLatitude();
    }
}

package cc.c0ldcat.autorun.utils;

import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.models.SimpleVector;
import cc.c0ldcat.autorun.wrappers.com.amap.api.location.AMapLocationWrapper;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.model.LatLngWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class CommonUtils {
    public static String concatPath(String... paths) {
        return concatPath(Arrays.asList(paths));
    }

    public static String concatPath(List<String> paths) {
        StringWriter sw = new StringWriter();
        for (String path : paths) {
            sw.append(path.replaceAll("/$",""));
            sw.append('/');
        }
        return sw.toString().replaceAll("/$","");
    }

    public static String exceptionStacktraceToString(Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }

    public static double distance(double fromLongitude, double fromLatitude, double toLongitude, double toLatitude) {
        return Math.sqrt(Math.pow(fromLongitude - toLongitude, 2) + Math.pow(fromLatitude - toLatitude, 2));
    }

    public static double distance(Location from, Location to) {
        return distance(from.getLongitude(), from.getLatitude(), to.getLongitude(), to.getLatitude());
    }

    public static SimpleVector vector(Location from, Location to, double length) {
        double time = length / distance(from, to);
        return new SimpleVector(time * (to.getLongitude() - from.getLongitude()), time * (to.getLatitude() - from.getLatitude()));
    }
}

package cc.c0ldcat.autorun.utils;

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
}

package cc.c0ldcat.autorun.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CommonUtils {

    public static String exceptionStacktraceToString(Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }
}

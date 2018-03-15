package cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class MyRuningActivityWrapper extends ReflectWrapper {
    public static String CLASS = "com.example.gita.gxty.ram.MyRuningActivity";
    public static String VAR_MAP = "s"; // 132 f6378s
    public static String METHOD_UPDATE_CHECKPOINT = "t"; // 640 m7447t

    public MyRuningActivityWrapper(Object object) {
        super(object);
    }

    public Object getMap() {
        return getAttributeIfExist(VAR_MAP);
    }
}

package cc.c0ldcat.autorun.modules.real;

import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.models.SimpleVector;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.amap.api.location.AMapLocationWrapper;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.service.BaseService.AMapLocationListenerWrapper;
import de.robv.android.xposed.XC_MethodHook;

public class FakeLocation extends Module implements Location {
    private ClassLoader classLoader;
    private double latitude = 0;
    private double longitude = 0;

    public FakeLocation(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void load() {
        super.load();

        new AMapLocationListenerWrapper().hookOnLocationChanged(classLoader, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                AMapLocationWrapper aMapLocation = new AMapLocationWrapper();
                aMapLocation.setObject(param.args[0]);

                // init location if no fake location
                if (latitude == 0 || longitude == 0) {
                    LogUtils.i("init fake location");
                    latitude = aMapLocation.getLatitude();
                    longitude = aMapLocation.getLongitude();
                }

                // replace fake location
                aMapLocation.setLatitude(latitude);
                aMapLocation.setLongitude(longitude);
            }
        });
    }

    public void move(SimpleVector vector) {
        latitude += vector.getLatitude();
        longitude += vector.getLongitude();
    }
}

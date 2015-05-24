package hackathon.dclab.com.minidianping;

import android.app.Application;
import android.location.Location;

/**
 * Created by Yongfeng on 15/5/23.
 */
public class DPApplication extends Application {
    private static Location location;

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        DPApplication.location = location;
    }
}

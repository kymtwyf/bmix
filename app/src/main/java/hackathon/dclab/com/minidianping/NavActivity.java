package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

/**
 * Created by weicheng on 1/6/15.
 */
public class NavActivity extends Activity{
    private MapView map = null;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_nav);
        map = (MapView)findViewById(R.id.nav_map);
    }
}

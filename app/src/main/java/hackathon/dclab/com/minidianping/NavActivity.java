package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import hackathon.dclab.com.minidianping.domain.Business;

/**
 * Created by weicheng on 1/6/15.
 */
public class NavActivity extends Activity{
    private MapView mapView = null;
    private BaiduMap map = null;
    private MapStatus mapStatus = null;
    private Business business = null;
    private TextView business_title = null;
    private TextView address = null;
    private Button address_nav = null;
    private ImageView yes = null;
    private ImageView collect = null;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_nav);
        mapView = (MapView)findViewById(R.id.nav_map);
        business = (Business)getIntent().getSerializableExtra("business");
        business_title = (TextView)findViewById(R.id.nav_header);
        address = (TextView)findViewById(R.id.nav_address_context);
        address_nav = (Button)findViewById(R.id.nav_address_button);
        yes = (ImageView)findViewById(R.id.nav_yes);
        collect = (ImageView)findViewById(R.id.nav_collect);

        //更新餐厅名字
        business_title.setText(business.name);
        //更新地址信息
        address.setText(business.address);

        //更新地图
        LatLng point = new LatLng(business.latitude, business.longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        map = mapView.getMap();
        map.addOverlay(option);
        mapStatus = new MapStatus.Builder().target(point).zoom(16).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        map.setMapStatus(mapStatusUpdate);
    }
}

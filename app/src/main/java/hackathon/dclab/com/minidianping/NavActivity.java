package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
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

import java.net.URISyntaxException;

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
    private ImageView yes = null;
    private ImageView collect = null;
    private ImageView tele_btn = null;
    private TextView tele_text = null;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_nav);
        mapView = (MapView)findViewById(R.id.nav_map);
        business = (Business)getIntent().getSerializableExtra("business");
        business_title = (TextView)findViewById(R.id.nav_header);
        address = (TextView)findViewById(R.id.nav_address_context);
        yes = (ImageView)findViewById(R.id.nav_yes);
        collect = (ImageView)findViewById(R.id.nav_collect);
        tele_btn = (ImageView)findViewById(R.id.nav_tel_btn);
        tele_text = (TextView)findViewById(R.id.nav_tel_context);

        //更新餐厅名字
        business_title.setText(business.name);
        //更新地址信息
        address.setText(business.address);
        //更新电话信息
        tele_text.setText(business.telephones.get(0));

        //更新地图
        LatLng point = new LatLng(business.latitude, business.longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        map = mapView.getMap();
        map.addOverlay(option);
        mapStatus = new MapStatus.Builder().target(point).zoom(16).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        map.setMapStatus(mapStatusUpdate);

        final BDLocation cirloc = DPApplication.getLocation();

        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = null;
                double srclat = cirloc.getLatitude(), srclong = cirloc.getLongitude();
                double deslat = business.latitude, deslong = business.longitude;
                String city = cirloc.getCity();
                try {
                    intent = Intent.getIntent("intent://map/direction?" +
                            "origin=latlng:" + srclat +","+ srclong + "|name:我的位置"+
                            "&destination=latlng:" + deslat +","+ deslong +"|name:要去的地方"+
                            "&mode=driving"+
                            "&region="+city+
                            "&referer=SJTU|MiniDianping#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                    startActivity(intent); //启动调用
                } catch (URISyntaxException e) {
                    Log.e("intent", e.getMessage());
                }
            }
        });

        tele_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tele_text.getText()));
                NavActivity.this.startActivity(intent);
            }
        });
    }
}

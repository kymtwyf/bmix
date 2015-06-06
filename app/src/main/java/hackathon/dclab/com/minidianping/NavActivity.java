package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
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
    private MyOnClickListener listener = null;

    private SensorManager sensorManager; //用来检测摇一摇
    private Vibrator vibrator; //用来检测摇一摇
    private static final int SENSOR_SHAKE = 10;
    private Handler vihandler;//摇一摇的handler
    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;
    private long leaveCheckTime = 0;
    private long returnCheckTime = 0;

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
        listener = new MyOnClickListener();

        //更新餐厅名字
        business_title.setText(business.name);
        //更新地址信息
        address.setText(business.address);
        //更新电话信息
        tele_text.setText(business.telephones.get(0));

        //更新地图
        LatLng point = new LatLng(business.latitude, business.longitude);

        //摇一摇相关
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vihandler = new MyShakeHandler();

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        map = mapView.getMap();
        map.addOverlay(option);
        mapStatus = new MapStatus.Builder().target(point).zoom(17).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        map.setMapStatus(mapStatusUpdate);
        yes.setOnClickListener(listener);
        tele_btn.setOnClickListener(listener);
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.nav_tel_btn: {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tele_text.getText()));
                    NavActivity.this.startActivity(intent);
                    break;
                }
                case R.id.nav_yes:{
                    BDLocation cirloc = DPApplication.getLocation();
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
                        leaveCheckTime = System.currentTimeMillis();
                        startActivity(intent); //启动调用
                    } catch (URISyntaxException e) {
                        Log.e("intent", e.getMessage());
                    }
                    break;
                }

            }
        }
    }

    /**
     * 动作执行
     */
    private class MyShakeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    Log.i("NAV", "检测到摇晃，执行操作！");
                    sensorManager.unregisterListener(sensorEventListener);
                    yes.performClick();
                    break;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(leaveCheckTime != 0)
            returnCheckTime = System.currentTimeMillis();
        if(returnCheckTime - leaveCheckTime > 1000){
            leaveCheckTime = returnCheckTime;
            new AlertDialog.Builder(NavActivity.this)
                    .setTitle("请求评价")
                    .setMessage("我们猜测你可能吃了我们推荐的美食，请问要评价么？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(NavActivity.this,EvaluateActivity.class);
                            Bundle mbundle = new Bundle();
                            mbundle.putSerializable("business", business);
                            intent.putExtras(mbundle);
                            startActivity(intent);
                            NavActivity.this.finish();
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
    protected void onDestroy(){
        if (sensorManager != null) {// 注册监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
        super.onDestroy();
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            int medumValue = 15;// 多次调试，设置到15
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                vihandler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}

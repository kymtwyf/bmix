package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hackathon.dclab.com.minidianping.domain.Business;
import hackathon.dclab.com.minidianping.entities.GeoInfo;
import hackathon.dclab.com.minidianping.entities.Mode;
import hackathon.dclab.com.minidianping.entities.MyLog;


public class MainActivity extends Activity {
    private static String TAG = "MainActivity";
    public static final int INTENT_TO_LIST = 999;
    private TextView tvHeader;
    private TextView tvDistance;
    private ImageView ivPreview;
    private LinearLayout llRecommendReasons;
    private ListView lvRecommendDishes;
    private SimpleAdapter dishAdapter;
    private ArrayList<HashMap<String,String>> dishContent;
    private TextView tvAverage; // 人均消费：
    private TextView tvRatingNumber; //评分
    private RatingBar ratingBar; //星星
    private ImageView ivYes;
    private ImageView ivNo;
    private Button button_mod;
    private Button button_list;
    private MyOnClickListener listener;

    private Handler handler;
    private Handler vihandler;//摇一摇的handler
    private long lastCheckTime = 0;
    private final long INTERVAL = 100;

    public static final int MSG_UPDATE_PREVIEW = 0;
    public static final int MSG_RENDER_VIEW = 1;
    public static final int MSG_GET_ALL_MESSAGE = 2;
    private LocationClient mLocationClient = null;

    private List<Business> businesses = new ArrayList<Business>();
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private int currentIndex = 0;

    public static Mode mode = null;

    private SensorManager sensorManager; //用来检测摇一摇
    private Vibrator vibrator; //用来检测摇一摇
    private static final int SENSOR_SHAKE = 10;
    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;

    private ProgressBar circleProgress;
    private int iCount = 0;
    private boolean flag = false;

    private LogUtil logUtil;
    TelephonyManager tm = null;
    String android_id = "";
    GeoInfo geo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        android_id = tm.getDeviceId();
        System.out.println(android_id);

        circleProgress = (ProgressBar)findViewById(R.id.iv_progress);
        circleProgress.setIndeterminate(false);
        circleProgress.setVisibility(View.VISIBLE);
        circleProgress.setProgress(0);
        mThread.start();
        tvHeader = (TextView)findViewById(R.id.tvHeader);
        tvDistance = (TextView)findViewById(R.id.tvDistance);
        ivPreview = (ImageView)findViewById(R.id.ivPreview);
        llRecommendReasons = (LinearLayout)findViewById(R.id.ll_recommend_reasons);
        lvRecommendDishes = (ListView)findViewById(R.id.lv_recommend_dishes);
        tvAverage = (TextView)findViewById(R.id.tvAverage);
        tvRatingNumber = (TextView)findViewById(R.id.tvRatingNumber);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ivYes = (ImageView)findViewById(R.id.ivYes);
        ivNo = (ImageView)findViewById(R.id.ivNo);
        listener = new MyOnClickListener();
        ivYes.setOnClickListener(listener);
        ivNo.setOnClickListener(listener);
        button_mod = (Button)findViewById(R.id.button_mod);
        button_list = (Button)findViewById(R.id.button_list);
        button_mod.setOnClickListener(listener);
        button_list.setOnClickListener(listener);
        dishContent = new ArrayList<HashMap<String,String>>();
        dishAdapter = new SimpleAdapter(this,dishContent,R.layout.recommend_dish_item
                ,new String[]{"tv_dish_name","tv_dish_price"}
                ,new int[]{R.id.tv_dish_name,R.id.tv_dish_price});

        lvRecommendDishes.setAdapter(dishAdapter);
        ivPreview.setScaleType(ImageView.ScaleType.FIT_XY);
        handler = new ViewUpdateHandler();
        logUtil = new LogUtil(getApplicationContext());
        //定位相关
        SDKInitializer.initialize(getApplicationContext());
        mLocationClient = ((DPApplication)getApplication()).mLocationClient;
        LocationClientOption option = new LocationClientOption();
        initLocationOptions(option);//设置定位参数
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();

        //摇一摇相关
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vihandler = new MyShakeHandler();

        //取得第一次的请求
        BDLocation location = DPApplication.getLocation();
        //GeoInfo geo = new GeoInfo("中国",location.getCity(),location.getDistrict(),location.getLatitude(),location.getLongitude());
        geo = new GeoInfo("China","北京市","海淀区",116.313096,39.990047);
        File file = new File("/data/data/hackathon.dclab.com.minidianping/files/mode.xml");
        Mode mode = null;
        if(!file.exists()) {
             mode= new Mode(2, 1, 0x00000000);
             writeFile("mode.xml",mode);
        }
        mode = readFromXML("mode.xml");
        getRecommend = new HttpGetRecommend(android_id,geo,mode,page);
        String json = "";
        getRecommend.start();
        try {
            getRecommend.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        json = getRecommend.getResult();
        if (json == ""){
            getRecommend = new HttpGetRecommend(android_id,geo,mode,page);
            getRecommend.start();
            try {
                getRecommend.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            json = getRecommend.getResult();
        }
        businesses = Business.getBusinessFromJson(json);
        for(int i=0; i<businesses.size();++i){
            //System.out.println(businesses.get(i).ToString());
            bitmaps.add(null);//placeholder
        }
        //开启线程取图片
        (new Thread(testGetImg)).start();
        renderBusiness();
    }


    private void renderBusiness(){
        Log.e("rendering ","view");
        if(businesses == null || currentIndex>=businesses.size()){
            currentIndex--;
            Log.e("出错了","到最后一个了，或者 business为null");
            return;
        }
        if(currentIndex > businesses.size() - 5){
            new AsyncGetBusiness().execute();
        }
        Business curBus = businesses.get(currentIndex);
        tvHeader.setText(curBus.name);
        tvAverage.setText("人均消费：" + curBus.average_cost + " 元");
        if(bitmaps.get(currentIndex) != null){
            Log.e("图片","下载完了，"+currentIndex);

            ivPreview.setImageBitmap(bitmaps.get(currentIndex));
        }else{
            Log.e("图片","还没下载完");
        }
        tvDistance.setText("离你的距离：" + curBus.distance + "米");
        tvRatingNumber.setText(new java.text.DecimalFormat("#.0").format(curBus.recommend_mark));

        llRecommendReasons.removeAllViews();
        for(String reason : curBus.recommend_reason){
            TextView textView =  (TextView) getLayoutInflater().inflate(R.layout.recommend_list_item, null);
            textView.setText(reason);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
            llRecommendReasons.addView(textView);
        }
        dishContent.clear();
        for(Business.Dish dish:curBus.favourite_dishes){
            HashMap<String,String> map = new HashMap<>();
            map.put("tv_dish_name",dish.name);
            map.put("tv_dish_price","¥ "+dish.price+".00");
            dishContent.add(map);
        }
        dishAdapter.notifyDataSetChanged();

    }
    Runnable testGetImg = new Runnable() {
        @Override
        public void run() {
            for(int i = 0 ; i < businesses.size() ; i++){
                Bitmap bm = NetUtils.getBitmap(businesses.get(i).pic_url);
                if(bm != null) {
                    Log.e(TAG,"get image "+ i);
                    bitmaps.set(i,bm);
                    handler.obtainMessage(MSG_UPDATE_PREVIEW,i).sendToTarget();
                }
                else {
                    Log.e(TAG, "cant get image");
                    bitmaps.set(i,null);
                }
            }
            handler.sendEmptyMessage(MSG_GET_ALL_MESSAGE);

        }
    };
    /**设置定位参数
     * @param option
     */
    private void initLocationOptions(LocationClientOption option) {
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");//设置坐标类型Baidu encoded latitude & longtitude
        option.setScanSpan(1000*10);//扫描间隔10s
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//GPS + Network locating
        option.setAddrType("all");//locating results include all address infos
        option.setIsNeedAddress(true);//include address infos
    }

    @Override
    protected void onPause() {
        logUtil.storeLogs();
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 退出时停止定位sdk
        ((DPApplication)getApplication()).mLocationClient.stop();

        super.onDestroy();
    }
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(!flag)
                return;
            switch (view.getId()) {
                case R.id.ivYes: {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, NavActivity.class);
                    Bundle mbundle = new Bundle();
                    mbundle.putSerializable("business", businesses.get(currentIndex));
                    intent.putExtras(mbundle);
                    MainActivity.this.startActivity(intent);

                    MyLog myLog = new MyLog();
                    myLog.setBusiness(businesses.get(currentIndex));
                    myLog.setState(1);
                    logUtil.insertLog(myLog);
                    break;
                }
                case R.id.ivNo:{
                    handler.sendEmptyMessage(MSG_RENDER_VIEW);
                    MyLog myLog = new MyLog();
                    myLog.setBusiness(businesses.get(currentIndex));
                    myLog.setState(2);
                    logUtil.insertLog(myLog);
                    Log.e("DEBUG", businesses.size() + " " + currentIndex);
                    if(businesses.size() - currentIndex< 4){
                        Log.e("DEBUG","Start to download another");
                        String json = "";
                        if(try_downloading){
                            if(getRecommend.getResult()!=""){
                                //System.out.println("finish download\n");
                                json = getRecommend.getResult();
                                try_downloading = false;
                                List<Business> businesses_temp = Business.getBusinessFromJson(json);
                                for(int i=0; i<businesses_temp.size();++i){
                                    System.out.println(businesses_temp.get(i).ToString());
                                    bitmaps.add(null);//placeholder
                                }
                                businesses.addAll(businesses_temp);
                                for(int i=0; i<currentIndex;++i){
                                    businesses.remove(i);
                                }
                                for(int i=0; i<currentIndex; ++i){
                                    bitmaps.remove(i);
                                }
                                currentIndex -= currentIndex;
                                //开启线程取图片
                                (new Thread(testGetImg)).start();
                                renderBusiness();
                            }
                        }
                        else{
                            BDLocation location = DPApplication.getLocation();
                            GeoInfo geo = new GeoInfo("中国",location.getCity(),location.getDistrict(),location.getLongitude(),location.getLatitude());
                            Mode mode = readFromXML("mode.xml");
                            page++;
                            getRecommend = new HttpGetRecommend(android_id,geo,mode,page);
                            getRecommend.start();
                            try_downloading = true;
                        }
                    }
                    currentIndex++;
                    break;
                }
                case R.id.button_mod:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ModeActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                    break;

                }
                case R.id.button_list:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, NewHistoryActivity.class);
                    MainActivity.this.startActivityForResult(intent, INTENT_TO_LIST);
                }

            }
        }
    }
    private class ViewUpdateHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG,"HANDLING IMG");

            switch (msg.what){
                case MSG_GET_ALL_MESSAGE:
                    Log.e(TAG,"get all images!!!");
//                        ivPreview.setImageBitmap((Bitmap)msg.obj);
                    break;
                case MSG_UPDATE_PREVIEW:
                    if(currentIndex == (Integer)msg.obj) {
                        Log.e(TAG,"UPDATE PREVIEW "+ currentIndex + " "+ (Integer)msg.obj);
                        ivPreview.setImageBitmap(bitmaps.get(currentIndex));
                    }
                    break;
                case MSG_RENDER_VIEW:
                    Log.e(TAG,"RENDERING VIEW ");
                    renderBusiness();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(!flag)
                return;
            long currentCheckTime = System.currentTimeMillis();
            long interval = currentCheckTime - lastCheckTime;
            if(interval < INTERVAL)
                return;
            lastCheckTime = currentCheckTime;
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            int medumValue = 19;// 多次调试，设置到13
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

    /**
     * 动作执行
     */
    private class MyShakeHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    Log.i(TAG, "检测到摇晃，执行操作！");
                    ivNo.performClick();
                    //sensorManager.unregisterListener(sensorEventListener);
                    break;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case STOP:
                    circleProgress.setVisibility(View.GONE);
                    Thread.currentThread().interrupt();
                    break;
                case NEXT:
                    if(!Thread.currentThread().isInterrupted()){
                        circleProgress.setProgress(iCount);
                    }
                    break;
            }
        }
    };

    private Thread mThread = new Thread(new Runnable() {

        public void run() {
            iCount = 0;
            flag = false;
            for(int i=0 ; i < 20; i++){
                try{
                    iCount = (i + 1) * 5;
                    Thread.sleep(200);
                    if(i == 19){
                        Message msg = new Message();
                        msg.what = STOP;
                        mHandler.sendMessage(msg);
                        flag = true;
                        break;
                    }else{
                        Message msg = new Message();
                        msg.what = NEXT;
                        mHandler.sendMessage(msg);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    });

    private Mode readFromXML(String file){
        System.out.println("Read from String");
        String res="";
        try{
            FileInputStream fin = openFileInput(file);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        String[] list = res.split(",");
        int number = Integer.parseInt(list[0]);
        int type = Integer.parseInt(list[1]);
        int stype = Integer.parseInt(list[2]);
        return new Mode(number,type,stype);
    }


    public void writeFile(String fileName,Mode mode){
        System.out.println("Write To File");
        try{
            FileOutputStream fout =openFileOutput(fileName, MODE_PRIVATE);

            int number = mode.number;
            int type = mode.type;
            int stype = mode.stype;

            String str = number+","+type+","+stype;

            fout.write(str.getBytes());

            fout.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }

    }

    private class AsyncGetBusiness extends AsyncTask<Void,Void,Boolean> {
//        public AsyncGetBusiness(){
//
//        }
        List<Business> asyncResults = new ArrayList<>();
        List<Bitmap> asyncBitmaps = new ArrayList<>();
        List<NameValuePair> params = new ArrayList<>();
        String resultStr = "";

//        private Map<String,String> params = new HashMap<>();
        @Override
        protected void onPreExecute() {
//            Toast.makeText(getApplicationContext(),"Loading in background", Toast.LENGTH_SHORT).show();
            setParams("user_id", android_id);
            setParams("latitude", geo.latitude + "");
            setParams("longitude", geo.longitude + "");
            setParams("mode", new Gson().toJson(mode));

            super.onPreExecute();
        }
        public void setParams(String key, String value) {
            params.add(new BasicNameValuePair(key,value));
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                resultStr = NetUtils.postRequest(HttpGetRecommend.action,params);
                asyncResults = Business.getBusinessFromJson(resultStr);
                if(asyncResults == null || asyncResults.size() == 0) {
                    throw new Exception();
                }else{
                    for(int i = 0 ; i < asyncResults.size() ; i ++){
                        asyncBitmaps.add(NetUtils.getBitmap(asyncResults.get(i).pic_url));
                    }
                    return true;
                }
            }catch(Exception e){
                return false;

            }
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            if(bool){
//                Toast.makeText(getApplicationContext(),"缓冲完成",Toast.LENGTH_SHORT).show();
                businesses.addAll(asyncResults);
                bitmaps.addAll(asyncBitmaps);
            }else{
                Toast.makeText(getApplicationContext(),"缓冲失败",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(bool);
        }
    }
}

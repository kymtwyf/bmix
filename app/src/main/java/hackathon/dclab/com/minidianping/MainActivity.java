package hackathon.dclab.com.minidianping;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hackathon.dclab.com.minidianping.domain.Business;
import hackathon.dclab.com.minidianping.entities.GeoInfo;
import hackathon.dclab.com.minidianping.entities.Mode;


public class MainActivity extends Activity {
    private static String TAG = "MainActivity";
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
    private MyOnClickListener listener;

    private Handler handler;

    public static final int MSG_UPDATE_PREVIEW = 0;
    public static final int MSG_RENDER_VIEW = 1;
    public static final int MSG_GET_ALL_MESSAGE = 2;
    private LocationClient mLocationClient = null;

    private List<Business> businesses = new ArrayList<Business>();;
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private int currentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        button_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ModeActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        dishContent = new ArrayList<HashMap<String,String>>();
        dishAdapter = new SimpleAdapter(this,dishContent,R.layout.recommend_dish_item
                ,new String[]{"tv_dish_name","tv_dish_price"}
                ,new int[]{R.id.tv_dish_name,R.id.tv_dish_price});

        lvRecommendDishes.setAdapter(dishAdapter);
        ivPreview.setScaleType(ImageView.ScaleType.FIT_XY);
        handler = new ViewUpdateHandler();

        //定位相关
        mLocationClient = ((DPApplication)getApplication()).mLocationClient;
        LocationClientOption option = new LocationClientOption();
        initLocationOptions(option);//设置定位参数
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        //定位相关


        //HttpGetRecommend http = new HttpGetRecommend();
        GeoInfo geo = new GeoInfo("China","Shanghai","Minhang",0,0);
        Mode mode = new Mode(2,1,0x000000);
        Gson gs = new Gson();
        HttpGetRecommend getRecommend = new HttpGetRecommend("123456",geo,mode);
        ExecutorService exs= Executors.newCachedThreadPool();
        String json = null;
        try {
            json = exs.submit(getRecommend).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        businesses = Business.getBusinessFromJson(json);
        for(int i=0; i<businesses.size();++i){
            System.out.println(businesses.get(i).ToString());
            bitmaps.add(null);//placeholder

        }
        //开启线程取图片
        (new Thread(testGetImg)).start();
        renderBusiness();
    }


    private void renderBusiness(){
        Log.e("rendering ","view");
        if(businesses == null || currentIndex>=businesses.size()){
            Log.e("出错了","到最后一个了，或者 business为null");
            return;
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
        option.setScanSpan(1000 * 60 * 5);//扫描间隔5min
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//GPS + Network locating
        option.setAddrType("all");//locating results include all address infos
        option.setIsNeedAddress(true);//include address infos
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
            switch (view.getId()) {
                case R.id.ivYes:{
                    //log 一些信息
                    break;
                }
                case R.id.ivNo:{
                    break;
                }
            }
            currentIndex++;
            handler.sendEmptyMessage(MSG_RENDER_VIEW);
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

}

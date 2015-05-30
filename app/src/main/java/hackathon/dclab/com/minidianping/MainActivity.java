package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


public class MainActivity extends Activity {
    private static String TAG = "MainActivity";
    private TextView tvHeader;
    private ImageView ivPreview;
    private LinearLayout llRecommendReasons;
    private ListView lvRecommendDishes;
    private TextView tvAverage; // 人均消费：
    private TextView tvRatingNumber; //评分
    private RatingBar ratingBar; //星星
    private ImageView ivYes;
    private ImageView ivNo;

    private Handler handler;

    public static final int MSG_UPDATE_PREVIEW = 0;
    private LocationClient mLocationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHeader = (TextView)findViewById(R.id.tvHeader);
        ivPreview = (ImageView)findViewById(R.id.ivPreview);
        llRecommendReasons = (LinearLayout)findViewById(R.id.lv_recommend_reasons);
        lvRecommendDishes = (ListView)findViewById(R.id.lv_recommend_dishes);
        tvAverage = (TextView)findViewById(R.id.tvAverage);
        tvRatingNumber = (TextView)findViewById(R.id.tvRatingNumber);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ivYes = (ImageView)findViewById(R.id.ivYes);
        ivNo = (ImageView)findViewById(R.id.ivNo);



        ivPreview.setScaleType(ImageView.ScaleType.FIT_XY);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG,"HANDLING IMG");

                switch (msg.what){
                    case MSG_UPDATE_PREVIEW:
                        Log.e(TAG,"SETTING IMG");
                        ivPreview.setImageBitmap((Bitmap)msg.obj);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        (new Thread(testGetImg)).start();

        //定位相关
        mLocationClient = ((DPApplication)getApplication()).mLocationClient;
        LocationClientOption option = new LocationClientOption();
        initLocationOptions(option);//设置定位参数
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        //定位相关

    }



    Runnable testGetImg = new Runnable() {
        @Override
        public void run() {
            Bitmap bm = NetUtils.getBitmap("http://kazge.com/wp-content/themes/deskchaos/img/outer-back.jpg");
            if(bm != null) Log.e(TAG,"get image");
            else Log.e(TAG, "cant get image");
            handler.obtainMessage(MSG_UPDATE_PREVIEW,bm).sendToTarget();

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


}

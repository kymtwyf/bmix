package hackathon.dclab.com.minidianping;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.w3c.dom.Text;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static String TAG = "MainActivity";
    private ImageView imgView;
    private TextView tvLongtitude;
    private TextView tvLatitude;

    private Handler handler;

    public static final int MSG_UPDATE_TEST = 0;
    private LocationClient mLocationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView)findViewById(R.id.getImage);
        tvLatitude = (TextView)findViewById(R.id.latitude);
        tvLongtitude = (TextView)findViewById(R.id.longitude);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG,"HANDLING IMG");

                switch (msg.what){
                    case MSG_UPDATE_TEST:
                        Log.e(TAG,"SETTING IMG");
                        imgView.setImageBitmap((Bitmap)msg.obj);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        (new Thread(testGetImg)).start();

        mLocationClient = ((DPApplication)getApplication()).mLocationClient;
        LocationClientOption option = new LocationClientOption();
        initLocationOptions(option);//设置定位参数
        mLocationClient.setLocOption(option);

        mLocationClient.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Runnable testGetImg = new Runnable() {
        @Override
        public void run() {
            Bitmap bm = NetUtils.getBitmap("http://kazge.com/wp-content/themes/deskchaos/img/outer-back.jpg");
            if(bm != null) Log.e(TAG,"get image");
            else Log.e(TAG, "cant get image");
            handler.obtainMessage(MSG_UPDATE_TEST,bm).sendToTarget();
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

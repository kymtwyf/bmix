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

import org.w3c.dom.Text;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static String TAG = "MainActivity";
    private ImageView imgView;
    private TextView tvLongtitude;
    private TextView tvLatitude;

    private Handler handler;
    private LocationManager mLocationManager;

    public static final int MSG_UPDATE_TEST = 0;


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

        mLocationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location tmpLocation = getLastBestLocation();

        if(null != tmpLocation){
            DPApplication dpapp = (DPApplication)getApplicationContext();
            dpapp.setLocation(tmpLocation);
            Log.e(TAG,"SETTING KNOWN LOCATION");

            tvLatitude.setText(tmpLocation.getLatitude()+"");
            tvLongtitude.setText(tmpLocation.getLongitude()+"");
        }else{
            Log.e(TAG,"unkonwn location");
        }
        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)
                && mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


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


    /**
     * @return the last know best location
     */
    private Location getLastBestLocation() {
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG,"onchange ");

            if (location != null) {
                Log.e("Map", "Location changed : Lat: "
                        + location.getLatitude() + " Lng: "
                        + location.getLongitude());
                DPApplication dpapp = (DPApplication)getApplicationContext();
                dpapp.setLocation(location);
                Log.e(TAG,"SETTING ON CHANGE LOCATION");
                tvLatitude.setText(location.getLatitude()+"");
                tvLongtitude.setText(location.getLongitude()+"");
            }else{
                Log.e(TAG,"onchange get null location");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}

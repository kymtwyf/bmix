package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import hackathon.dclab.com.minidianping.entities.MyLog;


public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        LogUtil logUtil = new LogUtil(this);
        List<MyLog>  myLogs = logUtil.getLogs();
        for(MyLog log :myLogs){
            Log.e("History Activity",log.getState()+"");

        }
    }


}

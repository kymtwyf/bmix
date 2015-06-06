package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import hackathon.dclab.com.minidianping.entities.MyLog;


public class HistoryActivity extends Activity {

    private TextView tvHistory;
    private TextView tvLike;
    private RelativeLayout rlContent;
    private ListFragment historyFragment;
    private ListFragment likeFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        LogUtil logUtil = new LogUtil(this);
        List<MyLog>  myLogs = logUtil.getLogs();
        for(MyLog log :myLogs){
            Log.e("History Activity",log.getState()+"");
        }

        tvHistory = (TextView)findViewById(R.id.tvHistory);
        tvLike = (TextView)findViewById(R.id.tvLike);
        rlContent = (RelativeLayout)findViewById(R.id.rl_list_content);

        historyFragment = ListFragment.newInstance(1);
//        likeFragment = ListFragment.newInstance(2);


        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_list_content,historyFragment);
        fragmentTransaction.commit();




    }


}

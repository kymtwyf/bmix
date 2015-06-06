package hackathon.dclab.com.minidianping;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hackathon.dclab.com.minidianping.domain.Business;
import hackathon.dclab.com.minidianping.entities.MyLog;

/**
 * Created by Yongfeng on 15/6/6.
 */
public class NewHistoryActivity extends Activity {
    private ListView listView;
    private SimpleAdapter logAdapter;
    private TextView tvBack;
    private ArrayList<Map<String,String>> logListContent;
    private List<MyLog> allLogs;
    int[] stateIcons = new int[]{R.drawable.button1,R.drawable.button2,R.drawable.banner_info_bg};//test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_history);
        tvBack = (TextView)findViewById(R.id.tvBack);
        allLogs = (new LogUtil(this)).getLogs();
        logListContent = new ArrayList<>();

        List<Long> allIds = new ArrayList<>();
        if(allLogs == null || allLogs.size() == 0){
            Toast.makeText(this,"没有历史浏览记录",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = allLogs.size()-1 ; i >= 0; i--){
                MyLog myLog = allLogs.get(i);
                Business business = myLog.getBusiness();
                if(!allIds.contains(business.id)){
                    allIds.add(business.id);
                    HashMap<String,String> hm = new HashMap<>();
                    hm.put("tvName",business.name);
                    hm.put("tvAverage",business.average_cost+"");
                    hm.put("tvRank", business.recommend_mark+"");
                    hm.put("ivState",Integer.toString(stateIcons[myLog.getState()-1]));
                    logListContent.add(hm);
                }
            }
            logAdapter = new SimpleAdapter(getApplicationContext(),logListContent
                    ,R.layout.history_item
                    ,new String[]{"tvName","tvAverage","tvRank","ivState"}
                    ,new int[]{R.id.tvName,R.id.tvAverage,R.id.tvRank,R.id.ivState});
            listView = (ListView)findViewById(R.id.listView);
            listView.setAdapter(logAdapter);
        }

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }
}

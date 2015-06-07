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
    private TextView tvHistory;
    private TextView tvLike;
    private ArrayList<Map<String,String>> logListContent;
    private ImageView ivIndicator;
    private List<MyLog> allLogs;

    private HistoryOnclickListener listener;
    int[] stateIcons = new int[]{R.drawable.icon_1,R.drawable.icon_2,R.drawable.icon_3};//test
//    int[] colors = new int[]{R.color.white,R.color}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_history);
        tvBack = (TextView)findViewById(R.id.tvBack);
        tvHistory = (TextView)findViewById(R.id.tvHistory);
        tvLike = (TextView)findViewById(R.id.tvLike);
        ivIndicator = (ImageView)findViewById(R.id.ivIndicator);
        allLogs = (new LogUtil(this)).getLogs();
        logListContent = new ArrayList<>();

        logAdapter = new SimpleAdapter(getApplicationContext(),logListContent
                ,R.layout.history_item
                ,new String[]{"tvName","tvAverage","tvRank","ivState"}
                ,new int[]{R.id.tvName,R.id.tvAverage,R.id.tvRank,R.id.ivState});
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(logAdapter);

        listener = new HistoryOnclickListener();
        tvBack.setOnClickListener(listener);
        tvHistory.setOnClickListener(listener);
        tvLike.setOnClickListener(listener);

        List<Long> allIds = new ArrayList<>();
        if(allLogs == null || allLogs.size() == 0){
            Toast.makeText(getApplicationContext(),"没有历史浏览记录",Toast.LENGTH_SHORT).show();
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
            logAdapter.notifyDataSetChanged();
        }
    }

    class HistoryOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tvBack:{
                    finish();
                    break;
                }
                case R.id.tvHistory:{
                    tvHistory.setTextColor(getResources().getColor(R.color.white));
                    tvHistory.setBackgroundColor(getResources().getColor(R.color.color_splitter_red));
                    tvLike.setTextColor(getResources().getColor(R.color.color_listview_label_grey));
                    tvLike.setBackgroundColor(getResources().getColor(R.color.white));
                    List<Long> allIds = new ArrayList<>();
                    logListContent.clear();
                    if(allLogs == null || allLogs.size() == 0){
                        Toast.makeText(getApplicationContext(),"没有历史浏览记录",Toast.LENGTH_SHORT).show();
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
                        logAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                case R.id.tvLike:{
                    tvHistory.setTextColor(getResources().getColor(R.color.color_listview_label_grey));
                    tvHistory.setBackgroundColor(getResources().getColor(R.color.white));
                    tvLike.setTextColor(getResources().getColor(R.color.white));
                    tvLike.setBackgroundColor(getResources().getColor(R.color.color_splitter_red));
                    List<Long> allIds = new ArrayList<>();
                    logListContent.clear();
                    if(allLogs == null || allLogs.size() == 0){
                        Toast.makeText(getApplicationContext(),"没有历史浏览记录",Toast.LENGTH_SHORT).show();
                    }else{
                        for(int i = allLogs.size()-1 ; i >= 0; i--){
                            MyLog myLog = allLogs.get(i);
                            Business business = myLog.getBusiness();
                            if(!allIds.contains(business.id) && myLog.getState() == 3){
//                                allLogs.remove(myLog);
                                allIds.add(business.id);
                                HashMap<String,String> hm = new HashMap<>();
                                hm.put("tvName",business.name);
                                hm.put("tvAverage",business.average_cost+"");
                                hm.put("tvRank", business.recommend_mark+"");
                                hm.put("ivState",Integer.toString(stateIcons[myLog.getState()-1]));
                                logListContent.add(hm);
                            }
                        }
                        logAdapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }
    }
}

package hackathon.dclab.com.minidianping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hackathon.dclab.com.minidianping.entities.MyLog;

/**
 * Created by Yongfeng on 15/6/6.
 */
public class ListFragment extends Fragment {
    int type; // 1-> history 2-> like
    private List<MyLog> allLogs;
    private LvHistoryAdapter lvHistoryAdapter;
    public static ListFragment newInstance(int type){
        ListFragment instance = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("type",type);
        instance.setArguments(args);
        return instance;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(null != args ){
            this.type = args.getInt("type");
        }else{
            Log.e("wocao,", "empty arguments in listfragment ");
            this.type = 1;
        }
        allLogs = (new LogUtil(getActivity().getApplicationContext())).getLogs();

        switch(type){
            case 1:{// history -> all logs
                break;
            }
            case 2:{
                List<MyLog> favLogs = new ArrayList<>();
                for(MyLog mylog:allLogs){
                    if(3 == mylog.getState()){
                        favLogs.add(mylog);
                    }
                }
                allLogs = favLogs;
                break;
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lv_history,container,false);
        ListView lv_content = (ListView)v.findViewById(R.id.listview);
        lvHistoryAdapter = new LvHistoryAdapter(getActivity().getApplicationContext(),allLogs);
        lv_content.setAdapter(lvHistoryAdapter);
        return v;

    }
}

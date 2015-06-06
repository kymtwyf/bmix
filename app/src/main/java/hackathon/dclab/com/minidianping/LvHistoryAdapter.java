package hackathon.dclab.com.minidianping;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import hackathon.dclab.com.minidianping.domain.Business;
import hackathon.dclab.com.minidianping.entities.MyLog;

/**
 * Created by Yongfeng on 15/6/6.
 */
public class LvHistoryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<MyLog> logs = null;
    public LvHistoryAdapter(Context context, List<MyLog> logs){
        this.context = context;
        this.logs = logs;
        this.inflater = LayoutInflater.from(context);

    }
    public class ViewHolder {
        ImageView ivState;
        TextView name;
        TextView average;
        TextView rank;
    }
    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public Object getItem(int i) {
        return logs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.history_item,null);

            viewHolder.ivState = (ImageView)view.findViewById(R.id.ivState);
            viewHolder.name = (TextView)view.findViewById(R.id.tvName);
            viewHolder.average = (TextView)view.findViewById(R.id.tvAverage);
            viewHolder.rank = (TextView)view.findViewById(R.id.tvRank);


            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        if(logs == null || logs.size() == 0){
            Log.e("wocao,","没有LOG");
            return view;
        }
        Business curBus = logs.get(i).getBusiness();
        if(logs.get(i).getIcon() != null){
            viewHolder.ivState.setImageBitmap(logs.get(i).getIcon());
        }else{
            Log.e("lvHistory ,", "no image " + i);
        }
        viewHolder.name.setText(curBus.name);
        viewHolder.average.setText(logs.get(i).getBusiness().average_cost + "");
        viewHolder.rank.setText(curBus.recommend_mark + "");




        return view;
    }
}

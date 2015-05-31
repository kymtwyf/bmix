package hackathon.dclab.com.minidianping;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hackathon.dclab.com.minidianping.domain.Business;

/**
 * Created by Yongfeng on 15/5/31.
 */
public class LogUtil {
    public static String STORAGE_KEY = "LOG.db";
    private List<Log> logs = null;
    private Context context;
    public LogUtil(Context context){
        this.context = context;
        logs = new ArrayList<Log>();
        try {
            logs = (List<Log>)InternalStorage.readObject(context,STORAGE_KEY);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void insertLog(Log log){
        this.logs.add(log);
    }
    public List<Log> getLogs(){
        return this.logs;
    }

    public void storeLogs(){
        try {
            InternalStorage.writeObject(context, STORAGE_KEY, logs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Log implements Serializable{
        private Bitmap icon;
        private Business business;

        private int state;// 1 勾选过， 2 没勾过

        public Bitmap getIcon() {
            return icon;
        }

        public void setIcon(Bitmap icon) {
            this.icon = icon;
        }

        public Business getBusiness() {
            return business;
        }

        public void setBusiness(Business business) {
            this.business = business;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            if(1 != state && 2 != state ){
                state = 1;//你不能这么做吧，不给1不给2
            }
            this.state = state;
        }
    }

}

package hackathon.dclab.com.minidianping;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hackathon.dclab.com.minidianping.domain.Business;
import hackathon.dclab.com.minidianping.entities.MyLog;

/**
 * Created by Yongfeng on 15/5/31.
 */
public class LogUtil {
    public static String STORAGE_KEY = "LOG.db";
    private List<MyLog> logs = null;
    private Context context;
    public LogUtil(Context context){
        this.context = context;
        logs = new ArrayList<MyLog>();
        try {
            logs = (List<MyLog>)InternalStorage.readObject(context,STORAGE_KEY);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void insertLog(MyLog log){
        this.logs.add(log);
    }
    public List<MyLog> getLogs(){
        return this.logs;
    }

    public void storeLogs(){
        try {
            InternalStorage.writeObject(context, STORAGE_KEY, logs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

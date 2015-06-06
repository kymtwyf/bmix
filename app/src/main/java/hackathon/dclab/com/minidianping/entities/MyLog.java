package hackathon.dclab.com.minidianping.entities;

/**
 * Created by Yongfeng on 15/5/31.
 */

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

import hackathon.dclab.com.minidianping.InternalStorage;
import hackathon.dclab.com.minidianping.domain.Business;

public class MyLog implements Serializable {
    static final long serialVersionUID =5582055119083809202L;
    private Bitmap icon;
    private Business business;

    private int state;// 1 勾选过， 2 没勾过 3 收藏

    public MyLog(){
        this.icon =null;
        this.business = null;
        this.state = 1;
    }
    public MyLog(Bitmap bm, Business business, int sate){
        this.icon = bm;
        this.business = business;
        this.state = state;
    }
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
        if(1 != state && 2 != state && 3 != state){
            state = 1;//你不能这么做吧，不给1不给2 也不给3
        }
        this.state = state;
    }
}
package hackathon.dclab.com.minidianping.entities;

/**
 * Created by Yongfeng on 15/5/31.
 */

import android.graphics.Bitmap;

import java.io.Serializable;

import hackathon.dclab.com.minidianping.domain.Business;

public class MyLog implements Serializable {
    private Bitmap icon;
    private Business business;

    private int state;// 1 勾选过， 2 没勾过

    public MyLog(){
        this.icon =null;
        this.business = null;
        this.state = 1;
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
        if(1 != state && 2 != state ){
            state = 1;//你不能这么做吧，不给1不给2
        }
        this.state = state;
    }
}
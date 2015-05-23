package hackathon.dclab.com.minidianping.entities;

/**
 * Created by Yongfeng on 15/5/22.
 */
public class User {
    private String id;
    private String mobile;
    private String name;
    private float longitude;
    private float latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float[] getLocation(){
        return new float[]{longitude,latitude};
    }

    public void setLocation(float longitude,float latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

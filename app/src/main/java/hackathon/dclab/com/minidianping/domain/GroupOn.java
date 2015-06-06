package hackathon.dclab.com.minidianping.domain;

import java.io.Serializable;

/**
 * Created by weicheng on 26/5/15.
 */
public class GroupOn implements Serializable{
    public GroupOn(String id, String url, String description){
        this.id = id;
        this.url = url;
        this.description = description;
    }
    public String id;
    public String url;
    public String description;
}

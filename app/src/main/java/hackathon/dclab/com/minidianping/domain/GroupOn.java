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
    private String id;
    private String url;
    private String description;
}

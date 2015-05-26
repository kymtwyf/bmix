package hackathon.dclab.com.minidianping.domain;

/**
 * Created by weicheng on 26/5/15.
 */
public class Deal {
    public Deal(long id,String description, String url){
        this.id = id;
        this.description = description;
        this.url = url;
    }
    public void setId(long id){
        this.id= id;
    }
    public long getId(){
        return id;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return url;
    }
    private long id = 0;
    private String description="";
    private String url= "";

}

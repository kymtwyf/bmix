package hackathon.dclab.com.minidianping.domain;

/**
 * Created by weicheng on 26/5/15.
 */
public class GroupOn {
    public GroupOn(String type,String name, int price, String url){
        this.type = type;
        this.name = name;
        this.url = url;
        this.price = price;
    }
    private String type;
    private String name;
    private String url;
    private int price;
}

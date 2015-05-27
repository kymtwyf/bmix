package hackathon.dclab.com.minidianping.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yongfeng on 15/5/22.
 */
public class Restaurant {
    private String id;
    private String name;
    private float rating;
    private int average;
    private Map<String,String> mustOrder;
    private List<String> recommendReason;

    public Restaurant(String name,int rating){
        this.name = name;
        this.rating = rating;
        this.mustOrder = new HashMap<String,String>();
        this.recommendReason = new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public Map<String, String> getMustOrder() {
        return mustOrder;
    }

    public void setMustOrder(Map<String, String> mustOrder) {
        this.mustOrder = mustOrder;
    }

    public List<String> getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(List<String> recommendReason) {
        this.recommendReason = recommendReason;
    }
}

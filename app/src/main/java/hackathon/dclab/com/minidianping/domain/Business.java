package hackathon.dclab.com.minidianping.domain;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weicheng on 26/5/15.
 */
public class Business implements Serializable{
    public static class Dish implements Serializable{
        public String name;
        public int price;
        public String index;
        public Dish(String name,int price, String index){
            this.name = name;
            this.price = price;
            this.index = index;
        }
    }
    public long id = 0;
    public long dp_business_id = 0;
    public String name="";             //店的名称
    public String pic_url="";
    public String telephones = "";        //电话
    public String address="";          //地址
    public double latitude=0;          //纬度
    public double longitude = 0;       //经度
    public int distance = 0;           //距离
    public int average_cost = 80;    //人均消费
    public double recommend_mark = 8.5;
    public List<String> recommend_reason = null;
    public List<Dish>  favourite_dishes = null;
    public List<GroupOn> groupon;


    public Business(long id, long dp_business_id, String name, String pic_url, String telephones, String address, double latitude, double longitude
                    , int distance,int average_cost,double recommend_mark, List<String> recommend_reason, List<Dish> favourite_dishes, List<GroupOn> groupon){
        this.id = id;
        this.dp_business_id = dp_business_id;
        this.name = name;
        this.pic_url = pic_url;
        this.telephones = telephones;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.average_cost = average_cost;
        this.recommend_mark = recommend_mark;
        this.recommend_reason = recommend_reason;
        this.favourite_dishes = favourite_dishes;
        this.groupon = groupon;
        this.average_cost = average_cost;
    }

    public String ToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("average_cost: ");
        sb.append(average_cost);
        return sb.toString();
    }

    public static List<Business> getBusinessFromJson(String json){
        Gson gs = new Gson();
        List<Business> business_list = new ArrayList<Business>();
        try {
            JSONObject datajson = new JSONObject(json);
            JSONArray businesses = datajson.getJSONArray("businesses");
            for(int i=0; i<businesses.length();++i){
                JSONObject temp = (JSONObject)businesses.get(i);
                //id
                long id = temp.getLong("business_id");
                //点评id
                long business_id = temp.getLong("business_id");
                //餐厅名字
                String name = temp.getString("name");
                //图片url
                String pic_url = temp.getString("photo_url");
                //电话
                String telephone = temp.getString("telephone");
                //地址
                String address = temp.getString("address");
                //维度
                double latitude = temp.getDouble("latitude");
                //经度
                double longitude = temp.getDouble("longitude");
                //距离
                int distance = temp.getInt("distance");
                //人均消费
                int average_cost = temp.getInt("avg_price");
                //推荐理由
                double recommend_mark = temp.getDouble("avg_rating") * 2;
                JSONArray reason = temp.getJSONArray("recommend_reason");
                List<String> recommend_reason = new ArrayList<String>();
                for(int j=0; j<reason.length(); ++j){
                    recommend_reason.add(reason.getString(j));
                }
                //推荐菜单
                JSONArray dishes = temp.getJSONArray("favourite_dishes");
                List<Dish> favouriate_dishes = new ArrayList<Dish>();
                for(int j=0; j<dishes.length(); ++j){
                    JSONObject tt = (JSONObject)dishes.get(j);
                    String dname = tt.getString("name");
                    int dprice = tt.getInt("price");
                    //String dindex = tt.getString("id");
                    favouriate_dishes.add(new Dish(dname,dprice,"0"));
                }
                //团购
                JSONArray dealsArray = temp.getJSONArray("deals");
                List<GroupOn> deals = new ArrayList<GroupOn>();
                for(int j=0; j<dealsArray.length(); ++j){
                    JSONObject tt = (JSONObject)dealsArray.get(j);
                    String deal_id = tt.getString("id");
                    String deal_url = tt.getString("url");
                    String deal_description = tt.getString("description");
                    deals.add(new GroupOn(deal_id,deal_url,deal_description));
                }
                //JSONObject grouponjson = temp.getJSONObject("groupon");
                //GroupOn groupon = new GroupOn(grouponjson.getString("type"),grouponjson.getString("name"),grouponjson.getInt("price"),grouponjson.getString("url"));
                business_list.add(new Business(id,business_id,name,pic_url,telephone,address,latitude,longitude,distance,average_cost,
                        recommend_mark,recommend_reason,favouriate_dishes,deals));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return business_list;
    }
}

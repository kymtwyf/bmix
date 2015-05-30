package hackathon.dclab.com.minidianping.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weicheng on 26/5/15.
 */
public class Business {
    public static class Dish{
        public String name;
        public int price;
        public int index;
        public Dish(String name,int price, int index){
            this.name = name;
            this.price = price;
            this.index = index;
        }
    }
    public long id = 0;
    public long dp_business_id = 0;
    public String name="";             //店的名称
    public String pic_url="";
    public List<String> telephones = null;        //电话
    public String address="";          //地址
    public double latitude=0;          //纬度
    public double longitude = 0;       //经度
    public int distance = 0;           //距离
    public double recommend_mark = 8.5;
    public List<String> recommend_reason = null;
    public List<Dish>  favourite_dishes = null;


    public Business(long id, long dp_business_id, String name, String pic_url, List<String> telephones, String address, double latitude, double longitude
                    , int distance,double recommend_mark, List<String> recommend_reason, List<Dish> favourite_dishes ){
        this.id = id;
        this.dp_business_id = dp_business_id;
        this.name = name;
        this.pic_url = pic_url;
        this.telephones = telephones;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.recommend_mark = recommend_mark;
        this.recommend_reason = recommend_reason;
        this.favourite_dishes = favourite_dishes;
    }

    public static List<Business> getBusinessFromJson(String json){
        Gson gs = new Gson();
        //List<Business> business_list= gs.fromJson(json,new TypeToken<List<Business>>(){}.getType());
        List<Business> business_list = new ArrayList<Business>();
        try {
            JSONObject datajson = new JSONObject(json);
            JSONArray businesses = datajson.getJSONArray("businesses");
            for(int i=0; i<businesses.length();++i){
                JSONObject temp = (JSONObject)businesses.get(i);
                long id = 0;
                long business_id = temp.getLong("dp_business_id");
                String name = temp.getString("name");
                String pic_url = temp.getString("pic_url");

                List<String> telephones = new ArrayList<String>();
                JSONArray teles = temp.getJSONArray("telephones");
                for(int j=0; j<teles.length(); ++j){
                    JSONObject tt = (JSONObject)teles.get(j);
                    telephones.add(tt.toString());
                }

                String address = temp.getString("address");
                double latitude = temp.getDouble("latitude");
                double longitude = temp.getDouble("longitude");
                int distance = temp.getInt("distance");
                double recommend_mark = temp.getDouble("recommend_mark");
                JSONArray reason = temp.getJSONArray("recommend_reason");
                List<String> recommend_reason = new ArrayList<String>();
                for(int j=0; j<reason.length(); ++j){
                    JSONObject tt = (JSONObject)reason.get(j);
                    recommend_reason.add(tt.toString());
                }

                JSONArray dishes = temp.getJSONArray("favouriate_dishes");
                List<Dish> favouriate_dishes = new ArrayList<Dish>();
                for(int j=0; j<dishes.length(); ++j){
                    JSONObject tt = (JSONObject)dishes.get(j);
                    String dname = tt.getString("name");
                    int dprice = tt.getInt("price");
                    int dindex = tt.getInt("index");
                    favouriate_dishes.add(new Dish(dname,dprice,dindex));
                }
                business_list.add(new Business(id,business_id,name,pic_url,telephones,address,latitude,longitude,distance,
                        recommend_mark,recommend_reason,favouriate_dishes));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return business_list;
    }
}

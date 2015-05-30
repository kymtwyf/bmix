package hackathon.dclab.com.minidianping;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hackathon.dclab.com.minidianping.entities.GeoInfo;
import hackathon.dclab.com.minidianping.entities.Mode;

/**
 * Created by weicheng on 30/5/15.
 */
public class HTTPUtils {
    String action="";
    HttpPost httpRequest=null;
    List<BasicNameValuePair> params=null;
    HttpResponse httpResponse;
    public HTTPUtils() {
        /*建立HttpPost连接*/
        httpRequest=new HttpPost(action);
        /*Post运作传送变数必须用NameValuePair[]阵列储存*/
        params=new ArrayList<BasicNameValuePair>();
    }

    public void setParams(String key, String value){
        params.add(new BasicNameValuePair(key,value));
    }

    public String Recommend(String userid, GeoInfo geo, Mode mode){
        action = "http://pybackend.mybluemix.net/recommend";
        String strResult="";
        Gson gs = new Gson();
        setParams("user_id",userid);
        setParams("geo_info",gs.toJson(geo));
        System.out.println(gs.toJson(geo));
        setParams("mode", gs.toJson(mode));
        System.out.println(gs.toJson(mode));
        try {
            //发出HTTP request
            strResult = NetUtils.postRequest(action,params);
            System.out.println(strResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return strResult;
    }
}

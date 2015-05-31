package hackathon.dclab.com.minidianping;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import hackathon.dclab.com.minidianping.domain.Business;
import hackathon.dclab.com.minidianping.entities.GeoInfo;
import hackathon.dclab.com.minidianping.entities.Mode;

/**
 * Created by weicheng on 30/5/15.
 */
public class HttpGetRecommend implements Callable<String>{
    String action="http://pybackend.mybluemix.net/find_businesses";
    HttpPost httpRequest=null;
    List<NameValuePair> params=null;
    HttpResponse httpResponse;
    String userid;
    GeoInfo geo;
    Mode mode;
    String strResult = "";
    public HttpGetRecommend(String userid, GeoInfo geo, Mode mode) {
        /*建立HttpPost连接*/
        httpRequest=new HttpPost(action);
        /*Post运作传送变数必须用NameValuePair[]阵列储存*/
        params=new ArrayList<NameValuePair>();
        this.userid = userid;
        this.geo = geo;
        this.mode = mode;
    }

    public void setParams(String key, String value) {
        params.add(new BasicNameValuePair(key,value));
    }

    @Override
    public String call() throws Exception {
        Gson gs = new Gson();
        setParams("user_id",userid);
        setParams("geo_info",gs.toJson(geo));
        //System.out.println(gs.toJson(geo));
        setParams("mode", gs.toJson(mode));
        try {
            //发出HTTP request
            strResult = NetUtils.postRequest(action,params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strResult;
    }
}

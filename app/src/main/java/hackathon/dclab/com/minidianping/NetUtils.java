package hackathon.dclab.com.minidianping;

/**
 * Created by Yongfeng on 15/5/21.
 *
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;

/**
 * 网络工具类
 *
 */

public class NetUtils {
    /**
     * 对网络连接状态进行判断
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    /**
     * get请求
     *
     * @param urlString
     * @param params
     * @return
     */
    public static String getRequest(String urlString, Map<String, String> params) {

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(urlString);

            if (null != params) {

                urlBuilder.append("?");

                Iterator<Entry<String, String>> iterator = params.entrySet()
                        .iterator();

                while (iterator.hasNext()) {
                    Entry<String, String> param = iterator.next();
                    urlBuilder
                            .append(URLEncoder.encode(param.getKey(), "UTF-8"))
                            .append('=')
                            .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    if (iterator.hasNext()) {
                        urlBuilder.append('&');
                    }
                }
            }
            // 创建HttpClient对象
            HttpClient client = getNewHttpClient();
            // 发送get请求创建HttpGet对象
            HttpGet getMethod = new HttpGet(urlBuilder.toString());
            HttpResponse response = client.execute(getMethod);
            // 获取状态码
            int res = response.getStatusLine().getStatusCode();
            if (res == 200) {

                StringBuilder builder = new StringBuilder();
                // 获取响应内容
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                for (String s = reader.readLine(); s != null; s = reader
                        .readLine()) {
                    builder.append(s);
                }
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private synchronized static DefaultHttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
		/* 从连接池中取连接的超时时间 */
        ConnManagerParams.setTimeout(params, 30000);
		/* 连接超时 */
        HttpConnectionParams.setConnectionTimeout(params, 30000);
		/* 请求超时 */
        HttpConnectionParams.setSoTimeout(params, 30000);
        // 设置我们的HttpClient支持HTTP和HTTPS两种模式
        SchemeRegistry reg = new SchemeRegistry();
        SSLSocketFactory sf = null;
        ClientConnectionManager conMgr = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);
            sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            reg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            reg.register(new Scheme("https", sf, 443));
            // 使用线程安全的连接管理来创建HttpClient
            conMgr = new ThreadSafeClientConnManager(params, reg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultHttpClient(conMgr, params);
    }

    /**
     * post请求
     *
     * @param urlString
     * @param params
     * @return
     */
    public static String postRequest(String urlString,
                                     List<NameValuePair> params) {

        try {
            // 2. 发get请求创建HttpGet对象
            HttpPost postMethod = new HttpPost(urlString);
            System.out.println(urlString);
            postMethod.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            //HttpResponse response = new DefaultHttpClient().execute(postMethod);
            HttpResponse response = getHttpClient().execute(postMethod);
            int statueCode = response.getStatusLine().getStatusCode();
            if (statueCode == 200) {
                System.out.println(statueCode);
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Bitmap getBitmap(String imgUrl) {
        Bitmap bm = null;
        try {
            HttpGet getMethod = new HttpGet(imgUrl);

            HttpResponse response = getHttpClient().execute(getMethod);
            int statueCode = response.getStatusLine().getStatusCode();
            if(statueCode == 200) {
                System.out.print(statueCode);
                InputStream is = response.getEntity().getContent();
                bm = BitmapFactory.decodeStream(is);
                is.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bm;
    }
    // 保存时+当时的秒数，
    public static long expires(String second) {
        Long l = Long.valueOf(second);

        return l * 1000L + System.currentTimeMillis();
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}

package hackathon.dclab.com.minidianping.entities;

/**
 * Created by weicheng on 30/5/15.
 */
public class GeoInfo {
    public String county="";
    public String city="";
    public String district = "";
    public double longitude = 0;
    public double latitude = 0;

    public GeoInfo(String country, String city, String district,double longitude, double latitude){
        this.county = country;
        this.city = city;
        this.district = district;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

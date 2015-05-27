package hackathon.dclab.com.minidianping.domain;

/**
 * Created by weicheng on 26/5/15.
 */
public class Merchant {
    private long business_id = 0;
    private String name="";             //店的名称
    private String branch_name="";      //分店
    private String address="";          //地址
    private String telephone = "";      //电话
    private String city="";             //城市
    private String[] regions={""};      //区域
    private String[] categories = {""}; //分类
    private double latitude=0;          //纬度
    private double longitude = 0;       //经度
    private double avg_rating = 0;      //平均评价
    private double product_grade = 0;   //产品等级
    private double decoration_grade=0;  //装修等级
    private double service_grade = 0;   //服务等级
    private double product_score=0;     //产品得分
    private double decoration_score=0;  //装修得分
    private double service_score=0;     //服务得分
    private int avg_price = 0;          //平均价格
    private int review_count = 0;       //评价数量
    private String review_list_url="";  //评价列表网址
    private int distance=0;             //距离
    private String photo_url = "";      //照片URL
    private String s_phtot_url="";      //小照片url
    private int photo_count = 0;        //照片数量
    private String photo_list_url ="";  //照片列表URL
    private boolean has_coupon = false;         //有无优惠券
    private long coupon_id=0;           //优惠券id
    private String coupon_description="";//优惠券描述
    private String coupon_url="";       //优惠券网址
    private boolean has_deal=false;             //有无团购
    private int deal_count=0;           //团购数量
    private Deal[] deals;               //团购的数组
    private boolean has_online_reservation = false;//是否有网上订座
    private String nline_reservation_url="";//网上订座URL

    public Merchant(long id, String name, String address, String branch_name, String telephone, String city, String[]regions, String[] categories,
                    double latitude, double longitude, double avg_rating, int avg_price){
        this.business_id = id;
        this.address = address;
        this.branch_name = branch_name;
        this.telephone = telephone;
        this.city = city;
        this.regions = regions;
        this.categories = categories;
        this.latitude = latitude;
        this.longitude = longitude;
        this.avg_rating = avg_rating;
        this.avg_price = avg_price;
    }
}

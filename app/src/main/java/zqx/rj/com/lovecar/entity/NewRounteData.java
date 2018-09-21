package zqx.rj.com.lovecar.entity;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.entity
 * 文件名：  NewRounteData
 * 创建者：  ZQX
 * 创建时间：2018/4/20 17:30
 * 描述：    最新路线实体类
 */

public class NewRounteData {

    // 车票 ID
    private String id;
    // 负责人
    private String publisher;
    // 上车地点
    private String from_place;
    // 下车地点
    private String to_place;
    // 车牌号码
    private String plate_number;
    // 价格
    private String price;
    //包车时间
    private String departure_time;
    // 车票数量
    private String surplus;
    // 创建时间
    private String create_time;
    // 用户 id
    private String user_id;
    // 手机号码
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getFrom_place() {
        return from_place;
    }

    public void setFrom_place(String from_place) {
        this.from_place = from_place;
    }

    public String getTo_place() {
        return to_place;
    }

    public void setTo_place(String to_place) {
        this.to_place = to_place;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

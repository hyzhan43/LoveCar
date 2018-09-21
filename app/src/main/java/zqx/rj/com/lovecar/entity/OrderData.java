package zqx.rj.com.lovecar.entity;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.entity
 * 文件名：  OrderData
 * 创建者：  ZQX
 * 创建时间：2018/5/9 19:11
 * 描述：    订单实体类
 */

public class OrderData {

    private String create_time;
    private String departure_time;
    private String from_place;
    private String to_place;
    private String price;
    private String order_number;
    private String poll;
    private String total_price;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getPoll() {
        return poll;
    }

    public void setPoll(String poll) {
        this.poll = poll;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}

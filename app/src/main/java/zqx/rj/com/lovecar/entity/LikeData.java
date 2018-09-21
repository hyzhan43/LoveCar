package zqx.rj.com.lovecar.entity;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.entity
 * 文件名：  LikeData
 * 创建者：  ZQX
 * 创建时间：2018/5/25 17:20
 * 描述：    收藏
 */

public class LikeData {

    private String collection_id;
    private String create_time;
    private String publish_id;
    private String departure_time;
    private String from_place;
    private String to_place;
    private String price;
    private String surplus;

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPublish_id() {
        return publish_id;
    }

    public void setPublish_id(String publish_id) {
        this.publish_id = publish_id;
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
}

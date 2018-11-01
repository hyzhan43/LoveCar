package zqx.rj.com.lovecar.entity;

import java.util.List;

/**
 * author：  HyZhan
 * created： 2018/11/1 20:56
 * desc：    TODO
 */

public class Record {

    private String total;
    private String by_count;
    private String sug;
    private List<Line> pic;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBy_count() {
        return by_count;
    }

    public void setBy_count(String by_count) {
        this.by_count = by_count;
    }

    public String getSug() {
        return sug;
    }

    public void setSug(String sug) {
        this.sug = sug;
    }

    public List<Line> getPic() {
        return pic;
    }

    public void setPic(List<Line> pic) {
        this.pic = pic;
    }
}

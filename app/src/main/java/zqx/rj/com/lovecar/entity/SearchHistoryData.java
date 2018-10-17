package zqx.rj.com.lovecar.entity;

import org.litepal.crud.DataSupport;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.entity
 * 文件名：  SearchHistoryData
 * 创建者：  ZQX
 * 创建时间：2018/5/15 13:31
 * 描述：    搜索历史记录
 */

public class SearchHistoryData extends DataSupport {

    private int id;
    private String startPlace;
    private String endPlace;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof SearchHistoryData) {
            SearchHistoryData other = (SearchHistoryData) obj;

            return this.startPlace.equals(other.startPlace)
                    && this.endPlace.equals(other.endPlace);
        }

        return super.equals(obj);
    }
}

package zqx.rj.com.lovecar.entity;

import java.util.List;

/**
 * author：  HyZhan
 * created： 2018/11/1 20:11
 * desc：    TODO
 */

public class Crowd {

    private String least;
    private String most;

    private String tatal;
    private List<Pic> pic;

    public String getLeast() {
        return least;
    }

    public void setLeast(String least) {
        this.least = least;
    }

    public String getMost() {
        return most;
    }

    public void setMost(String most) {
        this.most = most;
    }

    public String getTatal() {
        return tatal;
    }

    public void setTatal(String tatal) {
        this.tatal = tatal;
    }

    public List<Pic> getPicList() {
        return pic;
    }

    public void setPicList(List<Pic> picList) {
        pic = picList;
    }
}

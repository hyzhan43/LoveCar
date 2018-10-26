package zqx.rj.com.lovecar.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * author：  HyZhan
 * created： 2018/10/26 16:26
 * desc：    TODO
 */

public class MomentsInfo {
    private String create_time;
    private String content;
    private String thumb;
    private String username;
    private ArrayList<String> img_items;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getImg_items() {
        return img_items;
    }

    public void setImg_items(ArrayList<String> img_items) {
        this.img_items = img_items;
    }
}

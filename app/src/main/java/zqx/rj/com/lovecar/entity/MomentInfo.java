package zqx.rj.com.lovecar.entity;

import com.lzy.ninegrid.ImageInfo;

import java.util.List;

/**
 * author：  HyZhan
 * created：2018/9/28 23:59
 * desc：    TODO
 */

public class MomentInfo {

    private String name;
    private String iconUrl;
    private String content;
    private String time;
    private List<ImageInfo> images;

    public MomentInfo(String name, String iconUrl, String content, String time, List<ImageInfo> images) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.content = content;
        this.time = time;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ImageInfo> getImages() {
        return images;
    }

    public void setImages(List<ImageInfo> images) {
        this.images = images;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

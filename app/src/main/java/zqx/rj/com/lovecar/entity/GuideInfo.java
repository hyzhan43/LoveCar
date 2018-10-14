package zqx.rj.com.lovecar.entity;

/**
 * author：  HyZhan
 * created：2018/9/29 0:53
 * desc：    TODO
 */

public class GuideInfo {

    private String imageUrl;
    private String title;
    private String time;

    public GuideInfo(String imageUrl, String title, String time) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

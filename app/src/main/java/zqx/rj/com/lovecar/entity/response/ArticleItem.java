package zqx.rj.com.lovecar.entity.response;

/**
 * author：  HyZhan
 * created： 2018/10/23 19:30
 * desc：    TODO
 */

public class ArticleItem {
    private String id;
    private String display_time;
    private String title;
    private String source_uri;
    private String image_uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay_time() {
        return display_time;
    }

    public void setDisplay_time(String display_time) {
        this.display_time = display_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource_uri() {
        return source_uri;
    }

    public void setSource_uri(String source_uri) {
        this.source_uri = source_uri;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}

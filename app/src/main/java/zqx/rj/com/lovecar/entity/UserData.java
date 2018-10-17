package zqx.rj.com.lovecar.entity;

import com.google.gson.annotations.SerializedName;

/**
 * author：  HyZhan
 * created： 2018/10/14 14:36
 * desc：    用户头像
 */

public class UserData {

    @SerializedName("media_id")
    private String mediaId;

    private String thumb;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}

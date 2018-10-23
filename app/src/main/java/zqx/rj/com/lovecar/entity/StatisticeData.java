package zqx.rj.com.lovecar.entity;

import android.graphics.drawable.Drawable;

/**
 * author：  HyZhan
 * created： 2018/10/17 14:56
 * desc：    TODO
 */

public class StatisticeData {
    private String title;
    private int image;

    public StatisticeData(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

package zqx.rj.com.lovecar.entity;

import java.util.List;

import zqx.rj.com.lovecar.entity.response.ArticleItem;

/**
 * author：  HyZhan
 * created： 2018/10/23 18:10
 * desc：    TODO
 */

public class Article {

    private List<ArticleItem> items;

    private String total;

    public List<ArticleItem> getItems() {
        return items;
    }

    public void setItems(List<ArticleItem> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

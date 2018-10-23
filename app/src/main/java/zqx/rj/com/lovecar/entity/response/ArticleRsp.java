package zqx.rj.com.lovecar.entity.response;

import java.util.List;

import zqx.rj.com.lovecar.entity.Article;

/**
 * author：  HyZhan
 * created： 2018/10/23 18:08
 * desc：    TODO
 */

public class ArticleRsp extends BaseResponse {

    private Article data;

    public Article getData() {
        return data;
    }

    public void setData(Article data) {
        this.data = data;
    }
}

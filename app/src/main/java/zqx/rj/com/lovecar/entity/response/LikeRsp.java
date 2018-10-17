package zqx.rj.com.lovecar.entity.response;

import java.util.List;

import zqx.rj.com.lovecar.entity.LikeData;

/**
 * author：  HyZhan
 * created： 2018/10/15 17:56
 * desc：    TODO
 */

public class LikeRsp extends BaseResponse {
    private List<LikeData> data;

    public List<LikeData> getData() {
        return data;
    }

    public void setData(List<LikeData> data) {
        this.data = data;
    }
}

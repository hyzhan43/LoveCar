package zqx.rj.com.lovecar.entity.response;

import java.util.List;

import zqx.rj.com.lovecar.entity.MomentsInfo;

/**
 * author：  HyZhan
 * created： 2018/10/26 16:25
 * desc：    TODO
 */

public class CircleRsp extends BaseResponse{
    private List<MomentsInfo> data;

    public List<MomentsInfo> getData() {
        return data;
    }

    public void setData(List<MomentsInfo> data) {
        this.data = data;
    }
}

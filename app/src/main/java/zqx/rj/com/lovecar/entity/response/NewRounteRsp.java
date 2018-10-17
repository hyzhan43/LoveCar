package zqx.rj.com.lovecar.entity.response;

import java.util.List;

import zqx.rj.com.lovecar.entity.NewRounteData;

/**
 * author：  HyZhan
 * created： 2018/10/15 17:46
 * desc：    最新路线返回
 */

public class NewRounteRsp extends BaseResponse {

    private List<NewRounteData> data;

    public List<NewRounteData> getData() {
        return data;
    }

    public void setData(List<NewRounteData> data) {
        this.data = data;
    }
}

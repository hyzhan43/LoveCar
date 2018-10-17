package zqx.rj.com.lovecar.entity.response;

import zqx.rj.com.lovecar.entity.UserData;

/**
 * author：  HyZhan
 * created： 2018/10/15 17:54
 * desc：    TODO
 */

public class IconRsp extends BaseResponse{
    private UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}

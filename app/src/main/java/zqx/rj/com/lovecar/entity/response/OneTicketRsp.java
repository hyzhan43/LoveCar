package zqx.rj.com.lovecar.entity.response;

import zqx.rj.com.lovecar.entity.NewRounteData;

/**
 * author：  HyZhan
 * created： 2018/10/15 18:14
 * desc：    TODO
 */

public class OneTicketRsp extends BaseResponse{

    private NewRounteData data;

    public NewRounteData getData() {
        return data;
    }

    public void setData(NewRounteData data) {
        this.data = data;
    }
}

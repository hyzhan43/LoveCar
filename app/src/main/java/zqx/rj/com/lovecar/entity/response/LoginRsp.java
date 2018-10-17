package zqx.rj.com.lovecar.entity.response;

import zqx.rj.com.lovecar.entity.Account;

/**
 * author：  HyZhan
 * created： 2018/10/14 10:16
 * desc：    TODO
 */

public class LoginRsp extends BaseResponse{

    private Account data;

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }
}

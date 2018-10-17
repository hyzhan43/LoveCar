package zqx.rj.com.lovecar.entity.response;

import zqx.rj.com.lovecar.entity.Code;

/**
 * author：  HyZhan
 * created： 2018/10/15 18:09
 * desc：    TODO
 */

public class CodeRsp extends BaseResponse{
    private Code data;

    public Code getData() {
        return data;
    }

    public void setData(Code data) {
        this.data = data;
    }
}

package zqx.rj.com.lovecar.entity.response;

import java.util.List;

import zqx.rj.com.lovecar.entity.SamePeopleData;

/**
 * author：  HyZhan
 * created： 2018/10/15 18:12
 * desc：    TODO
 */

public class SamePeopleRsp extends BaseResponse{
    private List<SamePeopleData> data;

    public List<SamePeopleData> getData() {
        return data;
    }

    public void setData(List<SamePeopleData> data) {
        this.data = data;
    }
}

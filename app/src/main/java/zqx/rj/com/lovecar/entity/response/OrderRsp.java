package zqx.rj.com.lovecar.entity.response;

import java.util.List;

import zqx.rj.com.lovecar.entity.OrderData;

/**
 * author：  HyZhan
 * created： 2018/10/15 17:51
 * desc：    TODO
 */

public class OrderRsp extends BaseResponse {
    private List<OrderData> data;

    public List<OrderData> getData() {
        return data;
    }

    public void setData(List<OrderData> data) {
        this.data = data;
    }
}

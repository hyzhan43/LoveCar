package zqx.rj.com.lovecar.entity.response;

import zqx.rj.com.lovecar.entity.OrderDetailData;

/**
 * author：  HyZhan
 * created： 2018/10/15 17:59
 * desc：    TODO
 */

public class OrderDetailRsp extends BaseResponse{
    private OrderDetailData data;

    public OrderDetailData getData() {
        return data;
    }

    public void setData(OrderDetailData data) {
        this.data = data;
    }
}

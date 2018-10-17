package zqx.rj.com.lovecar.entity.response;

import java.util.List;

import zqx.rj.com.lovecar.entity.SearchResultData;

/**
 * author：  HyZhan
 * created： 2018/10/15 18:05
 * desc：    TODO
 */

public class SearchTicketsRsp extends BaseResponse{
    private List<SearchResultData> data;

    public List<SearchResultData> getData() {
        return data;
    }

    public void setData(List<SearchResultData> data) {
        this.data = data;
    }
}

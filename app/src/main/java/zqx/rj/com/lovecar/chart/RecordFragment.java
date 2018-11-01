package zqx.rj.com.lovecar.chart;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.lang.ref.WeakReference;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.Line;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.Record;
import zqx.rj.com.lovecar.entity.response.CrowdRsp;
import zqx.rj.com.lovecar.entity.response.RecordRsp;
import zqx.rj.com.lovecar.fragment.BaseFragment;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * author：  HyZhan
 * created： 2018/10/17 17:03
 * desc：    TODO
 */

public class RecordFragment extends BaseFragment {

    private TextView mTvCount;
    private TextView mTvSug;
    private TextView mTvTotal;
    private MyHandler handler;
    private static final int LOAD_SUC = 1;

    private static class MyHandler extends Handler {

        WeakReference<Fragment> mWeakReference;

        MyHandler(Fragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            RecordFragment fragment = (RecordFragment) mWeakReference.get();
            switch (msg.what) {
                case LOAD_SUC:
                    fragment.setLineChartData((RecordRsp) msg.obj);
                    fragment.setTips((RecordRsp) msg.obj);
                    break;
            }
        }
    }

    private ValueLineChart recordChart;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_record;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        recordChart = view.findViewById(R.id.vlc_record);

        mTvCount = view.findViewById(R.id.tv_count);
        mTvSug = view.findViewById(R.id.tv_sug);
        mTvTotal = view.findViewById(R.id.tv_total);
    }

    @Override
    public void initData() {
        super.initData();

        handler = new MyHandler(this);

        final String id = ShareUtils.getString(getActivity(), "publish_id", "-1");
//        final String id = "1380";

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.get(getActivity(),
                        API.GET_SITUATION + "?id=" + id);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    RecordRsp recordRsp = UtilTools.jsonToBean(response.getData(), RecordRsp.class);

                    Message message = new Message();
                    message.what = LOAD_SUC;
                    message.obj = recordRsp;
                    handler.sendMessage(message);
                }
            }
        }).start();


    }

    public void setTips(RecordRsp recordRsp){

        if (recordRsp.getData() != null){
            mTvTotal.setText("座位总数：" + recordRsp.getData().getTotal());
            mTvSug.setText("建议：" + recordRsp.getData().getSug());
            mTvCount.setText("购票人数：" + recordRsp.getData().getBy_count());
        }
    }

    public void setLineChartData(RecordRsp recordRsp) {

        ValueLineSeries series = new ValueLineSeries();
        //设置折线图颜色
        series.setColor(0xFF56B7F1);

        Record record = recordRsp.getData();

        if (record.getPic() != null && record.getPic().size() > 0) {

            for (Line line : record.getPic()) {
                series.addPoint(new ValueLinePoint(line.getBy_time() + "日",
                        line.getBy_count()));
            }
        }

//        series.addPoint(new ValueLinePoint("", 0));
//        series.addPoint(new ValueLinePoint("10日", 5));
//        series.addPoint(new ValueLinePoint("11日", 12));
//        series.addPoint(new ValueLinePoint("12日", 6));
//        series.addPoint(new ValueLinePoint("13日", 10));
//        series.addPoint(new ValueLinePoint("14日", 8));
//        series.addPoint(new ValueLinePoint("15日", 4));
//        series.addPoint(new ValueLinePoint("16日", 9));
//        series.addPoint(new ValueLinePoint("17日", 10));
//        series.addPoint(new ValueLinePoint("18日", 13));
//        series.addPoint(new ValueLinePoint("20日", 6));
//        series.addPoint(new ValueLinePoint("", 0));


        recordChart.addSeries(series);
        recordChart.startAnimation();
    }
}

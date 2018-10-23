package zqx.rj.com.lovecar.chart;

import android.view.View;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.fragment.BaseFragment;

/**
 * author：  HyZhan
 * created： 2018/10/17 17:03
 * desc：    TODO
 */

public class RecordFragment extends BaseFragment{

    private ValueLineChart recordChart;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_record;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        recordChart = view.findViewById(R.id.vlc_record);
    }

    @Override
    public void initData() {
        super.initData();

        ValueLineSeries series = new ValueLineSeries();
        //设置折线图颜色
        series.setColor(0xFF56B7F1);

        series.addPoint(new ValueLinePoint("", 0));
        series.addPoint(new ValueLinePoint("10日", 5));
        series.addPoint(new ValueLinePoint("11日", 12));
        series.addPoint(new ValueLinePoint("12日", 6));
        series.addPoint(new ValueLinePoint("13日", 10));
        series.addPoint(new ValueLinePoint("14日", 8));
        series.addPoint(new ValueLinePoint("15日", 4));
        series.addPoint(new ValueLinePoint("16日", 9));
        series.addPoint(new ValueLinePoint("17日", 10));
        series.addPoint(new ValueLinePoint("18日", 13));
        series.addPoint(new ValueLinePoint("20日", 6));
        series.addPoint(new ValueLinePoint("", 0));


        recordChart.addSeries(series);
        recordChart.startAnimation();
    }
}

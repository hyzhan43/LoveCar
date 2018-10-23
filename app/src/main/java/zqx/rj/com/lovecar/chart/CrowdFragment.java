package zqx.rj.com.lovecar.chart;

import android.graphics.Color;
import android.view.View;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.fragment.BaseFragment;

/**
 * author：  HyZhan
 * created： 2018/10/17 16:53
 * desc：    TODO
 */

public class CrowdFragment extends BaseFragment{

    private PieChart mPieChart;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_crowd;
    }

    @Override
    public void initView(View view) {
        mPieChart = view.findViewById(R.id.pc_crowd);
    }

    @Override
    public void initData() {
        mPieChart.addPieSlice(
                new PieModel("大一", 36, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(
                new PieModel("大二", 25, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(
                new PieModel("大三", 14, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(
                new PieModel("大四", 9, Color.parseColor("#FED70E")));


        mPieChart.startAnimation();
    }
}

package zqx.rj.com.lovecar.chart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


import java.lang.ref.WeakReference;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.Crowd;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.Pic;
import zqx.rj.com.lovecar.entity.response.CrowdRsp;
import zqx.rj.com.lovecar.fragment.BaseFragment;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * author：  HyZhan
 * created： 2018/10/17 16:53
 * desc：    TODO
 */

public class CrowdFragment extends BaseFragment {

    private PieChart mPieChart;
    private MyHandler handler;
    private static final int LOAD_SUC = 1;
    private TextView mTvMost;
    private TextView mTvLeast;

    private static class MyHandler extends Handler {

        WeakReference<Fragment> mWeakReference;

        MyHandler(Fragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            CrowdFragment fragment = (CrowdFragment) mWeakReference.get();
            switch (msg.what) {
                case LOAD_SUC:
                    fragment.setPieChartData((CrowdRsp) msg.obj);
                    fragment.setTips((CrowdRsp) msg.obj);
                    break;
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_crowd;
    }

    @Override
    public void initView(View view) {
        mPieChart = view.findViewById(R.id.pc_crowd);

        mTvMost = view.findViewById(R.id.tv_most);
        mTvLeast = view.findViewById(R.id.tv_least);
    }

    @Override
    public void initData() {

        handler = new MyHandler(this);

        final String id = ShareUtils.getString(getActivity(), "publish_id", "-1");

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.get(getActivity(),
                        API.GET_CROWD + "?id=" + id);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    CrowdRsp crowdRsp = UtilTools.jsonToBean(response.getData(), CrowdRsp.class);

                    Message message = new Message();
                    message.what = LOAD_SUC;
                    message.obj = crowdRsp;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    public void setTips(CrowdRsp crowdRsp){

        if (crowdRsp.getData() != null){
            mTvMost.setText("主要人群：" + crowdRsp.getData().getMost());
            mTvLeast.setText("最少人群：" + crowdRsp.getData().getLeast());
        }
    }


    public void setPieChartData(CrowdRsp chartData) {

        Random random = new Random();

        Crowd crowd = chartData.getData();

        if (crowd.getPicList() != null && crowd.getPicList().size() > 0){
            for (Pic pic : chartData.getData().getPicList()) {

                int ranColor = 0xff000000 | random.nextInt(0x00ffffff);

                mPieChart.addPieSlice(
                        new PieModel(pic.getStu_role(),
                                Integer.parseInt(pic.getCount()),
                                ranColor));
            }

            mPieChart.startAnimation();
        }
    }
}

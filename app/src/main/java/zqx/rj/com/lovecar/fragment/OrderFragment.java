package zqx.rj.com.lovecar.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.OrderAdapter;
import zqx.rj.com.lovecar.utils.StaticClass;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.fragment
 * 文件名：  MainFragment
 * 创建者：  ZQX
 * 创建时间：2018/4/20 14:25
 * 描述：    订单
 */

public class OrderFragment extends BaseFragment {

    private ViewPager mFragmentPager;

    @Override
    public int getLayoutId() {
        return R.layout.frag_order;
    }

    @Override
    public void initView(View view) {
        TabLayout tab = view.findViewById(R.id.tl_order);
        mFragmentPager = view.findViewById(R.id.vp_content);
        tab.setupWithViewPager(mFragmentPager);
    }

    @Override
    public void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(OrderStateFragment.newInstance(StaticClass.ORDER_ALREADY));
        fragments.add(OrderStateFragment.newInstance(StaticClass.ORDER_FINISH));
        fragments.add(OrderStateFragment.newInstance(StaticClass.ORDER_UNPAID));
        fragments.add(OrderStateFragment.newInstance(StaticClass.ORDER_CANCEL));

        List<String> titles = new ArrayList<>();
        titles.add("已付款");
        titles.add("已完成");
        titles.add("未付款");
        titles.add("已取消");

        OrderAdapter adapter = new OrderAdapter(getChildFragmentManager(), fragments, titles);
        mFragmentPager.setAdapter(adapter);
    }
}

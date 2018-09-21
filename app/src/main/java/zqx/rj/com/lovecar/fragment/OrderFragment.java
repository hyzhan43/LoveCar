package zqx.rj.com.lovecar.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.OrderAdapter;
import zqx.rj.com.lovecar.utils.ScreenTools;
import zqx.rj.com.lovecar.utils.StaticClass;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.fragment
 * 文件名：  MainFragment
 * 创建者：  ZQX
 * 创建时间：2018/4/20 14:25
 * 描述：    订单
 */

public class OrderFragment extends Fragment{

    private TabLayout mTab;
    private ViewPager mFragmentpager;
    private OrderAdapter mAdapter;
    private List<Fragment> fragments;
    private List<String> titles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_order, null);
        // 屏幕适配
        ScreenTools.fragment(view);

        initView(view);
        initDatas();
        return view;
    }

    private void initDatas() {
        fragments = new ArrayList<>();

        fragments.add(OrderStateFragment.newInstance(StaticClass.ORDER_ALREADY));
        fragments.add(OrderStateFragment.newInstance(StaticClass.ORDER_FINISH));
        fragments.add(OrderStateFragment.newInstance(StaticClass.ORDER_UNPAID));
        fragments.add(OrderStateFragment.newInstance(StaticClass.ORDER_CANCEL));

        titles = new ArrayList<>();
        titles.add("已付款");
        titles.add("已完成");
        titles.add("未付款");
        titles.add("已取消");

        mTab.addTab(mTab.newTab().setText(titles.get(0)));
        mTab.addTab(mTab.newTab().setText(titles.get(1)));
        mTab.addTab(mTab.newTab().setText(titles.get(2)));
        mTab.addTab(mTab.newTab().setText(titles.get(3)));

        mAdapter = new OrderAdapter(getFragmentManager(), fragments, titles);
        mFragmentpager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mFragmentpager);
    }

    private void initView(View view) {

        mTab = view.findViewById(R.id.tl_order);
        mFragmentpager = view.findViewById(R.id.vp_content);
    }
}

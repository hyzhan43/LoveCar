package zqx.rj.com.lovecar.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.NewTabAdapter;

/**
 * author：  HyZhan
 * created： 2018/10/17 11:36
 * desc：    TODO
 */

public class NewsTabFragment extends BaseFragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public int getLayoutId() {
        return R.layout.frag_tab_news;
    }

    @Override
    public void initView(View view) {
        mTabLayout = view.findViewById(R.id.tb_title);
        mViewPager = view.findViewById(R.id.vp_content);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void initData() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new NewsFragment());
        fragmentList.add(new StatisticeFragment());

        mViewPager.setAdapter(new NewTabAdapter(getChildFragmentManager(), fragmentList));

        mTabLayout.getTabAt(0).setIcon(R.drawable.news_chat).setText("聊天");
        mTabLayout.getTabAt(1).setIcon(R.drawable.news_notice).setText("统计");
    }
}

package zqx.rj.com.lovecar.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.NewTabAdapter;
import zqx.rj.com.lovecar.fragment.PublishTicketFragment;
import zqx.rj.com.lovecar.fragment.StatisticeFragment;
import zqx.rj.com.lovecar.utils.ShareUtils;

/**
 * author：  HyZhan
 * created： 2018/11/1 16:27
 * desc：    TODO
 */

public class PublishDetailActivity extends BaseActivity {

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_publish_detail);
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

        Button mBtnBack = findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText("详情");


        mTabLayout = findViewById(R.id.tl_menu);
        mViewPager = findViewById(R.id.vp_content);

        mTabLayout.setupWithViewPager(mViewPager);

        String id = ShareUtils.getString(this, "publish_id", "-1");

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(PublishTicketFragment.getNewInstance(id));
        fragmentList.add(new StatisticeFragment());

        mViewPager.setAdapter(new NewTabAdapter(getSupportFragmentManager(), fragmentList));

        TabLayout.Tab tab1 = mTabLayout.getTabAt(0);
        TabLayout.Tab tab2 = mTabLayout.getTabAt(1);

        if (tab1 != null && tab2 != null) {
            tab1.setIcon(R.drawable.news_chat).setText("车票");
            tab2.setIcon(R.drawable.news_notice).setText("统计");
        }
    }
}

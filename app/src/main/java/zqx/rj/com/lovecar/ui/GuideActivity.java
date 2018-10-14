package zqx.rj.com.lovecar.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.GuideAdapter;
import zqx.rj.com.lovecar.entity.GuideInfo;

public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private ListView mGuideListView;
    private GuideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        initData();
    }

    private void initData() {

        List<GuideInfo> guideInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            guideInfoList.add(new GuideInfo("", "", ""));
        }

        mAdapter = new GuideAdapter(guideInfoList, this);
        mGuideListView.setAdapter(mAdapter);
    }

    private void initView() {
        TextView mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(getResources().getString(R.string.home_guide));

        Button mBtnBack = findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mGuideListView = findViewById(R.id.lv_home_guide);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}

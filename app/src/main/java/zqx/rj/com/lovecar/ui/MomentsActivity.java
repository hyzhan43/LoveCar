package zqx.rj.com.lovecar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.MomentsAdapter;
import zqx.rj.com.lovecar.entity.MomentInfo;

public class MomentsActivity extends BaseActivity implements View.OnClickListener {

    private ListView mMomentsListView;
    private MomentsAdapter mAdapter;

    // 图片资源
    private String[] mUrls = new String[]{
            "http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2024625579,507531332&fm=21&gp=0.jpg"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);

        initView();
        initData();
    }

    private void initView() {

        TextView mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(getResources().getString(R.string.ticket_moments));

        Button mBtnBack = findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mMomentsListView = findViewById(R.id.lv_moments);
    }

    private void initData() {

        List<MomentInfo> momentInfos = new ArrayList<>();
        List<ImageInfo> imageInfos = new ArrayList<>();
        for (int i = 0; i < mUrls.length; i++) {
            ImageInfo info = new ImageInfo();

            info.setThumbnailUrl(mUrls[i]);
            info.setBigImageUrl(mUrls[i]);

            imageInfos.add(info);
        }

        for (int i = 0; i < mUrls.length; i++) {
            momentInfos.add(new MomentInfo(i + "号", mUrls[i], "", "", imageInfos));
        }


        mAdapter = new MomentsAdapter(momentInfos, this);
        mMomentsListView.setAdapter(mAdapter);
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

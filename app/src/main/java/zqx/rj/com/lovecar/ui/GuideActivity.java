package zqx.rj.com.lovecar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.GuideAdapter;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.response.ArticleItem;
import zqx.rj.com.lovecar.entity.response.ArticleRsp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.UtilTools;

public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private ListView mGuideListView;
    private GuideAdapter mAdapter;
    private MyHandler mHandler;
    private List<ArticleItem> mArticleList;

    private static final int UPDATE_ARTICLE = 1;

    private static class MyHandler extends Handler {

        WeakReference<Activity> weakReference;

        MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GuideActivity activity = (GuideActivity) weakReference.get();
            switch (msg.what) {
                case UPDATE_ARTICLE:
                    activity.mAdapter.setNewData(activity.mArticleList);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        initData();
    }

    private void initView() {

        mHandler = new MyHandler(this);

        TextView mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(getResources().getString(R.string.home_guide));

        Button mBtnBack = findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mGuideListView = findViewById(R.id.lv_home_guide);

        mGuideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GuideActivity.this, WebViewActivity.class);
                intent.putExtra("url", mArticleList.get(position).getSource_uri());
                startActivity(intent);
            }
        });
    }


    private void initData() {

        mAdapter = new GuideAdapter(new ArrayList<ArticleItem>(), this);
        mGuideListView.setAdapter(mAdapter);

        new Thread() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.get(GuideActivity.this, API.GET_ARTICLE);

                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJson(response.getData());
                } else {
                    mHandler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();
    }

    private void parseJson(String data) {

        ArticleRsp articleRsp = UtilTools.jsonToBean(data, ArticleRsp.class);
        if (articleRsp.getCode() == 1) {
            mArticleList = articleRsp.getData().getItems();
            mHandler.sendEmptyMessage(UPDATE_ARTICLE);
        }
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

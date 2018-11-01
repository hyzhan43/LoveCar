package zqx.rj.com.lovecar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.NewRounteAdapter;
import zqx.rj.com.lovecar.entity.NewRounteData;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.response.NewRounteRsp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * author：  HyZhan
 * created： 2018/11/1 15:16
 * desc：    TODO
 */

public class MyPublishActivity extends BaseActivity {

    private ListView mListView;
    private NewRounteAdapter adapter;
    private List<NewRounteData> datas = new ArrayList<>();

    private MyHandler handler;
    private static final int LOAD_SUC = 1;

    public static class MyHandler extends Handler{

        WeakReference<Activity> mWeakReference;

        MyHandler(Activity activity){
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyPublishActivity publishActivity = (MyPublishActivity) mWeakReference.get();
            switch (msg.what){
                case LOAD_SUC:
                    publishActivity.adapter.updateDatas((List<NewRounteData>) msg.obj);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.frag_my_publish);
        super.onCreate(savedInstanceState);

        initView();
        initData();
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
        mTvTitle.setText("我发布的车票");

        mListView = findViewById(R.id.lv_tickets);
        adapter = new NewRounteAdapter(datas, this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyPublishActivity.this, PublishDetailActivity.class);

                ShareUtils.putString(MyPublishActivity.this,"publish_id", datas.get(position).getId());

                startActivity(intent);
            }
        });
    }


    private void initData() {

        handler = new MyHandler(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.get(MyPublishActivity.this, API.GET_PUBLISH);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    NewRounteRsp myPublishRsp = UtilTools.jsonToBean(response.getData(), NewRounteRsp.class);

                    Message message = new Message();
                    message.what = LOAD_SUC;
                    message.obj = myPublishRsp.getData();
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}

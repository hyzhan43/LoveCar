package zqx.rj.com.lovecar.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.LikeAdapter;
import zqx.rj.com.lovecar.entity.LikeData;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.response.BaseResponse;
import zqx.rj.com.lovecar.entity.response.LikeRsp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  LikeActivity
 * 创建者：  ZQX
 * 创建时间：2018/4/26 15:31
 * 描述：    收藏页面
 */

public class LikeActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private TextView back;

    private ListView lv_like;
    private LikeAdapter adapter;
    private List<LikeData> dataList;
    private List<LikeData> nowList;
    private MyHandler handler = new MyHandler();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case StaticClass.GET_LIKE_SUC:

                    dataList.addAll(nowList);
                    adapter.notifyDataSetChanged();
                    break;
                case StaticClass.GET_LIKE_FAIL:
                    T.show(LikeActivity.this, "获取收藏失败");
                    break;
                case StaticClass.NETWORK_FAIL:
                    T.show(LikeActivity.this, getString(R.string.network_fail));
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_like);
        super.onCreate(savedInstanceState);


        initData();
        initView();
    }

    private void initData() {
        dataList = new ArrayList<>();

        final String userId = UtilTools.getUserId(this);
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                RequestBody body = new FormBody.Builder()
                        .add("user_id", userId)
                        .build();

                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(LikeActivity.this, API.GET_LIKE, body);

                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJson(response.getData());
                } else {
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();
    }

    private void parseJson(String response) {
        LikeRsp likeRsp = UtilTools.jsonToBean(response, LikeRsp.class);
        if (likeRsp.getCode() == 1) {
            nowList = likeRsp.getData();
            handler.sendEmptyMessage(StaticClass.GET_LIKE_SUC);
        } else {
            Message message = new Message();
            message.obj = likeRsp.getMessage();
            message.what = StaticClass.GET_LIKE_FAIL;
            handler.sendMessage(message);
        }
    }

    private void parseJson2(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("code");
            if (result.equals("1")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");

                nowList = new Gson().fromJson(dataArray.toString(),
                        new TypeToken<List<LikeData>>() {
                        }.getType());

                handler.sendEmptyMessage(StaticClass.GET_LIKE_SUC);
            } else if (result.equals("0")) {
                String errorMessage = jsonObject.getString("message");
                Message message = new Message();
                message.obj = errorMessage;
                message.what = StaticClass.GET_LIKE_FAIL;
                handler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(StaticClass.GET_LIKE_FAIL);
        }
    }

    private void initView() {

        title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.my_like));
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        lv_like = findViewById(R.id.lv_like);
        adapter = new LikeAdapter(dataList, this);
        lv_like.setAdapter(adapter);
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

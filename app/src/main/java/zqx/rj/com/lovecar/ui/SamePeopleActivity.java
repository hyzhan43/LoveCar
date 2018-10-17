package zqx.rj.com.lovecar.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.SamePeopleAdapter;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.SamePeopleData;
import zqx.rj.com.lovecar.entity.response.BaseResponse;
import zqx.rj.com.lovecar.entity.response.SamePeopleRsp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  SamePeopleActivity
 * 创建者：  ZQX
 * 创建时间：2018/5/25 12:42
 * 描述：    同路人
 */

public class SamePeopleActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_same_people;
    private SamePeopleAdapter adapter;
    private List<SamePeopleData> samePeopleData;
    private List<SamePeopleData> newdataList;

    private TextView title;
    private Button back;


    private MyHandler handler = new MyHandler();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticClass.NETWORK_FAIL:
                    T.show(SamePeopleActivity.this, getString(R.string.network_fail));
                    break;
                case StaticClass.SAME_PEOPLE_SUC:
                    samePeopleData.addAll(newdataList);
                    adapter.notifyDataSetChanged();
                    break;
                case StaticClass.SAME_PEOPLE_FAIL:
                    T.show(SamePeopleActivity.this, "操作异常");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_same_people);
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        samePeopleData = new ArrayList<>();

        new Thread() {
            @Override
            public void run() {

                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.get(SamePeopleActivity.this,
                        API.GET_SAME_PEOPLE);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJson(response.getData());
                } else {
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }

            }
        }.start();
    }

    private void parseJson(String response) {
        SamePeopleRsp samePeopleRsp = UtilTools.jsonToBean(response, SamePeopleRsp.class);
        if (samePeopleRsp.getCode() == 1) {
            newdataList = samePeopleRsp.getData();
            handler.sendEmptyMessage(StaticClass.SAME_PEOPLE_SUC);
        } else {
            handler.sendEmptyMessage(StaticClass.SAME_PEOPLE_FAIL);
        }
    }

    private void parseJson2(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 1) {

                JSONArray dataArray = jsonObject.getJSONArray("data");
                newdataList = new Gson().fromJson(dataArray.toString(),
                        new TypeToken<List<SamePeopleData>>() {
                        }.getType());
                handler.sendEmptyMessage(StaticClass.SAME_PEOPLE_SUC);
            } else if (code == 0) {
                handler.sendEmptyMessage(StaticClass.SAME_PEOPLE_FAIL);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.same_people));
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        lv_same_people = findViewById(R.id.lv_same_people);
        adapter = new SamePeopleAdapter(this, samePeopleData);
        lv_same_people.setAdapter(adapter);
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

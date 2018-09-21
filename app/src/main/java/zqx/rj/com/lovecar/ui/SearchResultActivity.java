package zqx.rj.com.lovecar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.SearchResultAdapter;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.SearchResultData;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.L;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.view.CustomDialog;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  SearchResultActivity
 * 创建者：  ZQX
 * 创建时间：2018/4/25 14:31
 * 描述：    搜索结果页面
 */

public class SearchResultActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

//    private static final int NETWORK_FAIL = 1;
//    private static final int SEARCH_SUCCESS = 2;
//    private static final int SEARCH_FAIL = 3;
    private Button btn_back;
    private TextView title;
    private ListView lv_search_result;
    private List<SearchResultData> resultList = new ArrayList<>();
    private SearchResultAdapter adapter;
    // 上车地点
    private String start = "";
    // 下车地点
    private String end = "";
    // 时间
    private String time = "";

    private CustomDialog dialog;

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StaticClass.SEARCH_SUCCESS:
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    break;
                case StaticClass.SEARCH_FAIL:
                    dialog.dismiss();
                    break;
                case StaticClass.NETWORK_FAIL:
                    dialog.dismiss();
                    T.show(SearchResultActivity.this, getString(R.string.network_fail));
                    break;
            }
        }
    }

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_result);
        super.onCreate(savedInstanceState);

        handler = new MyHandler();
        initActionBar();
        initDatas();
        initView();
    }

    private void initDatas() {
        if (getIntent() != null){
            Intent intent = getIntent();
            start = intent.getStringExtra("startPlace");
            end = intent.getStringExtra("endPlace");
            time = intent.getStringExtra("time");
        }
        new Thread(){
            @Override
            public void run() {
                RequestBody body = new FormBody.Builder()
                        .add("departure_time", time)
                        .add("from_place", start)
                        .add("to_place", end)
                        .build();
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(API.SEARCH_TICKET, body);
                if (response.getCode() == OkhttpResponse.STATE_OK){
                    pasingJson(response.getData());
                }else {
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();
    }


    private void pasingJson(String datas) {
        try {
            JSONObject jsonObject = new JSONObject(datas);
            String result = jsonObject.getString("code");
            if (result.equals("1")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                Gson gson = new Gson();
                List<SearchResultData> dataList = gson.fromJson(jsonArray.toString(),
                        new TypeToken<List<SearchResultData>>(){}.getType());
                resultList.addAll(dataList);
                handler.sendEmptyMessageDelayed(StaticClass.SEARCH_SUCCESS,1000);
            } else if (result.equals("0")){
                handler.sendEmptyMessageDelayed(StaticClass.SEARCH_FAIL,1000);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        lv_search_result = findViewById(R.id.lv_search_result);
        adapter = new SearchResultAdapter(resultList, this);
        lv_search_result.setAdapter(adapter);
        View empty = findViewById(R.id.rl_empty);
        lv_search_result.setEmptyView(empty);
        lv_search_result.setOnItemClickListener(this);

        dialog = new CustomDialog(this,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                R.layout.datas_loding,
                R.style.pop_anim_style,
                Gravity.CENTER);
        // 屏幕外点击无效
        dialog.setCancelable(true);
        dialog.show();
    }

    private void initActionBar() {
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        title = findViewById(R.id.tv_title);
        title.setText("搜索结果");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, TicketActivity.class);
        intent.putExtra("ticketId", resultList.get(position).getPublish_id());
        L.d("result id = " + resultList.get(position).getPublish_id());
        startActivity(intent);
    }
}

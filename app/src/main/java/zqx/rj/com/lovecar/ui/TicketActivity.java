package zqx.rj.com.lovecar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcxtzhang.lib.AnimShopButton;
import com.mcxtzhang.lib.IOnAddDelListener;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.NewRounteData;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.response.BaseResponse;
import zqx.rj.com.lovecar.entity.response.NewRounteRsp;
import zqx.rj.com.lovecar.entity.response.OneTicketRsp;
import zqx.rj.com.lovecar.utils.L;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;
import zqx.rj.com.lovecar.view.CustomDialog;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  TicketActivity
 * 创建者：  ZQX
 * 创建时间：2018/4/21 16:49
 * 描述：    车票详情页面
 */

public class TicketActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private Button back;

    private TextView publisher;
    private TextView call_number;
    private TextView main_plate_number;
    private TextView main_surplus;
    private TextView main_start_place;
    private TextView main_end_place;
    private TextView order_price_name;
    private TextView main_time;

    // 车票 id
    private String id;
    // 车票信息
    private NewRounteData oneTicket;
    private MyHandler handler;
    // 进度条
    private CustomDialog dialog;
    // 选择数量
    private AnimShopButton mTicketCount;
    // 购买
    private Button btn_buy;
    // 车票数量
    private int count;
    // 车票总价格
    private TextView tv_all_money;
    // 车票价格
    private String price;

    private LinearLayout ll_chat_principal;
    private LinearLayout ll_chat_like;


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticClass.GET_TICKETS_SUCCESS:
                    RefreshDatas();
                    dialog.dismiss();
                    break;
                case StaticClass.GET_TICKETS_FAIL:
                    T.show(TicketActivity.this, "数据获取失败");
                    dialog.dismiss();
                    break;
                case StaticClass.NETWORK_FAIL:
                    T.show(TicketActivity.this, getResources().getString(R.string.network_fail));
                    dialog.dismiss();
                    break;
            }
        }
    }

    private void RefreshDatas() {
        publisher.setText(oneTicket.getPublisher());
        call_number.setText(oneTicket.getPhone());
        main_plate_number.setText(oneTicket.getPlate_number());
        main_surplus.setText(oneTicket.getSurplus());

        String[] startPlace = oneTicket.getFrom_place().split("-");
        if (startPlace.length == 2) {
            main_start_place.setText(startPlace[1]);
        } else {
            main_start_place.setText(oneTicket.getFrom_place());
        }

        String[] endPlace = oneTicket.getTo_place().split("-");
        if (endPlace.length == 2) {
            main_end_place.setText(endPlace[1]);
        } else {
            main_end_place.setText(oneTicket.getTo_place());
        }

        price = oneTicket.getPrice();
        order_price_name.setText(getString(R.string.price) + price);
        main_time.setText(oneTicket.getDeparture_time());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ticket);
        super.onCreate(savedInstanceState);


        handler = new MyHandler();
        // 初始化 actionBar
        initActionBar();

        initView();
        initDatas();
    }

    private void initDatas() {

        Intent intent = getIntent();
        id = intent.getStringExtra("ticketId");

        new Thread() {
            @Override
            public void run() {
                RequestBody body = new FormBody.Builder()
                        .add("publish_id", id)
                        .build();
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(TicketActivity.this,
                        API.GET_ONE_TICKET, body);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJsonWithGson(response.getData());
                } else {
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();
    }

    private void parseJsonWithGson(String data) {
        OneTicketRsp oneTicketRsp = UtilTools.jsonToBean(data, OneTicketRsp.class);
        if (oneTicketRsp.getCode() == 1) {
            oneTicket = oneTicketRsp.getData();
            handler.sendEmptyMessageDelayed(StaticClass.GET_TICKETS_SUCCESS, 2000);
        } else {
            handler.sendEmptyMessageDelayed(StaticClass.GET_TICKETS_FAIL, 1000);
        }
    }

    private void parseJsonWithGson2(String datas) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(datas);
            String code = jsonObject.getString("code");
            if (code.equals("1")) {
                String result = jsonObject.getString("data");
                Gson gson = new Gson();
                oneTicket = gson.fromJson(result, NewRounteData.class);
                handler.sendEmptyMessageDelayed(StaticClass.GET_TICKETS_SUCCESS, 2000);
            } else if (code.equals("0")) {
                L.e("车票详情数据加载失败");
                handler.sendEmptyMessageDelayed(StaticClass.GET_TICKETS_FAIL, 1000);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private IOnAddDelListener countListener = new IOnAddDelListener() {
        //添加成功事件
        @Override
        public void onAddSuccess(int i) {
            count = i;
            tv_all_money.setText("￥" + i * Integer.parseInt(price));
        }

        //添加成功事件
        @Override
        public void onAddFailed(int i, FailType failType) {
            T.show(TicketActivity.this, "添加失败");
        }

        @Override
        public void onDelSuccess(int i) {
            tv_all_money.setText("￥" + i * Integer.parseInt(price));
        }

        @Override
        public void onDelFaild(int i, FailType failType) {
            T.show(TicketActivity.this, "减少失败");
        }
    };

    private void initView() {

        dialog = new CustomDialog(this,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_loding,
                R.style.Theme_dialog, Gravity.CENTER);
        // 屏幕外点击无效
        dialog.setCancelable(true);
        dialog.show();

        order_price_name = findViewById(R.id.order_price_name);
        publisher = findViewById(R.id.publisher);
        call_number = findViewById(R.id.call_number);
        main_plate_number = findViewById(R.id.main_plate_number);
        main_surplus = findViewById(R.id.main_surplus);
        main_start_place = findViewById(R.id.main_start_place);
        main_end_place = findViewById(R.id.main_end_place);
        main_time = findViewById(R.id.main_time);

        mTicketCount = findViewById(R.id.ab_count);
        mTicketCount.setOnAddDelListener(countListener);

        btn_buy = findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(this);

        tv_all_money = findViewById(R.id.tv_all_money);

        ll_chat_principal = findViewById(R.id.ll_chat_principal);
        ll_chat_principal.setOnClickListener(this);
        ll_chat_like = findViewById(R.id.ll_chat_like);
        ll_chat_like.setOnClickListener(this);
    }

    private void initActionBar() {

        title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.ticket_title));
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_buy:
                if (count != 0) {
                    BuyTickets();
                    T.show(this, "购票成功");
                } else {
                    T.show(this, "请选择购票数");
                }
                break;
            case R.id.ll_chat_principal:
                Intent intent = new Intent(TicketActivity.this, ChatMsgActivity.class);
                intent.putExtra("phone", call_number.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_chat_like:
                T.show(this, "喜欢");
                break;
        }
    }

    private void BuyTickets() {

    }
}

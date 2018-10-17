package zqx.rj.com.lovecar.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.OrderDetailData;
import zqx.rj.com.lovecar.entity.response.BaseResponse;
import zqx.rj.com.lovecar.entity.response.OrderDetailRsp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;
import zqx.rj.com.lovecar.view.CustomDialog;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  OrderDetailActivity
 * 创建者：  ZQX
 * 创建时间：2018/5/11 18:02
 * 描述：    订单详情页面
 */

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    //    private static final int NETWORK_FAIL = 2;
//    private static final int ORDER_SUCCESS = 3;
    private TextView title;
    private Button back;

    private TextView order_state;
    private TextView oder_number;
    private TextView order_create_time;

    private TextView tv_contact;
    private TextView tv_phone;
    private TextView tv_id;
    private TextView tv_price;

    private TextView order_start_place;
    private TextView order_end_place;
    private TextView order_time;
    private TextView order_count;

    private String orderNumber;
    private String orderState;

    private CustomDialog dialog;

    private MyHandler handler = new MyHandler();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticClass.ORDER_SUCCESS:

                    OrderDetailData orderData = (OrderDetailData) msg.obj;
                    updateData(orderData);
                    dialog.dismiss();
                    break;
                case StaticClass.NETWORK_FAIL:
                    dialog.dismiss();
                    T.show(OrderDetailActivity.this, getString(R.string.network_fail));
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_detail);
        super.onCreate(savedInstanceState);

        initView();
        initDialog();
        initData();
    }

    private void initDialog() {
        dialog = new CustomDialog(this,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_loding,
                R.style.Theme_dialog,
                Gravity.CENTER);
        dialog.setCancelable(false);
    }


    private void initData() {
        dialog.show();
        if (getIntent() != null) {
            orderNumber = getIntent().getStringExtra("orderNumber");
            orderState = getIntent().getStringExtra("state");
        }

        new Thread() {
            @Override
            public void run() {
                RequestBody body = new FormBody.Builder()
                        .add("order_number", orderNumber)
                        .build();

                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(OrderDetailActivity.this,
                        API.ORDER_DETAIL, body);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJson(response.getData());
                } else {
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();

    }

    private void parseJson(String responseData) {
         OrderDetailRsp orderDetailRsp = UtilTools.jsonToBean(responseData, OrderDetailRsp.class);

        if (orderDetailRsp.getCode() == 1){
            Message message = new Message();
            message.obj = orderDetailRsp.getData();
            message.what = StaticClass.ORDER_SUCCESS;
            handler.sendMessage(message);
        }else {
            handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
        }
    }

    private void parseJson2(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            int code = jsonObject.getInt("code");
            if (code == 1) {

                jsonObject = jsonObject.getJSONObject("data");

                OrderDetailData orderDetailData = new Gson().fromJson(jsonObject.toString(),
                        OrderDetailData.class);

                Message message = new Message();
                message.obj = orderDetailData;
                message.what = StaticClass.ORDER_SUCCESS;
                handler.sendMessage(message);

            } else if (code == 0) {
                handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateData(OrderDetailData orderData) {

        order_state.setText(UtilTools.getState(orderState));
        oder_number.setText(orderData.getOrder_number());
        order_create_time.setText(orderData.getCreate_time());

        tv_contact.setText(orderData.getName());
        tv_phone.setText(orderData.getIphone());
        tv_id.setText(orderData.getIdentity_card());
        tv_price.setText(orderData.getPrice());

        order_start_place.setText(orderData.getFrom_place());
        order_end_place.setText(orderData.getTo_place());
        order_time.setText(orderData.getDeparture_time());
        order_count.setText("票数：" + orderData.getPoll());
    }

    private void initView() {
        title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.order_detail));

        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        order_state = findViewById(R.id.order_state);
        order_create_time = findViewById(R.id.order_create_time);
        oder_number = findViewById(R.id.oder_number);
        tv_contact = findViewById(R.id.tv_contact);
        tv_phone = findViewById(R.id.tv_phone);
        tv_id = findViewById(R.id.tv_id);
        tv_price = findViewById(R.id.order_price);

        order_start_place = findViewById(R.id.order_start_place);
        order_end_place = findViewById(R.id.order_end_place);
        order_time = findViewById(R.id.order_time);
        order_count = findViewById(R.id.order_count);
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

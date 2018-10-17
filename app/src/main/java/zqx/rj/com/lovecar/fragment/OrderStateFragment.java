package zqx.rj.com.lovecar.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import zqx.rj.com.lovecar.adapter.OrderDetailAdapter;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.OrderData;
import zqx.rj.com.lovecar.entity.response.OrderRsp;
import zqx.rj.com.lovecar.ui.OrderDetailActivity;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;
import zqx.rj.com.lovecar.view.CustomDialog;

/**
 *
 */
public class OrderStateFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView list_already;
    private List<OrderData> tickets = new ArrayList<>();

    private OrderDetailAdapter adapter;
    private SwipeRefreshLayout swipelayout;
    private MyHandler handler = new MyHandler();

    private CustomDialog dialog;
    private boolean isInit = true;//初始化是否完成

    private String state;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticClass.GET_ORDER_SUCCESS:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    adapter.notifyDataSetChanged();
                    if (swipelayout.isRefreshing()) {
                        swipelayout.setRefreshing(false);
                        T.show(getActivity(), getActivity().getResources().getString(R.string.refresh_finish));
                    }
                    break;
                case StaticClass.GET_ORDER_EMPTY:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (swipelayout.isRefreshing()) {
                        swipelayout.setRefreshing(false);
                    }
                    T.show(getActivity(), getActivity().getResources()
                            .getString(R.string.not_tickets));
                    break;
                case StaticClass.NETWORK_FAIL:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    T.show(getActivity(), getActivity().getResources()
                            .getString(R.string.network_fail));
                    break;
                case StaticClass.DIALOG_SHOW:
                    initDialog();
                    dialog.show();
                    break;
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_state;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = getArguments().getString("state");
    }

    // 由于不能使用默认， 所以 新建一个 newInstance 方法
    public static OrderStateFragment newInstance(String state) {
        OrderStateFragment orderStateFragment = new OrderStateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state", state);
        orderStateFragment.setArguments(bundle);
        return orderStateFragment;
    }

    public void initDialog() {
        dialog = new CustomDialog(getActivity(),
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_loding,
                R.style.Theme_dialog,
                Gravity.CENTER);
        dialog.setCancelable(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isInit) {
            initDatas();
            isInit = false;
        }
    }

    private void initDatas() {

        new Thread() {
            @Override
            public void run() {
                // 初始化 并显示dialog
                handler.sendEmptyMessage(StaticClass.DIALOG_SHOW);
                RequestBody requestbody = new FormBody.Builder()
                        .add("user_id", UtilTools.getUserId(getActivity()))
                        .add("order_status", state)
                        .build();
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(getContext(), API.GET_ORDER, requestbody);
                // 若是 正常响应 200
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    // 解析 Json 数据
                    parseJson(response.getData());
                } else {
                    // 否则网络异常
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();
    }

    private void parseJson(String responseData) {
        OrderRsp orderRsp = UtilTools.jsonToBean(responseData, OrderRsp.class);

        if (orderRsp.getCode() == 1) {
            tickets.addAll(orderRsp.getData());
            if (tickets.size() > 0) {
                handler.sendEmptyMessage(StaticClass.GET_ORDER_SUCCESS);
            }
        } else {
            handler.sendEmptyMessage(StaticClass.GET_ORDER_EMPTY);
        }


    }

    private void parseJson2(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            int code = jsonObject.getInt("code");
            if (code == 1) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                List<OrderData> orderData = gson.fromJson(jsonArray.toString(),
                        new TypeToken<List<OrderData>>() {
                        }.getType());

                tickets.addAll(orderData);
                if (tickets.size() > 0) {
                    handler.sendEmptyMessage(StaticClass.GET_ORDER_SUCCESS);
                }
            } else if (code == 0) {
                handler.sendEmptyMessage(StaticClass.GET_ORDER_EMPTY);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView(View view) {

        list_already = view.findViewById(R.id.list_already);
        adapter = new OrderDetailAdapter(getActivity(), tickets);
        list_already.setAdapter(adapter);
        list_already.setOnItemClickListener(this);
        swipelayout = view.findViewById(R.id.swipelayout);
        swipelayout.setColorSchemeColors(getResources().getColor(R.color.color_main),
                getResources().getColor(R.color.color_white));
        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipelayout.isRefreshing()) {
                    tickets.clear();
                    initDatas();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderData data = tickets.get(position);
        String orderNumber = data.getOrder_number();
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("orderNumber", orderNumber);
        intent.putExtra("state", state);
        startActivity(intent);
    }
}

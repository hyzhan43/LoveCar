package zqx.rj.com.lovecar.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.TicketAdapter;
import zqx.rj.com.lovecar.entity.IconName;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.Ticket;
import zqx.rj.com.lovecar.entity.response.PublishTicketRsp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * author：  HyZhan
 * created： 2018/11/1 17:58
 * desc：    TODO
 */

public class PublishTicketFragment extends BaseFragment {

    private static final int LOAD_SUC = 1;
    private MyHandler handler;
    private List<IconName> dataList = new ArrayList<>();
    private TicketAdapter mTicketAdapter;

    public static PublishTicketFragment getNewInstance(String id) {

        Bundle bundle = new Bundle();
        bundle.putString("publish_id", id);

        PublishTicketFragment fragment = new PublishTicketFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public static class MyHandler extends Handler {

        WeakReference<Fragment> mWeakReference;

        MyHandler(Fragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            PublishTicketFragment fragment = (PublishTicketFragment) mWeakReference.get();
            switch (msg.what) {
                case LOAD_SUC:

                    fragment.setTitleData(fragment.getView(), (Ticket) msg.obj);
                    break;
            }
        }
    }

    public void setTitleData(View view, Ticket ticket) {

        dataList.add(new IconName(R.drawable.ic_about_us, "负责人",ticket.getUsername()));
        dataList.add(new IconName(R.drawable.ic_phone2, "手机号",ticket.getPhone()));
        dataList.add(new IconName(R.drawable.ic_plate, "车牌号",ticket.getPlate_number()));
        dataList.add(new IconName(R.drawable.ic_start_place, "上车地点",ticket.getFrom_place()));
        dataList.add(new IconName(R.drawable.ic_start_place, "下车地点",ticket.getTo_place()));
        dataList.add(new IconName(R.drawable.ic_record, "票余",ticket.getSurplus()));
        dataList.add(new IconName(R.drawable.ic_calendar, "时间",ticket.getDeparture_time()));
        mTicketAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_publish_detail;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        handler = new MyHandler(this);

        ListView listView = view.findViewById(R.id.lv_tickets);
        mTicketAdapter = new TicketAdapter(getActivity(), dataList);
        listView.setAdapter(mTicketAdapter);
    }

    @Override
    public void initData() {
        super.initData();


        final String id = getArguments().getString("publish_id", "-1");

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();
                RequestBody body = new FormBody.Builder()
                        .add("publish_id", id)
                        .build();

                OkhttpResponse response = okHttp.post(getActivity(), API.GET_AUTH_TICKET, body);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    PublishTicketRsp publishTicketRsp = UtilTools
                            .jsonToBean(response.getData(), PublishTicketRsp.class);

                    Message message = new Message();
                    message.what = LOAD_SUC;
                    message.obj = publishTicketRsp.getData();
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

}

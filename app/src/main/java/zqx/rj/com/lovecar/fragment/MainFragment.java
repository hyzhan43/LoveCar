package zqx.rj.com.lovecar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.samthompson.bubbleactions.BubbleActions;
import me.samthompson.bubbleactions.MenuCallback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.Observer.ObserverManager;
import zqx.rj.com.lovecar.Observer.UpdateNewTicketsListener;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.NewRounteAdapter;
import zqx.rj.com.lovecar.entity.NewRounteData;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.response.NewRounteRsp;
import zqx.rj.com.lovecar.ui.GuideActivity;
import zqx.rj.com.lovecar.ui.LikeActivity;
import zqx.rj.com.lovecar.ui.MomentsActivity;
import zqx.rj.com.lovecar.ui.SamePeopleActivity;
import zqx.rj.com.lovecar.ui.SearchTicketsActivity;
import zqx.rj.com.lovecar.ui.TicketActivity;
import zqx.rj.com.lovecar.ui.TravelActivity;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ScreenTools;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.fragment
 * 文件名：  MainFragment
 * 创建者：  ZQX
 * 创建时间：2018/4/20 14:25
 * 描述：    首页
 */

public class MainFragment extends BaseFragment implements AbsListView.OnScrollListener,
        View.OnClickListener, AdapterView.OnItemLongClickListener,
        UpdateNewTicketsListener {

    // 轮播图
    private Banner mBanner;
    private List<String> images;
    private static NewRounteAdapter adapter;
    // 最新路线
    private ListView lv_new_rounte;

    // 页数
    private int page = 1;

    // 最新路线数据
    private List<NewRounteData> newRounteDataList = new ArrayList<>();
    // 最新数据
    private List<NewRounteData> tempList = new ArrayList<>();

    // 搜索框
    private EditText et_search_ticket;
    // 同路人
    private LinearLayout ll_same_people;

    // 顶部 View
    private View headView;
    // 底部 View
    private View footView;

    private int last_index;
    private int total_index;
    public boolean isLoading = false;//表示是否正处于加载状态
    public boolean isEnd = false;

    private static final int UPLOAD_NEW_TICKETS = 1;
    private static Handler handler;


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.et_search_ticket:
                intent = new Intent(getActivity(), SearchTicketsActivity.class);
                break;
            case R.id.ll_same_people:
                intent = new Intent(getActivity(), SamePeopleActivity.class);
                break;
            case R.id.ll_tickets_moments:
                intent = new Intent(getActivity(), MomentsActivity.class);
                break;
            case R.id.ll_home_guide:
                intent = new Intent(getActivity(), GuideActivity.class);
                break;
            case R.id.ll_travel:
                intent = new Intent(getActivity(), TravelActivity.class);
                T.show(getContext(), "该功能暂未实现");
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void refresh() {

        handler.sendEmptyMessage(UPLOAD_NEW_TICKETS);
    }

    public static class MyHandler extends Handler {

        WeakReference<Fragment> mWeakReference;

        public MyHandler(Fragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {

            MainFragment fragment = (MainFragment) mWeakReference.get();

            switch (msg.what) {
                case StaticClass.LOAD_FINISH:
                    fragment.newRounteDataList.addAll(fragment.tempList);
                    adapter.notifyDataSetChanged();
                    fragment.tempList.clear();
                    fragment.footView.setVisibility(View.GONE);
                    break;
                case StaticClass.REFRESH_FINISH:
                    if (!fragment.isEnd) {
                        fragment.isEnd = true;
                        T.show(fragment.getActivity(), fragment.getString(R.string.refresh_finish));
                        fragment.lv_new_rounte.removeFooterView(fragment.footView);
                    }
                    break;
                case StaticClass.NETWORK_FAIL:
                    T.show(fragment.getActivity(), fragment.getString(R.string.network_fail));
                    break;
                case UPLOAD_NEW_TICKETS:
                    fragment.newRounteDataList.clear();
                    fragment.initNewRounteDatas(1);
                    break;
            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initView(View view) {
        handler = new MyHandler(this);


        // 加载 轮播图图片
        initImages();
        // 获取最新路线资源  默认加载 5条数据  第一页
        initNewRounteDatas(page);

        initListView(view);
        // 设置轮播图属性
        initBanner(headView);

        ObserverManager.Holder.instance.register(this);
    }

    public void initNewRounteDatas(final int page) {

        new Thread() {
            @Override
            public void run() {
                RequestBody body = new FormBody.Builder()
                        .add("ps", "5")
                        .add("p", String.valueOf(page))
                        .build();
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(getContext(), API.NEW_ROUNTE, body);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJsonWithGson(response.getData());
                } else {
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();
    }

    private void parseJsonWithGson(String data) {
        NewRounteRsp newRounteRsp = UtilTools.jsonToBean(data, NewRounteRsp.class);

        if (newRounteRsp.getCode() == 1) {
            tempList.addAll(newRounteRsp.getData());
            handler.sendEmptyMessageDelayed(StaticClass.LOAD_FINISH, 500);
        } else {
            handler.sendEmptyMessageDelayed(StaticClass.NETWORK_FAIL, 1500);
        }
    }

    private void parseJsonWithGson2(String data) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            String code = jsonObject.getString("code");
            if (code.equals("1")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                Gson gson = new Gson();
                //  Gson 映射 NewRounteData
                List<NewRounteData> newList = gson.fromJson(jsonArray.toString(),
                        new TypeToken<List<NewRounteData>>() {
                        }.getType());

                tempList.addAll(newList);
                handler.sendEmptyMessageDelayed(StaticClass.LOAD_FINISH, 500);
            } else if (code.equals("0")) {
                handler.sendEmptyMessageDelayed(StaticClass.NETWORK_FAIL, 1500);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initImages() {
        images = new ArrayList<>();
        images.add(API.BANNER_PIC1);
        images.add(API.BANNER_PIC2);
        images.add(API.BANNER_PIC3);
        images.add(API.BANNER_PIC4);
    }

    private void initListView(View view) {

        lv_new_rounte = view.findViewById(R.id.lv_new_rounte);
        adapter = new NewRounteAdapter(newRounteDataList, getActivity());

        initHeader();

        footView = LinearLayout.inflate(getActivity(), R.layout.main_foot, null);
        footView.setVisibility(View.GONE);
        ScreenTools.fragment(footView);

        lv_new_rounte.addFooterView(footView);

        lv_new_rounte.setAdapter(adapter);
        lv_new_rounte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TicketActivity.class);
                String tempId = newRounteDataList.get(position - 1).getId();
                intent.putExtra("ticketId", tempId);
                startActivity(intent);
            }
        });

        lv_new_rounte.setOnItemLongClickListener(this);
        lv_new_rounte.setOnScrollListener(this);
    }

    private void initHeader() {
        // 给 ListView 设置 顶部布局
        headView = LinearLayout.inflate(getActivity(), R.layout.main_head, null);
        // 屏幕适配
        ScreenTools.fragment(headView);
        lv_new_rounte.addHeaderView(headView);
        // 搜索框
        et_search_ticket = headView.findViewById(R.id.et_search_ticket);
        et_search_ticket.setFocusable(false);
        et_search_ticket.setOnClickListener(this);

        // 同路人
        ll_same_people = headView.findViewById(R.id.ll_same_people);
        ll_same_people.setOnClickListener(this);

        // 回家指南
        LinearLayout mGuide = headView.findViewById(R.id.ll_home_guide);
        mGuide.setOnClickListener(this);

        // 票圈
        LinearLayout mMoments = headView.findViewById(R.id.ll_tickets_moments);
        mMoments.setOnClickListener(this);

        // 轻旅游
        LinearLayout mTravel = headView.findViewById(R.id.ll_travel);
        mTravel.setOnClickListener(this);
    }

    // 长按事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        BubbleActions.on(view).fromMenu(R.menu.long_click_menu, new MenuCallback() {
            @Override
            public void doAction(int itemId) {
                switch (itemId) {
                    case R.id.action_good:
                        T.show(getActivity(), "good");
                        break;
                    case R.id.action_love:
                        Intent intent = new Intent(getActivity(), LikeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_share:
                        T.show(getActivity(), "share");
                        break;
                }
            }
        }).show();

        return true;
    }

    // ListView 滑动监听
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
        if (last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)) {
            if (!isLoading) {
                // 不处于加载状态的话 对其进行加载
                isLoading = true;
                footView.setVisibility(View.VISIBLE);
                onLoadDatas();
            }
        }
    }

    // ListView 滚动事件
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 获取当前显示的最后一个索引
        last_index = firstVisibleItem + visibleItemCount;
        total_index = totalItemCount;
    }

    // 刷新加载数据
    private void onLoadDatas() {
        initNewRounteDatas(++page);
        // 设置正在刷新标志位
        isLoading = false;
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    private void initBanner(View view) {

        mBanner = view.findViewById(R.id.banner);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());

        //设置图片集合
        mBanner.setImages(images);

        // 自动播放
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(5000);

        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }
}

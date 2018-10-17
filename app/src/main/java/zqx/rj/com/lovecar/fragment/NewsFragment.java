package zqx.rj.com.lovecar.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.NewsAdapter;
import zqx.rj.com.lovecar.ui.ChatMsgActivity;
import zqx.rj.com.lovecar.ui.StatisticeActivity;
import zqx.rj.com.lovecar.utils.ScreenTools;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.StaticClass;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.fragment
 * 文件名：  MainFragment
 * 创建者：  ZQX
 * 创建时间：2018/4/20 14:25
 * 描述：    消息
 */

public class NewsFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private SwipeMenuListView lv_chat;
    private List<Conversation> conversationList;
    private NewsAdapter adapter;
    private MyHandler handler;

    private SwipeMenuCreator creator;
    private SwipeMenuItem deleteItem;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticClass.RECEIVE_SUCCESS:
                    // 更新会话列表
                    updateDatas();
                    break;
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_news;
    }

    @Override
    public void initView(View view) {
        // 注册监听
        JMessageClient.registerEventReceiver(this);

        handler = new MyHandler();

    }

    @Override
    public void initData() {
        conversationList = new ArrayList<>();
        List<Conversation> conversations = JMessageClient.getConversationList();
        if (conversations != null) {
            conversationList.addAll(conversations);
        }

        findView(getView());
    }

    private void findView(View view) {

        lv_chat = view.findViewById(R.id.lv_chat);
        adapter = new NewsAdapter(conversationList, getActivity());
        lv_chat.setAdapter(adapter);
        lv_chat.setOnItemClickListener(this);
        createMenu();
        lv_chat.setMenuCreator(creator);
        // 右边滑出
        lv_chat.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lv_chat.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                // 获取当前会话
                Conversation conversation = conversationList.get(position);
                switch (index) {
                    case StaticClass.DELETE_CODE:
                        deletedConversation(conversation);
                        break;
                }
                return false;
            }
        });
    }

    // 删除单个会话
    private void deletedConversation(Conversation conversation) {
        // 获取当前会话的 username
        UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
        String username = userInfo.getUserName();
        // 根据 username 删除会话
        JMessageClient.deleteSingleConversation(username);
        updateDatas();
    }

    // 创建侧滑 item
    private void createMenu() {
        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 添加 删除
                deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(180);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(14);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
    }

    // chat Item 点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ChatMsgActivity.class);
        Conversation conversation = conversationList.get(position);

        File iconFile = conversation.getAvatarFile();
        String path = iconFile.getPath();
        ShareUtils.putString(getActivity(), "targetIconPath", path);

        // 重置单个会话未读消息数
        boolean reset = conversation.resetUnreadCount();
        if (reset) {
            adapter.notifyDataSetChanged();
        }
        intent.putExtra("position", position);

        startActivity(intent);
    }


    // 收到消息
    public void onEvent(MessageEvent event) {
        handler.sendEmptyMessage(StaticClass.RECEIVE_SUCCESS);
    }

    // 更新 会话列表
    private void updateDatas() {
        conversationList.clear();
        List<Conversation> conversations = JMessageClient.getConversationList();
        if (conversations != null) {
            conversationList.addAll(conversations);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 显示或者隐藏  更新 会话列表
        updateDatas();
    }
}

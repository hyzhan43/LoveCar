package zqx.rj.com.lovecar.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.ChatAdapter;
import zqx.rj.com.lovecar.entity.ChatListData;
import zqx.rj.com.lovecar.utils.L;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  ChatMsgActivity
 * 创建者：  ZQX
 * 创建时间：2018/5/4 14:51
 * 描述：    聊天页面
 */

public class ChatMsgActivity extends BaseActivity implements View.OnClickListener {

    private ListView chat_listview;
    private List<ChatListData> dataList = new ArrayList<>();
    private ChatAdapter adapter;
    private EditText et_input;
    private Button btn_send;

    private Button btn_back;
    private TextView tv_title;

    // 上拉刷新
    private SwipeRefreshLayout swipeLayout;
    private MyHandler handler;

    // 会话的联系人
    private String phone;
    // 负责人
    private String account;
    // 会话
    private Conversation conversation;
    // 所有会话
    private List<Conversation> conversationList;
    // 当前会话的所有消息记录
    private List<Message> allMessage;
    private int nowMessageCount;
    private List<Message> nowMessage;
    // 默认加载消息数
    private int count = 5;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case StaticClass.RECEIVE_SUCCESS:

                    Message message = (Message) msg.obj;
                    TextContent textContent = (TextContent) message.getContent();
                    String text = textContent.getText();
//                    long time = message.getCreateTime();
                    addLeftItem(text);
                    break;
                case StaticClass.UPDATE_SUCCESS:
                    if (nowMessageCount - 5 < 0){
                        // 如果消息数不足  5条 就按照剩下获取
                        count = nowMessageCount;
                        nowMessageCount = 0;
                    } else {
                        nowMessageCount = nowMessageCount - 5;
                    }

                    updateOldMessage(false, true, nowMessageCount, count);
                    // 更新
                    adapter.notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);

        init();
        updateOldMessage(true, false, nowMessageCount, count);
        initView();
    }

    // 获取 历史记录
    private void updateOldMessage(boolean isClear, boolean start, int begin, int count) {

        if (count < 0){
            return;
        }

        // 默认 获取 5条历史记录
        nowMessage = conversation.getMessagesFromOldest(begin, count);

        if (nowMessage.size() > 0) {
            if (isClear){
                dataList.clear();
            }
            int index = 0;
            for (Message msg : nowMessage) {
                ChatListData data = new ChatListData();
                TextContent content = (TextContent) msg.getContent();
                data.setText(content.getText());
                MessageDirect direct = msg.getDirect();
                // 如果是 send
                if (MessageDirect.send == direct) {
                    data.setType(ChatAdapter.VALUE_RIGHT_TEXT);
                } else if (MessageDirect.receive == direct) {
                    data.setType(ChatAdapter.VALUE_LEFT_TEXT);
                }
                if (start){
                    // list 顶部添加
                    dataList.add(index++, data);
                }else {
                    // 默认 添加底部
                    dataList.add(data);
                }
            }
        }
    }

    private void init() {
        handler = new MyHandler();

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        if (TextUtils.isEmpty(phone)) {
            conversationList = JMessageClient.getConversationList();
            int position = intent.getIntExtra("position", 0);
            conversation = conversationList.get(position);
            account = conversation.getTitle();
        } else {
            phone = intent.getStringExtra("phone");
            conversation = Conversation.createSingleConversation(phone);
            account = conversation.getTitle();
        }

        // 获取会话的历史记录
        allMessage = conversation.getAllMessage();
        nowMessageCount = allMessage.size() - 5;
    }

    private void initView() {

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(account);

        chat_listview = findViewById(R.id.chat_listview);
        adapter = new ChatAdapter(dataList, this);
        chat_listview.setAdapter(adapter);
        // 滚到底部
        chat_listview.setSelection(dataList.size());

        et_input = findViewById(R.id.et_input);
        et_input.setOnClickListener(this);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        swipeLayout = findViewById(R.id.swipeLayout);
        swipeLayout.setColorSchemeColors(Color.WHITE, getResources().getColor(R.color.color_main));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeLayout.isRefreshing()) {
                    // !!! 一定要在 handler 更新数据
                    // 不然会报错
                    handler.sendEmptyMessageDelayed(StaticClass.UPDATE_SUCCESS, 2000);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_input:
                et_input.setCursorVisible(true);
                break;
            case R.id.btn_send:
                btn_send.setEnabled(false);
                // 创建聊天会话
                createConversation();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    // 创建会话
    private void createConversation() {
        final String msg = et_input.getText().toString();
        if (TextUtils.isEmpty(msg)){
            btn_send.setEnabled(true);
            return ;
        }

        Message message = conversation.createSendMessage(new TextContent(msg));
        // 消息发送结果监听
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseDesc) {
                if (responseCode == 0) {
                    // 消息发送成功
                    addRightItem(msg);
                    et_input.setText("");
                    btn_send.setEnabled(true);
                } else {
                    // 消息发送失败
                    T.show(ChatMsgActivity.this, getResources().getString(R.string.send_fail));
                    L.d("send fail = " + responseDesc);
                }
            }
        });

        //使用默认控制参数发送消息
        MessageSendingOptions options = new MessageSendingOptions();
        // 设置保留离线
        options.setRetainOffline(false);
        JMessageClient.sendMessage(message);
    }

    // 添加左边文本
    private void addLeftItem(String text) {
        ChatListData data = new ChatListData();
        data.setType(ChatAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        dataList.add(data);

        // 刷新
        adapter.notifyDataSetChanged();

        // 滚到底部
        chat_listview.setSelection(chat_listview.getBottom());
    }

    // 添加右边文本
    private void addRightItem(String text) {
        ChatListData data = new ChatListData();
        data.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        dataList.add(data);
        // 刷新
        adapter.notifyDataSetChanged();

        // 滚到底部
        chat_listview.setSelection(chat_listview.getBottom());
    }

    // 收到消息
    public void onEvent(MessageEvent event) {
        final Message message = event.getMessage();
        switch (message.getContentType()) {
            case text:
                //处理文字消息
                android.os.Message msg = new android.os.Message();
                msg.obj = message;
                msg.what = StaticClass.RECEIVE_SUCCESS;
                handler.sendMessage(msg);
                break;
        }
    }


}

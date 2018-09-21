package zqx.rj.com.lovecar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.utils.L;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.adapter
 * 文件名：  NewsAdapter
 * 创建者：  ZQX
 * 创建时间：2018/5/3 17:57
 * 描述：    聊天适配器
 */

public class NewsAdapter extends BaseAdapter{

    private List<Conversation> datas;
    private Context context;
    private Conversation conversation;

    public NewsAdapter(List<Conversation> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();

            convertView = LinearLayout.inflate(context, R.layout.chat_item, null);

            viewHolder.tv_username = convertView.findViewById(R.id.tv_username);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolder.tv_content = convertView.findViewById(R.id.tv_content);

            viewHolder.btn_count = convertView.findViewById(R.id.btn_count);
            viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (datas.size() > 0){

            conversation = datas.get(position);
            if (conversation != null){
                Message message = conversation.getLatestMessage();
                if (message != null){
                    // 设置 会话列表用户名字
                    viewHolder.tv_username.setText(conversation.getTitle());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yy/mm/dd");
                    long time = message.getCreateTime();
                    String createTime = dateFormat.format(new Date(time));

                    // 时间
                    viewHolder.tv_time.setText(createTime);
                    // 设置最近一次会话
                    TextContent content = (TextContent) message.getContent();
                    String lastContent = content.getText();
                    viewHolder.tv_content.setText(lastContent);

                    // 设置未读消息数
                    int count = conversation.getUnReadMsgCnt();
                    if (count == 0){
                        viewHolder.btn_count.setVisibility(View.GONE);
                    } else {
                        viewHolder.btn_count.setVisibility(View.VISIBLE);
                        viewHolder.btn_count.setText(count + "");
                    }
                }
            }
        }

        return convertView;
    }

    class ViewHolder{
        TextView tv_username;
        TextView tv_time;
        TextView tv_content;

        ImageView iv_icon;
        Button btn_count;
    }
}

package zqx.rj.com.lovecar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.ChatListData;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.adapter
 * 文件名：  ChatAdapter
 * 创建者：  ZQX
 * 创建时间：2018/5/4 15:14
 * 描述：    聊天适配器
 */

public class ChatAdapter extends BaseAdapter{
    // 左边
    public static final int VALUE_LEFT_TEXT = 1;
    // 右边
    public static final int VALUE_RIGHT_TEXT = 2;

    private ChatListData data;
    private List<ChatListData> dataList;
    private Context mContext;

    public ChatAdapter(List<ChatListData> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderLeftText viewHolderLeftText = null;
        ViewHolderRightText viewHolderRightText = null;
        int type = getItemViewType(position);
        if (convertView == null){
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = new ViewHolderLeftText();
                    convertView = LinearLayout.inflate(mContext, R.layout.left_item, null);
                    viewHolderLeftText.tv_left_text = convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeftText);
                    break;

                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = new ViewHolderRightText();
                    convertView = LinearLayout.inflate(mContext, R.layout.right_item, null);
                    viewHolderRightText.tv_right_text = convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRightText);
                    break;
            }

        }else {
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = (ViewHolderLeftText) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = (ViewHolderRightText) convertView.getTag();
                    break;
            }
        }

        // 赋值
        data = dataList.get(position);
        switch (type){
            case VALUE_LEFT_TEXT:
                viewHolderLeftText.tv_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRightText.tv_right_text.setText(data.getText());
                break;
        }

        return convertView;
    }

    // 根据数据源的 position 来返回要显示的 item
    @Override
    public int getItemViewType(int position) {

        ChatListData data = dataList.get(position);
        int type = data.getType();
        return type;
    }

    //两个样式 返回2
    @Override
    public int getViewTypeCount() {
         return 3;
    }

    class ViewHolderLeftText{
        private TextView tv_left_text;
    }

    class ViewHolderRightText{
        private TextView tv_right_text;
    }
}

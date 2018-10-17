package zqx.rj.com.lovecar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.ChatListData;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.adapter
 * 文件名：  ChatAdapter
 * 创建者：  ZQX
 * 创建时间：2018/5/4 15:14
 * 描述：    聊天适配器
 */

public class ChatAdapter extends BaseAdapter {
    // 左边
    public static final int VALUE_LEFT_TEXT = 1;
    // 右边
    public static final int VALUE_RIGHT_TEXT = 2;

    // Target 头像
    private final File mIconFile;

    private ChatListData data;
    private List<ChatListData> dataList;
    private Context mContext;

    public ChatAdapter(List<ChatListData> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;

        String path = ShareUtils.getString(mContext, "targetIconPath", "");
        mIconFile = new File(path);
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
        if (convertView == null) {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = new ViewHolderLeftText();
                    convertView = LinearLayout.inflate(mContext, R.layout.left_item, null);
                    viewHolderLeftText.tv_left_text = convertView.findViewById(R.id.tv_left_text);
                    viewHolderLeftText.mIcon = convertView.findViewById(R.id.iv_icon);
                    convertView.setTag(viewHolderLeftText);
                    break;

                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = new ViewHolderRightText();
                    convertView = LinearLayout.inflate(mContext, R.layout.right_item, null);
                    viewHolderRightText.tv_right_text = convertView.findViewById(R.id.tv_right_text);
                    viewHolderRightText.mIcon = convertView.findViewById(R.id.iv_icon);
                    convertView.setTag(viewHolderRightText);
                    break;
            }

        } else {
            switch (type) {
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
        switch (type) {
            case VALUE_LEFT_TEXT:
                viewHolderLeftText.tv_left_text.setText(data.getText());

                Picasso.with(mContext)
                        .load(mIconFile)
                        .error(R.mipmap.ic_launcher)
                        .into(viewHolderLeftText.mIcon);

                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRightText.tv_right_text.setText(data.getText());
                UtilTools.updateIcon(mContext, viewHolderRightText.mIcon);
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

    private class ViewHolderLeftText {
        private TextView tv_left_text;
        private ImageView mIcon;
    }

    private class ViewHolderRightText {
        private TextView tv_right_text;
        private ImageView mIcon;
    }
}

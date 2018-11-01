package zqx.rj.com.lovecar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.IconName;

/**
 * author：  HyZhan
 * created： 2018/11/1 19:26
 * desc：    TODO
 */

public class TicketAdapter extends BaseAdapter {

    private Context context;
    private List<IconName> mDataList;

    public TicketAdapter(Context context, List<IconName> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_icon_name, null);

            viewHolder.title = convertView.findViewById(R.id.tv_lead);
            viewHolder.icon = convertView.findViewById(R.id.iv_icon);
            viewHolder.value = convertView.findViewById(R.id.tv_value);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mDataList.size() > 0){
            IconName iconName = mDataList.get(position);

            if (iconName != null) {
                viewHolder.icon.setImageResource(iconName.getIcon());
                viewHolder.title.setText(iconName.getName());
                viewHolder.value.setText(iconName.getValue());
            }
        }


        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView icon;
        TextView value;
    }
}

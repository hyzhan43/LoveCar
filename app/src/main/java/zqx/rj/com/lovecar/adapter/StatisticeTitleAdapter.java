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
import zqx.rj.com.lovecar.entity.StatisticeData;

/**
 * author：  HyZhan
 * created： 2018/10/17 14:46
 * desc：    TODO
 */

public class StatisticeTitleAdapter extends BaseAdapter {

    private Context context;
    private List<StatisticeData> mDataList;

    public StatisticeTitleAdapter(Context context, List<StatisticeData> mDataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.statistice_title_item, null);

            viewHolder.title = convertView.findViewById(R.id.tv_title);
            viewHolder.icon = convertView.findViewById(R.id.iv_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(mDataList.get(position).getTitle());
        viewHolder.icon.setImageResource(mDataList.get(position).getImage());

        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView icon;
    }
}

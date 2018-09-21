package zqx.rj.com.lovecar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.SearchHistoryData;
import zqx.rj.com.lovecar.utils.T;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.adapter
 * 文件名：  SearchHistoryAdapter
 * 创建者：  ZQX
 * 创建时间：2018/5/15 14:09
 * 描述：    历史记录
 */

public class SearchHistoryAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<SearchHistoryData> dataList;
    private SearchHistoryData data;
    private OnButtonListener listener;

    public interface OnButtonListener {
        void OnItemClick(View view, int position);
    }

    public SearchHistoryAdapter(Context context, List<SearchHistoryData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setButtonListener(OnButtonListener listener) {
        this.listener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.search_history, null);

            viewHolder.tv_start_place = convertView.findViewById(R.id.tv_start_place);
            viewHolder.tv_end_place = convertView.findViewById(R.id.tv_end_place);
            viewHolder.btn_delete = convertView.findViewById(R.id.btn_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = dataList.get(position);
        viewHolder.tv_start_place.setText(data.getStartPlace());
        viewHolder.tv_end_place.setText(data.getEndPlace());

        viewHolder.btn_delete.setTag(position);
        viewHolder.btn_delete.setOnClickListener(this);

        return convertView;
    }

    class ViewHolder {
        TextView tv_start_place;
        TextView tv_end_place;

        Button btn_delete;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                listener.OnItemClick(v, (Integer) v.getTag());
                break;
        }
    }
}

package zqx.rj.com.lovecar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.NewRounteData;
import zqx.rj.com.lovecar.entity.SearchResultData;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.adapter
 * 文件名：  NewRounteAdapter
 * 创建者：  ZQX
 * 创建时间：2018/4/20 17:54
 * 描述：    最新路线 Adapter
 */

public class SearchResultAdapter extends BaseAdapter{

    public List<SearchResultData> datas;
    private Context context;

    public SearchResultAdapter(List<SearchResultData> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (datas.size() > 0){
            return datas.size();
        }
        return 0;
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

            convertView = LayoutInflater.from(context).inflate(R.layout.new_rounte_item,
                    null, false);

            viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolder.main_size = convertView.findViewById(R.id.main_size);
            viewHolder.tv_start_place = convertView.findViewById(R.id.tv_start_place);
            viewHolder.tv_end_place = convertView.findViewById(R.id.tv_end_place);
            viewHolder.main_price = convertView.findViewById(R.id.main_price);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_time.setText(datas.get(position).getDeparture_time());
        viewHolder.main_size.setText(datas.get(position).getSurplus());

        String[] startPlace = datas.get(position).getFrom_place().split("-");
        viewHolder.tv_start_place.setText(startPlace[0]);

        String[] endPlace = datas.get(position).getTo_place().split("-");
        viewHolder.tv_end_place.setText(endPlace[0]);

        viewHolder.main_price.setText(datas.get(position).getPrice());

        return convertView;
    }

    class ViewHolder{

        TextView tv_time;
        TextView main_size;
        TextView tv_start_place;
        TextView tv_end_place;
        TextView main_price;
    }
}

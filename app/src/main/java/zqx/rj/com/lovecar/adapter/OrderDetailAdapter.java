package zqx.rj.com.lovecar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.OrderData;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.adapter
 * 文件名：  OrderDetailAdapter
 * 创建者：  ZQX
 * 创建时间：2018/5/9 16:45
 * 描述：    已完成 适配器
 */

public class OrderDetailAdapter extends BaseAdapter{

    private Context mContext;
    private List<OrderData> tickets;
    private OrderData data;

    public OrderDetailAdapter(Context mContext, List<OrderData> tickets) {
        this.mContext = mContext;
        this.tickets = tickets;
    }

    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int position) {
        return tickets.get(position);
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
            convertView = LinearLayout.inflate(mContext, R.layout.already_item, null);

            viewHolder.tv_order_number = convertView.findViewById(R.id.tv_order_number);
            viewHolder.tv_create_time = convertView.findViewById(R.id.tv_create_time);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolder.tv_start_place = convertView.findViewById(R.id.tv_start_place);
            viewHolder.tv_end_place = convertView.findViewById(R.id.tv_end_place);
            viewHolder.tv_count = convertView.findViewById(R.id.tv_count);
            viewHolder.tv_price = convertView.findViewById(R.id.tv_price);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = tickets.get(position);

        viewHolder.tv_order_number.setText(data.getOrder_number());
        viewHolder.tv_create_time.setText(data.getCreate_time());
        viewHolder.tv_time.setText(data.getDeparture_time());
        viewHolder.tv_start_place.setText(data.getFrom_place());
        viewHolder.tv_end_place.setText(data.getTo_place());
        viewHolder.tv_count.setText(data.getPoll());
        viewHolder.tv_price.setText("￥" + data.getPrice());

        return convertView;
    }

    class ViewHolder{
        TextView tv_order_number;
        TextView tv_create_time;
        TextView tv_time;
        TextView tv_start_place;
        TextView tv_end_place;
        TextView tv_count;
        TextView tv_price;

    }
}

package zqx.rj.com.lovecar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.SamePeopleData;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.adapter
 * 文件名：  SamePeopleAdapter
 * 创建者：  ZQX
 * 创建时间：2018/5/25 13:11
 * 描述：    TODO
 */

public class SamePeopleAdapter extends BaseAdapter {

    private Context context;
    private List<SamePeopleData> dataList;
    private SamePeopleData data;

    public SamePeopleAdapter(Context context, List<SamePeopleData> dataList) {
        this.context = context;
        this.dataList = dataList;
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

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.same_people_item,
                    null);

            viewHolder.icon = convertView.findViewById(R.id.civ_icon);
            viewHolder.name = convertView.findViewById(R.id.tv_name);
            viewHolder.time = convertView.findViewById(R.id.tv_time);
            viewHolder.startPlace = convertView.findViewById(R.id.tv_start_place);
            viewHolder.endPlace = convertView.findViewById(R.id.tv_end_place);
            viewHolder.surplus = convertView.findViewById(R.id.tv_surplus);
            viewHolder.remarks = convertView.findViewById(R.id.tv_remarks);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        data = dataList.get(position);

        UtilTools.loadImage(context, data.getThumb(), viewHolder.icon);

        viewHolder.name.setText(data.getPublisher());
        viewHolder.time.setText(data.getCreate_time());
        viewHolder.startPlace.setText(data.getFrom_place());
        viewHolder.endPlace.setText(data.getTo_place());
        viewHolder.surplus.setText(data.getNumber());
        viewHolder.remarks.setText(data.getRemark());

        return convertView;
    }

    class ViewHolder {
        CircleImageView icon;
        TextView name;
        TextView time;
        TextView startPlace;
        TextView endPlace;
        TextView surplus;
        TextView remarks;
    }
}

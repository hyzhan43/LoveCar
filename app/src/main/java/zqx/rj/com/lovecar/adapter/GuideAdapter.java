package zqx.rj.com.lovecar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.GuideInfo;
import zqx.rj.com.lovecar.entity.MomentInfo;

/**
 * author：  HyZhan
 * created：2018/9/23 23:53
 * desc：    回家指南 adapter
 */

public class GuideAdapter extends BaseAdapter {

    private List<GuideInfo> mList;
    private Context mContext;

    public GuideAdapter(List<GuideInfo> list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.guide_item, null);

            holder.mTvContent = convertView.findViewById(R.id.tv_content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String text = "翻着我们的照片，想念若隐若现，去年的冬天，我们笑得很甜......\\n—— 哎哟，不错哦!\n" +
                "翻着我们的照片，想念若隐若现，去年的冬天，我们笑得很甜......\\n—— 哎哟，不错哦!\n" +
                "翻着我们的照片，想念若隐若现，去年的冬天，我们笑得很甜......\\n—— 哎哟，不错哦!";
        holder.mTvContent.setText(text);

        return convertView;
    }

    class ViewHolder {
        TextView mTvContent;
    }

}

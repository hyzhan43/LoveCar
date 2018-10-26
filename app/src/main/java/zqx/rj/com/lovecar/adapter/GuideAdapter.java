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
import zqx.rj.com.lovecar.entity.response.ArticleItem;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * author：  HyZhan
 * created：2018/9/23 23:53
 * desc：    回家指南 adapter
 */

public class GuideAdapter extends BaseAdapter {

    private List<ArticleItem> mList;
    private Context mContext;

    public GuideAdapter(List<ArticleItem> list, Context context) {
        mList = list;
        mContext = context;
    }

    public void setNewData(List<ArticleItem> list) {
        mList = list;
        notifyDataSetChanged();
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

            holder.mImage = convertView.findViewById(R.id.iv_image);
            holder.mTvContent = convertView.findViewById(R.id.tv_content);
            holder.mTime = convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mList.get(position) != null){
            ArticleItem article = mList.get(position);

            UtilTools.loadImage(mContext, article.getImage_uri(), holder.mImage);
            holder.mTvContent.setText(article.getTitle());
            holder.mTime.setText(article.getDisplay_time());
        }

        return convertView;
    }

    class ViewHolder {
        ImageView mImage;
        TextView mTvContent;
        TextView mTime;
    }

}

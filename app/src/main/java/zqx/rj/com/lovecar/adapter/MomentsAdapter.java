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
import zqx.rj.com.lovecar.entity.MomentInfo;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * author：  HyZhan
 * created：2018/9/23 23:53
 * desc：    票圈 adapter
 */

public class MomentsAdapter extends BaseAdapter {

    private List<MomentInfo> mList;
    private Context mContext;
    private final int MAX_LINE_COUNT = 3;//最大显示行数

    private final int STATE_UNKNOW = -1;//未知状态
    private final int STATE_NOT_OVERFLOW = 1;//文本行数小于最大可显示行数
    private final int STATE_COLLAPSED = 2;//折叠状态
    private final int STATE_EXPANDED = 3;//展开状态

    private SparseArray<Integer> mTextStateList;//保存文本状态集合

    @SuppressLint("UseSparseArrays")
    public MomentsAdapter(List<MomentInfo> list, Context context) {
        mList = list;
        mContext = context;

        mTextStateList = new SparseArray<>();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.moment_item, null);

            holder.mNineGridView = convertView.findViewById(R.id.ng_view);
            holder.mContent = convertView.findViewById(R.id.tv_content);
            holder.mQuan = convertView.findViewById(R.id.tv_show_all);
            holder.mIvIcon = convertView.findViewById(R.id.iv_icon);
            holder.mTvName = convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MomentInfo info = mList.get(position);

        // 头像
        Picasso.with(mContext)
                .load(info.getIconUrl())
                .resizeDimen(R.dimen.icon_size, R.dimen.icon_size)
                .centerCrop()
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .into(holder.mIvIcon);

        holder.mTvName.setText(info.getName());

        List<ImageInfo> infoList = info.getImages();
        holder.mNineGridView.setAdapter(new NineGridViewClickAdapter(mContext, infoList));

        int state = mTextStateList.get(position, STATE_UNKNOW);
        if (state == STATE_UNKNOW) {
            // 当一个视图树的布局发生改变时，可以被ViewTreeObserver监听到
            // ViewTreeObserver.OnPreDrawListener  当视图树将要被绘制时，会调用的接口
            holder.mContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    //如果内容显示的行数大于限定显示行数
                    if (holder.mContent.getLineCount() > MAX_LINE_COUNT) {
                        holder.mContent.setMaxLines(MAX_LINE_COUNT);//设置最大显示行数
                        holder.mQuan.setVisibility(View.VISIBLE);//让其显示全文的文本框状态为显示
                        holder.mQuan.setText("[全文]");//设置其文字为全文
                        mTextStateList.put(position, STATE_COLLAPSED);
                    } else {
                        holder.mQuan.setVisibility(View.GONE);//显示全文隐藏
                        mTextStateList.put(position, STATE_NOT_OVERFLOW);//让其不能超过限定的行数
                    }

                    //这个回调会调用多次，获取完行数后记得注销监听
                    holder.mContent.getViewTreeObserver().removeOnPreDrawListener(this);

                    return true;
                }
            });
        } else {
            // 如果之前已经初始化过了，则使用保存的状态，无需在获取一次
            switch (state) {
                case STATE_NOT_OVERFLOW:
                    holder.mQuan.setVisibility(View.GONE);
                    break;
                case STATE_COLLAPSED:
                    holder.mContent.setMaxLines(MAX_LINE_COUNT);
                    holder.mQuan.setVisibility(View.VISIBLE);
                    holder.mQuan.setText("[全文]");
                    break;
                case STATE_EXPANDED:
                    holder.mContent.setMaxLines(Integer.MAX_VALUE);
                    holder.mQuan.setVisibility(View.VISIBLE);
                    holder.mQuan.setText("[收起]");
                    break;
            }
        }


        holder.mQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = mTextStateList.get(position, STATE_UNKNOW);
                if (state == STATE_COLLAPSED) {
                    holder.mContent.setMaxLines(Integer.MAX_VALUE);
                    holder.mQuan.setText("[收起]");
                    mTextStateList.put(position, STATE_EXPANDED);
                } else if (state == STATE_EXPANDED) {
                    holder.mContent.setMaxLines(MAX_LINE_COUNT);
                    holder.mQuan.setText("[全文]");
                    mTextStateList.put(position, STATE_COLLAPSED);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        NineGridView mNineGridView;
        ImageView mIvIcon;
        TextView mTvName;
        TextView mContent;
        TextView mQuan;
    }

}

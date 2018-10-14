package zqx.rj.com.lovecar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lzy.ninegrid.NineGridView;
import com.squareup.picasso.Picasso;

import zqx.rj.com.lovecar.R;

/**
 * author：  HyZhan
 * created：2018/9/28 23:56
 * desc：    TODO
 */

public class PicassoImageLoader implements NineGridView.ImageLoader{

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}

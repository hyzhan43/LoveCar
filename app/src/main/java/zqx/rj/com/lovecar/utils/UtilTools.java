package zqx.rj.com.lovecar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import zqx.rj.com.lovecar.R;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  UtilTools
 * 创建者：  ZQX
 * 创建时间：2018/5/6 14:32
 * 描述：    工具统一类
 */

public class UtilTools {

    // 设置字体
    public static void setFont(Context mContext, TextView textView){
        // 设置字体
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(), "font/login_title.ttf");
        textView.setTypeface(fontType);
    }

    public static void updateIcon(Context context, CircleImageView account_icon) {
        String url = ShareUtils.getString(context, "icon_url", "");
        if (TextUtils.isEmpty(url)){
            account_icon.setImageResource(R.mipmap.ic_launcher);
        }else {
            Uri uri = Uri.parse(url);
            //Picasso 加载图片简单用法
            Picasso.with(context)
                    .load(uri)
                    .error(R.mipmap.ic_launcher)
                    .into(account_icon);
        }
    }

    //  Bitmap -> File
    public static File saveFile(Bitmap bm, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path , fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    public static String getUserId(Context context){
        return ShareUtils.getString(context, "user_id", "0");
    }

    public static String getPhone(Context context){
        return ShareUtils.getString(context, "phone", "0");
    }

    public static String getState(String state){
        switch (Integer.parseInt(state)){
            case 1:
                return "未付款";
            case 2:
                return "已取消";
            case 3:
                return "已付款";
            case 4:
                return "已完成";

            default:
                return "未知";
        }
    }
}

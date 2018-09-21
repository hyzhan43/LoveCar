package zqx.rj.com.lovecar.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  T
 * 创建者：  ZQX
 * 创建时间：2018/4/20 13:47
 * 描述：    Toast 封装类
 */

public class T {

    // 默认 Toast 时间
    public static int TOAST_TIME = Toast.LENGTH_SHORT;

    public static Toast toast;

    public static void show(Context context, String text){

        if (toast == null){
            toast = Toast.makeText(context, text, TOAST_TIME);
        }else {
            toast.setText(text);
        }

        toast.show();
    }
}

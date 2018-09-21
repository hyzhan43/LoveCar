package zqx.rj.com.lovecar.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yatoooon.screenadaptation.ScreenAdapterTools;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  ScreenTools
 * 创建者：  ZQX
 * 创建时间：2018/4/21 14:03
 * 描述：    屏幕适配 封装类
 */

public class ScreenTools {

    public static void activity(Activity activity){
        ScreenAdapterTools.getInstance().loadView((ViewGroup) activity.getWindow().getDecorView());
    }

    public static void fragment(View view){
        ScreenAdapterTools.getInstance().loadView((ViewGroup) view);
    }
}

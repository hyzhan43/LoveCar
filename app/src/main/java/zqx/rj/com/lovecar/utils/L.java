package zqx.rj.com.lovecar.utils;

import android.util.Log;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  L
 * 创建者：  ZQX
 * 创建时间：2018/4/20 13:29
 * 描述：    Log 封装类
 */

public class L {

    // 开关
    public static boolean DEBUG = true;
    // TAG
    private static final String TAG = "LoveCar";

    public static void i(String logText){
        if (DEBUG){
            Log.i(TAG, logText);
        }
    }

    public static void d(String logText){
        if (DEBUG){
            Log.d(TAG, logText);
        }
    }

    public static void w(String logText){
        if (DEBUG){
            Log.w(TAG, logText);
        }
    }

    public static void e(String logText){
        if (DEBUG){
            Log.e(TAG, logText);
        }
    }


}

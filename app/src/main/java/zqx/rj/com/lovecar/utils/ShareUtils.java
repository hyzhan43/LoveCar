package zqx.rj.com.lovecar.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  ShareUtils
 * 创建者：  ZQX
 * 创建时间：2018/4/20 17:49
 * 描述：    SharedPreferences 封装
 */

public class ShareUtils {

    public static final String NAME = "config";

    // 键 值
    public static void putString(Context mContext, String key, String value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, mContext.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }


    // 键    默认值
    public static String getString (Context mContext, String key, String defvalue){

        SharedPreferences sp = mContext.getSharedPreferences(NAME, mContext.MODE_PRIVATE);
        return sp.getString(key, defvalue);
    }

    // 键    值
    public static void putInt(Context mContext, String key, int value){

        SharedPreferences sp = mContext.getSharedPreferences(NAME, mContext.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    // 键    默认值
    public static int getInt(Context mContext, String key, int defvalue){

        SharedPreferences sp = mContext.getSharedPreferences(NAME, mContext.MODE_PRIVATE);
        return sp.getInt(key, defvalue);
    }

    // 键    值
    public static void putBoolean(Context mContext, String key, boolean value){

        SharedPreferences sp = mContext.getSharedPreferences(NAME, mContext.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    // 键    默认值
    public static boolean getBoolean(Context mContext, String key, boolean defvalue){

        SharedPreferences sp = mContext.getSharedPreferences(NAME, mContext.MODE_PRIVATE);
        return sp.getBoolean(key, defvalue);
    }

    // 删除  单个
    public static void deleShare(Context mContext, String key){

        SharedPreferences sp = mContext.getSharedPreferences(NAME, mContext.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    // 删除   全部
    public static void deleAll(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, mContext.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}

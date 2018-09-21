package zqx.rj.com.lovecar.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  ActivityCollector
 * 创建者：  ZQX
 * 创建时间：2018/5/6 15:43
 * 描述：    所以活动管理器
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    /**
     * 添加活动
     * @param activity
     */
    public static void addActivity(Activity activity){
        if(!activities.contains(activity)){
            activities.add(activity);
        }
    }

    /**
     * 移除活动
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * 结束所有活动
     */
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}

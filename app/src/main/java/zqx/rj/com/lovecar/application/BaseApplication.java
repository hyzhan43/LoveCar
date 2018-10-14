package zqx.rj.com.lovecar.application;

import android.app.Application;

import com.lzy.ninegrid.NineGridView;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.litepal.LitePalApplication;

import cn.jpush.im.android.api.JMessageClient;
import zqx.rj.com.lovecar.fragment.MainFragment;
import zqx.rj.com.lovecar.utils.PicassoImageLoader;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.application
 * 文件名：  BaseApplication
 * 创建者：  ZQX
 * 创建时间：2018/4/20 13:32
 * 描述：    TODO
 */

public class BaseApplication extends LitePalApplication{

    // 创建
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化屏幕适配框架
        ScreenAdapterTools.init(this);

        // 极光 IM 即时通讯初始化
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);

        NineGridView.setImageLoader(new PicassoImageLoader());
    }
}

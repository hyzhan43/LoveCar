package zqx.rj.com.lovecar.ui;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  BaseActivity
 * 创建者：  ZQX
 * 创建时间：2018/4/20 13:33
 * 描述：    Activity 基类
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.utils.ActivityCollector;
import zqx.rj.com.lovecar.utils.L;
import zqx.rj.com.lovecar.utils.ScreenTools;
import zqx.rj.com.lovecar.utils.StaticClass;

/**
 * 主要做的事情
 * 1、统一的属性
 * 2、统一的接口
 * 3、统一的方法
 */
public class BaseActivity extends AppCompatActivity{

    private AlertDialog dialog;

    private class Handler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StaticClass.LOGIN_OUT:
                    showDownLine();
                    break;
            }
        }
    }
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为每个活动添加屏幕适配  和 账号事件监听

        // 子类必须先 setContentView 在 super 才能实现屏幕适配
        // 屏幕适配
        ScreenTools.activity(this);
        // 沉浸式状态栏
        ImmersionBar.with(this).init();

        // 注册监听
        JMessageClient.registerEventReceiver(this);

        //每创建一个活动，就加入到活动管理器中
        ActivityCollector.addActivity(this);
    }

    // 用户强制下线
    private void DownLine() {
        ActivityCollector.finishAll();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
        //每销毁一个活动，就从活动管理器中移除
        ActivityCollector.removeActivity(this);
    }

    // 用户登录状态变更事件
    public void onEvent(LoginStateChangeEvent event) {

        LoginStateChangeEvent.Reason reason = event.getReason();

        L.d("reason = " + reason);

        if (reason.user_logout == reason){
            handler.sendEmptyMessage(StaticClass.LOGIN_OUT);
        }
    }

    public void showDownLine(){
        dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.down_line_titles))
                .setMessage(getString(R.string.down_line_tips))
                .setPositiveButton(getString(R.string.right), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownLine();
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.show();
    }
}

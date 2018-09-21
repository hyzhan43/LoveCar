package zqx.rj.com.lovecar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.MainActivity;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  SplashActivity
 * 创建者：  ZQX
 * 创建时间：2018/5/6 14:28
 * 描述：    闪屏页
 */

public class SplashActivity extends BaseActivity {

    private TextView tv_splash;
    private Handler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        // 延迟 2000 ms
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);

        tv_splash = findViewById(R.id.tv_splash);

        // 设置字体
        UtilTools.setFont(this, tv_splash);
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    // 判断程序是否 第一次运行
                    if (isFirst()) {
                        // 去登录页面
                        startActivity(new Intent(SplashActivity.this,
                                LoginActivity.class));
                        finish();
                    } else {
                        // 自动登录
                        CheckPhoneAndPassword();
                    }
                    break;
                case StaticClass.LOGIN_FAIL:
                    if (msg.obj != null){
                        T.show(SplashActivity.this, msg.obj.toString());
                    }else {
                        T.show(SplashActivity.this, getString(R.string.network_fail));
                    }
                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class));
                    finish();
                    break;
                case StaticClass.LOGIN_SUCCESS:
                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                    finish();
                    break;

            }
        }
    }

    private void CheckPhoneAndPassword() {

        // 设置密码
        final String account = ShareUtils.getString(this, "account", "");
        final String password = ShareUtils.getString(this, "password", "");

        JMessageClient.login(account, password, new BasicCallback() {
            @Override
            public void gotResult(int code, String result) {
                if (code == 0) {
                    // 登录 爱宝车账号
                    loginAccount(account, password);
                } else {
                    Message message = new Message();
                    message.obj = result;
                    message.what = StaticClass.LOGIN_FAIL;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private void loginAccount(final String account, final String password) {

        new Thread() {
            @Override
            public void run() {
                RequestBody body = new FormBody.Builder()
                        .add("phone", account)
                        .add("password", password)
                        .build();


                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(API.USER_LOGIN, body);

                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJson(response.getData());
                } else {
                    handler.sendEmptyMessage(StaticClass.LOGIN_FAIL);
                }
            }
        }.start();
    }

    private void parseJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String result = jsonObject.getString("code");
            if (result.equals("1")) {
                JSONObject object = jsonObject.getJSONObject("data");
                String id = object.getString("id");
                String icon = object.getString("thumb");
                String phone = object.getString("phone");

                ShareUtils.putString(this,"icon_url", icon);
                ShareUtils.putString(this,"user_id", id);
                ShareUtils.putString(this,"phone", phone);

                handler.sendEmptyMessage(StaticClass.LOGIN_SUCCESS);
            } else if (result.equals("0")) {
                String errorMessage = jsonObject.getString("message");
                Message message = new Message();
                message.obj = errorMessage;
                message.what = StaticClass.LOGIN_FAIL;
                handler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(StaticClass.LOGIN_FAIL);
        }
    }


    // 判断程序是否第一次运行
    private boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        return isFirst;
    }

    // 禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}

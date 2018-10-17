package zqx.rj.com.lovecar.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.jpush.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;


import java.lang.reflect.Type;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.MainActivity;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.response.BaseResponse;
import zqx.rj.com.lovecar.entity.response.LoginRsp;
import zqx.rj.com.lovecar.utils.L;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;
import zqx.rj.com.lovecar.view.CustomDialog;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  LoginActivity
 * 创建者：  ZQX
 * 创建时间：2018/4/26 15:36
 * 描述：    登陆页面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_login;
    private TextView tv_lovecar;

    private TextView tv_register;
    private EditText et_account;
    private EditText et_password;
    private MyHandler handler;

    // 进度条
    private CustomDialog dialog;

    // 记住密码
    private CheckBox remenber_pass;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case StaticClass.LOGIN_SUCCESS:
                    dialog.dismiss();
                    ThisToActivity(LoginActivity.this, MainActivity.class);
                    // 标记我们已经登录过
                    ShareUtils.putBoolean(LoginActivity.this,
                            StaticClass.SHARE_IS_FIRST, false);
                    finish();
                    break;
                case StaticClass.LOGIN_FAIL:
                    dialog.dismiss();
                    if (msg.obj != null) {
                        T.show(LoginActivity.this, msg.obj.toString());
                    } else {
                        T.show(LoginActivity.this, getString(R.string.network_fail));
                    }
                    break;

                case StaticClass.IS_FIRST:
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case StaticClass.GET_USER_INFO:
                    getUserInfo();
                    break;
            }
        }
    }

    private void getUserInfo() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        handler = new MyHandler();
        initView();
        initDialog();
    }

    private void initDialog() {
        dialog = new CustomDialog(this,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_loding,
                R.style.Theme_dialog,
                Gravity.CENTER);
        dialog.setCancelable(false);
    }

    private void initView() {

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_lovecar = findViewById(R.id.tv_lovecar);
        UtilTools.setFont(this, tv_lovecar);

        tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);

        et_account = findViewById(R.id.et_account);
        et_account.setOnClickListener(this);
        et_password = findViewById(R.id.et_password);
        et_password.setOnClickListener(this);

        remenber_pass = findViewById(R.id.remenber_pass);

        // 设置选中状态
        boolean isCheck = ShareUtils.getBoolean(this, "remenber_pass", false);
        remenber_pass.setChecked(isCheck);
        if (isCheck) {
            // 设置密码
            et_account.setText(ShareUtils.getString(this, "account", ""));
            et_password.setText(ShareUtils.getString(this, "password", ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                CheckPhoneAndPassword();
                break;
            case R.id.tv_register:
                ThisToActivity(this, RegisterActivity.class);
                break;
            case R.id.et_account:
                et_account.setCursorVisible(true);
                break;
            case R.id.et_password:
                et_password.setCursorVisible(true);
                break;
        }
    }

    private void CheckPhoneAndPassword() {

        dialog.show();
        final String account = et_account.getText().toString();
        final String password = et_password.getText().toString();

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
                OkhttpResponse response = okHttp.post(LoginActivity.this, API.USER_LOGIN, body);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJson(response.getData());
                } else {
                    handler.sendEmptyMessage(StaticClass.LOGIN_FAIL);
                }
            }
        }.start();
    }

    private void parseJson(String data) {

        LoginRsp loginRsp = new Gson().fromJson(data, LoginRsp.class);

        Log.d("LST", "code->" + loginRsp.getCode());
        Log.d("LST", "msg->" + loginRsp.getMessage());

        if (loginRsp.getCode() == 1) {
            ShareUtils.putString(this, "token", loginRsp.getData().getAccessToken());
            ShareUtils.putString(this, "phone", et_account.getText().toString());
            handler.sendEmptyMessage(StaticClass.LOGIN_SUCCESS);
            handler.sendEmptyMessage(StaticClass.GET_USER_INFO);
        } else {
            Message message = new Message();
            message.obj = loginRsp.getMessage();
            message.what = StaticClass.LOGIN_FAIL;
            handler.sendMessage(message);
        }
    }

    private void parseJson2(String data) {


        try {
            JSONObject jsonObject = new JSONObject(data);
            String result = jsonObject.getString("code");
            if (result.equals("1")) {
                JSONObject object = jsonObject.getJSONObject("data");
                String id = object.getString("id");
                String icon = object.getString("thumb");
                String phone = object.getString("phone");

                ShareUtils.putString(this, "icon_url", icon);
                ShareUtils.putString(this, "user_id", id);
                ShareUtils.putString(this, "phone", phone);
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

    /**
     * 当前页面跳转到另一个页面
     */
    private void ThisToActivity(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        startActivity(intent);
    }


    // 假设我现在输入用户名和密码，但是我不点击登录，而是直接退出了
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null) {
            dialog.dismiss();
        }


        // 保存状态
        ShareUtils.putBoolean(this, "remenber_pass", remenber_pass.isChecked());
        // 是否记住密码
        if (remenber_pass.isChecked()) {
            // 记住用户名和密码
            ShareUtils.putString(this, "account", et_account.getText().toString());
            ShareUtils.putString(this, "password", et_password.getText().toString());
        } else {
            ShareUtils.deleShare(this, "account");
            ShareUtils.deleShare(this, "password");
        }
    }
}

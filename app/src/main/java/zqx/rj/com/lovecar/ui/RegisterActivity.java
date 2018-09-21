package zqx.rj.com.lovecar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.L;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ScreenTools;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  RegisterActivity
 * 创建者：  ZQX
 * 创建时间：2018/4/26 15:35
 * 描述：    注册页面
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button back;

    private Button btn_register;
    private EditText et_name;
    private EditText et_phone;
    private EditText et_password;
    private EditText et_password_again;
    private EditText et_code;

    // 同意协议
    private CheckBox cb_agree;
    // 发送验证码
    private Button btn_send_code;

    private String phone;
    private String code = "code";
    private String name;
    private String password;
    private String again;

    // 发送的验证码
    private String toCode = "tocode";

    // 验证码倒计时
    private int time = 60;
//    private static final int START_TIME = 1000;
//    private static final int END_TIME = 1001;
//
//    private static final int REGISTER_SUCCESS = 1002;
//    private static final int REGISTER_ERROR = 1003;
//    private static final int NETWORK_FAIL = 1004;

    private MyHandler handler;
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StaticClass.START_TIME:
                    btn_send_code.setText(time + " s");
                    break;
                case StaticClass.END_TIME:
                    btn_send_code.setEnabled(true);
                    btn_send_code.setText("重新发送");
                    time = 60;
                    break;
                case StaticClass.REGISTER_ERROR:
                    String error = msg.obj.toString();
                    T.show(RegisterActivity.this, error);
                    break;
                case StaticClass.REGISTER_SUCCESS:
                    T.show(RegisterActivity.this, "注册成功");
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case StaticClass.NETWORK_FAIL:
                    T.show(RegisterActivity.this, getString(R.string.network_fail));
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);

        handler = new MyHandler();
        initView();
    }

    private void initView() {
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        et_password_again = findViewById(R.id.et_password_again);
        et_code = findViewById(R.id.et_code);
        btn_send_code = findViewById(R.id.btn_send_code);
        btn_send_code.setOnClickListener(this);

        cb_agree = findViewById(R.id.cb_agree);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_register:
                checkPhone();
                break;
            case R.id.btn_send_code:
                // 倒计时
                timeOver();
                // 发送验证码
                sendCode();
                break;
        }
    }

    private void timeOver() {
        btn_send_code.setEnabled(false);
        // 开始倒计时
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (time > 0){
                    --time;
                    handler.sendEmptyMessage(StaticClass.START_TIME);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(StaticClass.END_TIME);
            }
        }).start();
    }

    private void checkPhone() {
        phone = et_phone.getText().toString();
        name = et_name.getText().toString();
        password = et_password.getText().toString();
        again = et_password_again.getText().toString();
        code = et_code.getText().toString();

        if (TextUtils.isEmpty(phone) & TextUtils.isEmpty(password) & TextUtils.isEmpty(again)
                & TextUtils.isEmpty(code)){
            T.show(this, "请把信息补全");
        }else if (!cb_agree.isChecked()){
            T.show(this,"请同意《爱宝车服务协议》");
        }else if (!password.equals(again)){
            T.show(this, "两次密码输入有误，请重新输入");
        }
        else if (!code.equals(toCode)){
            T.show(this, "验证码输入有误");
        }
        else {
            // 注册 爱宝车账号
            registerAccount();
        }
    }

    private void registerAccount() {
        new Thread(){
            @Override
            public void run() {
                RequestBody body = new FormBody.Builder()
                        .add("username", name)
                        .add("password", password)
                        .add("repassword", again)
                        .add("phone", phone)
                        .build();

                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(API.USER_REGISTER, body);

                if (response.getCode() == OkhttpResponse.STATE_OK){
                    parseRegisterJson(response.getData());
                }else {
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();
    }

    // 解析 注册   {"code":1,"message":"注册成功","data":[]}
    private void parseRegisterJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String code = jsonObject.getString("code");
            if (code.equals("1")){
                handler.sendEmptyMessage(StaticClass.REGISTER_SUCCESS);
            }else if (code.equals("0")){
                Message message = new Message();
                message.what = StaticClass.REGISTER_ERROR;
                message.obj = jsonObject.getString("message");
                handler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void sendCode() {
        phone = et_phone.getText().toString();
        if (!TextUtils.isEmpty(phone)){

            new Thread(){
                @Override
                public void run() {
                    RequestBody body = new FormBody.Builder()
                            .add("phone", phone)
                            .build();
                    OkHttp okHttp = new OkHttp();
                    OkhttpResponse response = okHttp.post(API.SEND_CODE, body);
                    if (response.getCode() == OkhttpResponse.STATE_OK){
                        parseCodeJson(response.getData());
                    }else {
                        handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                    }
                }
            }.start();
        }
    }

    private void parseCodeJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String code = jsonObject.getString("code");
            if (code.equals("1")){
                toCode = jsonObject.getJSONObject("data").getString("Verification");
            }else if (code.equals("0")){
                handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package zqx.rj.com.lovecar.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.jpush.im.android.api.JMessageClient;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.StaticClass;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  SettingActivity
 * 创建者：  ZQX
 * 创建时间：2018/5/6 14:52
 * 描述：    设置页面
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_login_out;

    public AlertDialog dialog;
    private TextView title;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);


        initView();
    }

    private void initView() {
        title = findViewById(R.id.tv_title);
        title.setText("设置");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        btn_login_out = findViewById(R.id.btn_login_out);
        btn_login_out.setOnClickListener(this);

        dialog = new AlertDialog.Builder(this)
                .setTitle("你确定要退出登录吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginOut();
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_out:
                dialog.show();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void LoginOut() {
        // 极光 退出
        JMessageClient.logout();
        // 标记 第一次登录
        ShareUtils.putBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

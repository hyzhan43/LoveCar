package zqx.rj.com.lovecar.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.response.IconRsp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;
import zqx.rj.com.lovecar.view.CustomDialog;

import static cn.jpush.im.android.api.model.UserInfo.Field.gender;
import static cn.jpush.im.android.api.model.UserInfo.Field.nickname;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  ImformationActivity
 * 创建者：  ZQX
 * 创建时间：2018/5/7 17:45
 * 描述：    个人信息页面
 */

public class ImformationActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private Button back;
    private LinearLayout ll_icon;
    private LinearLayout ll_nickname;
    private LinearLayout ll_phone;
    private LinearLayout ll_age;
    private LinearLayout ll_sex;

    private CircleImageView mIcon;

    private CustomDialog mPhotoDialog;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;
    private File tempFile = null;
    private Bitmap iconBitmap;

    private TextView tv_nickname;
    private TextView tv_age;
    private TextView tv_sex;
    private TextView tv_phone;
    private AlertDialog mEditDialog;
    private Button bt_cancel;
    private Button bt_right;
    private EditText et_input;

    private UserInfo userInfo;

    private AlertDialog sexDialog;
    private Button sex_cancel;
    private Button sex_right;
    private RadioButton rb_women;
    private RadioButton rb_man;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_imformation);
        super.onCreate(savedInstanceState);

        initView();
        initDialog();
        initEditDialog();
        initSexDialog();
    }


    private void initDialog() {
        mPhotoDialog = new CustomDialog(this,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_photo,
                R.style.pop_anim_style,
                Gravity.BOTTOM);
        mPhotoDialog.setCancelable(false);

        btn_camera = mPhotoDialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = mPhotoDialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = mPhotoDialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
    }

    private void initView() {

        title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.my_imformation));
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        ll_icon = findViewById(R.id.ll_icon);
        ll_nickname = findViewById(R.id.ll_nickname);
        ll_phone = findViewById(R.id.ll_phone);
        ll_sex = findViewById(R.id.ll_sex);

        ll_icon.setOnClickListener(this);
        ll_nickname.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        ll_sex.setOnClickListener(this);

        mIcon = findViewById(R.id.my_icon);
        UtilTools.updateIcon(this, mIcon);


        tv_nickname = findViewById(R.id.tv_nickname);
        tv_sex = findViewById(R.id.tv_sex);
        tv_phone = findViewById(R.id.tv_phone);

        userInfo = JMessageClient.getMyInfo();
        // 设置信息
        tv_nickname.setText(userInfo.getNickname());
        tv_phone.setText(userInfo.getUserName());

        UserInfo.Gender gender = userInfo.getGender();
        if (gender == UserInfo.Gender.female) {
            tv_sex.setText(getResources().getString(R.string.women));
        } else {
            tv_sex.setText(getResources().getString(R.string.man));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                // 设置头像
                backIconToMy();
                finish();
                break;
            case R.id.ll_icon:
                mPhotoDialog.show();
                break;
            case R.id.ll_nickname:
                mEditDialog.show();
                break;
            case R.id.ll_sex:
                sexDialog.show();
                break;
            case R.id.btn_cancel:
                mPhotoDialog.dismiss();
                break;
            case R.id.btn_picture:
                // 跳转相册
                toPicture();
                break;
            case R.id.btn_camera:
                // 跳转到相机
                toCamera();
                break;
        }
    }

    // 更新用户信息
    private void updateUserInfo(UserInfo.Field updateField) {
        JMessageClient.updateMyInfo(updateField, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int code, String result) {
                if (code == 0) {
                    T.show(ImformationActivity.this, "更新成功");
                } else {
                    T.show(ImformationActivity.this, "失败：" + result);
                }
            }
        });
    }

    private void initEditDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null);
        mEditDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setView(view)
                .create();
        mEditDialog.setCancelable(false);
        et_input = view.findViewById(R.id.et_input);
        String name = tv_nickname.getText().toString();
        et_input.setText(name);
        et_input.setSelection(name.length());

        bt_cancel = view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditDialog.dismiss();
            }
        });
        bt_right = view.findViewById(R.id.bt_right);
        bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNickName = et_input.getText().toString();
                userInfo.setNickname(newNickName);
                updateUserInfo(nickname);
                tv_nickname.setText(newNickName);
                mEditDialog.dismiss();
            }
        });
    }


    private void initSexDialog() {
        View view = LinearLayout.inflate(this, R.layout.dialog_sex, null);
        sexDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        sexDialog.setCancelable(false);

        rb_man = view.findViewById(R.id.rb_man);
        rb_women = view.findViewById(R.id.rb_women);
        sex_cancel = view.findViewById(R.id.bt_cancel);
        sex_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog.dismiss();
            }
        });
        sex_right = view.findViewById(R.id.bt_right);
        sex_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_man.isChecked()) {
                    userInfo.setGender(UserInfo.Gender.male);
                    tv_sex.setText(getResources().getString(R.string.man));
                } else if (rb_women.isChecked()) {
                    userInfo.setGender(UserInfo.Gender.female);
                    tv_sex.setText(getResources().getString(R.string.women));
                }
                if (rb_man.isChecked() || rb_women.isChecked()) {
                    updateUserInfo(gender);
                    sexDialog.dismiss();
                }
            }
        });
    }

    // 跳转相机
    private void toCamera() {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 传递路径
        File file = new File(Environment.getExternalStorageDirectory(), StaticClass.PHOTO_IMAGE_FILE_NAME);
        Uri uri = Uri.fromFile(file);
        // 更改系统默认拍照存储路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, StaticClass.CAMERA_REQUEST_CODE);
        mPhotoDialog.dismiss();
    }

    // 跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        // 选择照片
        intent.setType("image/*");
        startActivityForResult(intent, StaticClass.IMAGE_REQUEST_CODE);
        mPhotoDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相册数据
                case StaticClass.IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                // 相机数据
                case StaticClass.CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            StaticClass.PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                // 照片裁剪成功返回
                case StaticClass.RESULT_REQUEST_CODE:
                    // 有可能点击舍去
                    if (data != null) {
                        // 拿到图片设置
                        setImageToView(data);
                        // 既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    // 设置头像
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            iconBitmap = bundle.getParcelable("data");
            mIcon.setImageBitmap(iconBitmap);

            try {
                File iconFile = UtilTools.saveFile(
                        iconBitmap,
                        Environment.getExternalStorageDirectory().getPath(),
                        "ibc_icon.jpg");

                updateIcon(iconFile);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void startPhotoZoom(Uri uri) {
        if (uri != null) {
            // 设置裁剪行为
            Intent intent = new Intent("com.android.camera.action.CROP");
            // 裁剪数据和类型
            intent.setDataAndType(uri, "image/*");
            // 设置裁剪
            intent.putExtra("crop", "true");
            // 裁剪宽高比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // 裁剪图片的质量 (分辨率)
            intent.putExtra("outputX", 320);
            intent.putExtra("outputY", 320);
            // 发送数据 (固定写法 return-data)
            intent.putExtra("return-data", true);

            startActivityForResult(intent, StaticClass.RESULT_REQUEST_CODE);
        }
    }

    public void updateIcon(final File file) {

        final String phone = UtilTools.getPhone(ImformationActivity.this);
        new Thread() {
            @Override
            public void run() {

                MultipartBody multipartBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("phone", phone)
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(MediaType.parse("file/*"), file))
                        .build();

                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.post(ImformationActivity.this,
                        API.UPDATE_ICON, multipartBody);

                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJson(response.getData());
                } else {
                    // TODO 错误
                }
            }
        }.start();
    }

    private void parseJson(String data) {
        IconRsp iconRsp = UtilTools.jsonToBean(data, IconRsp.class);

        if (iconRsp.getCode() == 1){
            ShareUtils.putString(this, "icon_url", iconRsp.getData().getThumb());
        }else {
            Log.d("LST", "错误->" + iconRsp.getMessage());
        }
    }

    private void parseJson2(String resposne) {
        try {
            JSONObject jsonObject = new JSONObject(resposne);
            String code = jsonObject.getString("code");
            if (code.equals("1")) {
                jsonObject = jsonObject.getJSONObject("data");
                String url = jsonObject.getString("imgUrl");
                ShareUtils.putString(this, "icon_url", url);

            } else if (code.equals("0")) {

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backIconToMy();
    }

    // 设置 回传 头像
    private void backIconToMy() {
        Intent intent = new Intent();
        if (iconBitmap != null) {
            intent.putExtra("update", true);
            setResult(StaticClass.RESULT_ICON, intent);
        }
    }
}

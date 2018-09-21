package zqx.rj.com.lovecar.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import zqx.rj.com.lovecar.Observer.ObserverManager;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.Get;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;

public class PublishActivity extends BaseActivity implements View.OnClickListener {

    private TimePickerView pvTime;
    private TextView mTime;

    private TextView mStartPlace;
    private TextView mEndPlace;
    private EditText mName;
    private EditText mPhone;
    private EditText mCarNumber;
    private EditText mPrice;

    private OptionsPickerView pvOptions;
    private List<Integer> seats;
    private TextView mSeatCount;

    private static final int UPDATE_SUC = 1;
    private static final int UPDATE_FAIL = 2;
    private static MyHandler handler;

    private static class MyHandler extends Handler {

        WeakReference<Activity> mWeakReference;

        public MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final PublishActivity activity = (PublishActivity) mWeakReference.get();

            switch (msg.what) {

                case UPDATE_SUC:
                    // 同步 mainFragment -> listView 信息
                    activity.updateNewsTickets();
                    break;
                case UPDATE_FAIL:

                    String errorMsg = (String) msg.obj;
                    T.show(activity, errorMsg);
                    break;

                case StaticClass.NETWORK_FAIL:
                    T.show(activity, activity.getString(R.string.network_fail));
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        handler = new MyHandler(this);

        initView();
        initTimePicker();
        initSeatPicker();
    }

    @SuppressWarnings("unchecked")
    private void initSeatPicker() {

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mSeatCount.setText(String.valueOf(options1 + 10));
            }
        })
                .setSubmitColor(getResources().getColor(R.color.color_main))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.color_main))//取消按钮文字颜色
                .setContentTextSize(18)//滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();


        pvOptions.setPicker(getOptionsDatas());//添加数据源
    }

    public List<Integer> getOptionsDatas() {

        if (seats == null) {
            seats = new ArrayList<>();
            for (int i = 10; i < 61; i++) {
                seats.add(i);
            }
        }
        return seats;
    }

    private void initTimePicker() {
        // 时间选择器
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTime.setText(getTime(date));
            }
        }).setSubmitColor(getResources().getColor(R.color.color_main))
                .setCancelColor(getResources().getColor(R.color.color_main))
                //分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏。
                .setType(new boolean[]{false, true, true, true, true, false})
                .setLabel(null, "月", "日", "点", "分", null)
                .build();

    }

    private String getTime(Date date) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return simpleDateFormat.format(date);
    }

    private void initView() {
        TextView title = findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.publish));

        Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        LinearLayout timeLayout = findViewById(R.id.ll_start_time);
        timeLayout.setOnClickListener(this);

        LinearLayout startLayout = findViewById(R.id.ll_start_place);
        startLayout.setOnClickListener(this);

        LinearLayout endLayout = findViewById(R.id.ll_end_place);
        endLayout.setOnClickListener(this);

        mTime = findViewById(R.id.tv_time);

        Button publish = findViewById(R.id.btn_publish);
        publish.setOnClickListener(this);

        mStartPlace = findViewById(R.id.tv_start_place);
        mEndPlace = findViewById(R.id.tv_end_place);
        mName = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_phone);
        mCarNumber = findViewById(R.id.et_carnumber);
        mPrice = findViewById(R.id.et_price);

        ImageView change = findViewById(R.id.iv_change);
        change.setOnClickListener(this);

        mSeatCount = findViewById(R.id.tv_seat_count);
        mSeatCount.setOnClickListener(this);
        TextView seat = findViewById(R.id.tv_seat);
        seat.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_start_time:
                // 显示当前系统时间
                pvTime.setDate(Calendar.getInstance());
                pvTime.show();
                break;
            case R.id.btn_publish:

                if (checkInput()) {
                    // 更新网络信息
                    uploadNetWork();
                }
                break;

            case R.id.ll_start_place:
                Intent intent = new Intent(this, InputTipsActivity.class);
                startActivityForResult(intent, StaticClass.REQUEST_START);
                break;
            case R.id.ll_end_place:
                Intent intent2 = new Intent(this, InputTipsActivity.class);
                startActivityForResult(intent2, StaticClass.REQUEST_END);
                break;

            case R.id.iv_change:
                String start = Get.text(mStartPlace);
                String end = Get.text(mEndPlace);
                if ((!TextUtils.isEmpty(start))
                        && (!TextUtils.isEmpty(end))) {
                    mStartPlace.setText(end);
                    mEndPlace.setText(start);
                } else {
                    T.show(PublishActivity.this, "请输入上车地点/下车地点");
                }
                break;

            case R.id.tv_seat_count:
            case R.id.tv_seat:
                pvOptions.show();
                break;
        }
    }

    /**
     * 提交信息到服务器
     */
    private void uploadNetWork() {

        new Thread() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();
                RequestBody body = new FormBody.Builder()
                        .add("publisher", Get.text(mName))
                        .add("phone", Get.text(mPhone))
                        .add("from_place", Get.text(mStartPlace))
                        .add("to_place", Get.text(mEndPlace))
                        .add("plate_number", Get.text(mCarNumber))
                        .add("price", Get.text(mPrice))
                        .add("surplus", Get.text(mSeatCount))
                        .add("departure_time", Get.text(mTime))
                        .add("user_id", UtilTools.getUserId(PublishActivity.this))
                        .build();

                OkhttpResponse response = okHttp.post(API.UPDATE_NEW_TICKETS, body);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parseJson(response.getData());
                } else {
                    handler.sendEmptyMessage(StaticClass.NETWORK_FAIL);
                }
            }
        }.start();
    }

    private void parseJson(String datas) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(datas);
            String code = jsonObject.getString("code");
            if (code.equals("1")) {
                handler.sendEmptyMessage(UPDATE_SUC);
            } else if (code.equals("0")) {

                Message message = new Message();
                message.what = UPDATE_FAIL;
                message.obj = jsonObject.getString("message");
                handler.sendMessage(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateNewsTickets() {
        ObserverManager.Holder.instance.notifyListener();
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(Get.text(mStartPlace))
                || TextUtils.isEmpty(Get.text(mEndPlace))
                || TextUtils.isEmpty(Get.text(mTime))
                || TextUtils.isEmpty(Get.text(mName))
                || TextUtils.isEmpty(Get.text(mPhone))
                || TextUtils.isEmpty(Get.text(mCarNumber))
                || TextUtils.isEmpty(Get.text(mPrice))) {

            T.show(this, getResources().getString(R.string.input_empty_info));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticClass.REQUEST_START) {
            if (resultCode == StaticClass.RESULT_CODE_INPUTTIPS && data != null) {
                Tip tip = data.getParcelableExtra("tip");
                if (tip.getName() != null) {
                    mStartPlace.setText(tip.getName());
                }
            }

            if (resultCode == StaticClass.RESULT_CODE_KEYWORDS && data != null) {
                String keywords = data.getStringExtra("query");
                mStartPlace.setText(keywords);
            }
        } else if (requestCode == StaticClass.REQUEST_END) {
            if (resultCode == StaticClass.RESULT_CODE_INPUTTIPS && data != null) {
                Tip tip = data.getParcelableExtra("tip");
                if (tip.getName() != null) {
                    mEndPlace.setText(tip.getName());
                }
            }

            if (resultCode == StaticClass.RESULT_CODE_KEYWORDS && data != null) {
                String keywords = data.getStringExtra("query");
                mEndPlace.setText(keywords);
            }
        }
    }

}

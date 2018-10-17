package zqx.rj.com.lovecar.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.ui.ImformationActivity;
import zqx.rj.com.lovecar.ui.SettingActivity;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.UtilTools;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.fragment
 * 文件名：  MainFragment
 * 创建者：  ZQX
 * 创建时间：2018/4/20 14:25
 * 描述：    我的
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {

//    private static final int REQUEST_ICON = 1;
//    private static final int RESULT_ICON = 4;
    private LinearLayout ll_setting;
    private LinearLayout ll_my_imformation;
    private LinearLayout ll_my_wallet;
    private LinearLayout ll_my_like;
    private LinearLayout ll_my_aboue_us;
    private LinearLayout ll_my_update;

    private CircleImageView account_icon;
    private TextView account_name;


    @Override
    public int getLayoutId() {
        return R.layout.frag_my;
    }

    @Override
    public void initView(View view) {

        // 沉浸式状态栏
        ImmersionBar.with(this).init();

        ll_setting = view.findViewById(R.id.ll_setting);
        ll_setting.setOnClickListener(this);

        ll_my_imformation = view.findViewById(R.id.ll_my_imformation);
        ll_my_imformation.setOnClickListener(this);

        ll_my_wallet = view.findViewById(R.id.ll_my_wallet);
        ll_my_wallet.setOnClickListener(this);

        ll_my_like = view.findViewById(R.id.ll_my_like);
        ll_my_like.setOnClickListener(this);

        ll_my_aboue_us = view.findViewById(R.id.ll_my_aboue_us);
        ll_my_aboue_us.setOnClickListener(this);

        ll_my_update = view.findViewById(R.id.ll_my_update);
        ll_my_update.setOnClickListener(this);

        account_icon = view.findViewById(R.id.account_icon);
        UtilTools.updateIcon(getContext(), account_icon);

        account_name = view.findViewById(R.id.account_name);
        UserInfo userInfo = JMessageClient.getMyInfo();
        account_name.setText(userInfo.getNickname());
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_setting:
                ToActivity( SettingActivity.class);
                break;
            case R.id.ll_my_imformation:
                Intent intent = new Intent(getActivity(), ImformationActivity.class);
                startActivityForResult(intent, StaticClass.REQUEST_ICON);
                break;
            case R.id.ll_my_wallet:

                break;
            case R.id.ll_my_like:

                break;
            case R.id.ll_my_aboue_us:

                break;
            case R.id.ll_my_update:

                break;
        }
    }

    // 跳转页面
    private void ToActivity(Class cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case StaticClass.REQUEST_ICON:
                if (resultCode == StaticClass.RESULT_ICON){
                    boolean isUpdate = data.getBooleanExtra("update", false);
                    if (isUpdate == true){
                        UtilTools.updateIcon(getContext(), account_icon);
                    }
                }
                break;
        }
    }
}

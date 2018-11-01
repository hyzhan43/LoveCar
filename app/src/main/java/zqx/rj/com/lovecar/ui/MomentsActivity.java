package zqx.rj.com.lovecar.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.entity.Moment;
import zqx.rj.com.lovecar.entity.MomentsInfo;
import zqx.rj.com.lovecar.entity.OkhttpResponse;
import zqx.rj.com.lovecar.entity.response.CircleRsp;
import zqx.rj.com.lovecar.utils.API;
import zqx.rj.com.lovecar.utils.OkHttp;
import zqx.rj.com.lovecar.utils.ShareUtils;
import zqx.rj.com.lovecar.utils.T;
import zqx.rj.com.lovecar.utils.UtilTools;

public class MomentsActivity extends BaseActivity implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks,
        BGANinePhotoLayout.Delegate {

    private MyHandler handler;
    private List<MomentsInfo> mMomentsInfo = new ArrayList<>();

    private final int MAX_LINE_COUNT = 3;//最大显示行数

    private final int STATE_UNKNOW = -1;//未知状态
    private final int STATE_NOT_OVERFLOW = 1;//文本行数小于最大可显示行数
    private final int STATE_COLLAPSED = 2;//折叠状态
    private final int STATE_EXPANDED = 3;//展开状态

    private SparseArray<Integer> mTextStateList;//保存文本状态集合

    private final static int NET_WORK = -1;
    private final static int SUCCESS = 1;
    private final static int ERROR = 2;
    private final static int DELETE_SUC = 5;

    private final static int PUBLISH_SUC = 3;
    private final static int PUBLISH_FAIL = 4;

    private static final int RC_ADD_MOMENT = 1;
    private static final int PRC_PHOTO_PREVIEW = 1;

    private BGANinePhotoLayout mCurrentClickNpl;

    private RecyclerView mMomentRv;
    private MomentAdapter mMomentAdapter;

    private static class MyHandler extends Handler {

        WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MomentsActivity activity = (MomentsActivity) weakReference.get();
            switch (msg.what) {
                case SUCCESS:
                    activity.mMomentAdapter.setData(activity.mMomentsInfo);
                    break;
                case ERROR:
                    T.show(activity, msg.obj.toString());
                    break;
                case NET_WORK:
                    T.show(activity, activity.getString(R.string.network_fail));
                    break;
                case PUBLISH_SUC:
                    T.show(activity, "发布成功");
                    activity.updateMomentData((Moment) msg.obj);
                    break;
                case PUBLISH_FAIL:
                    T.show(activity, "发布失败");
                    break;
                case DELETE_SUC:
                    T.show(activity, "删除成功");
                    activity.mMomentAdapter.removeItem((Integer) msg.obj);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);

        initView();
        initData();
    }

    private void initView() {

        handler = new MyHandler(this);

        ImageView mIvCamera = findViewById(R.id.iv_camera);
        mIvCamera.setVisibility(View.VISIBLE);
        mIvCamera.setOnClickListener(this);

        TextView mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(getResources().getString(R.string.ticket_moments));

        Button mBtnBack = findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mMomentRv = findViewById(R.id.rv_moment_list_moments);

        mMomentAdapter = new MomentAdapter(mMomentRv);
        mMomentRv.setLayoutManager(new LinearLayoutManager(this));
        mMomentRv.setAdapter(mMomentAdapter);
        mMomentRv.addOnScrollListener(new BGARVOnScrollListener(this));
    }

    private void initData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();
                OkhttpResponse response = okHttp.get(MomentsActivity.this, API.GET_CIRCLE);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    parsingJson(response.getData());
                } else {
                    handler.sendEmptyMessage(NET_WORK);
                }
            }
        }).start();
    }

    private void parsingJson(String data) {

        CircleRsp circleRsp = UtilTools.jsonToBean(data, CircleRsp.class);
        if (circleRsp.getCode() == 1) {
            mMomentsInfo = circleRsp.getData();
            handler.sendEmptyMessage(SUCCESS);
        } else {
            Message message = new Message();
            message.what = ERROR;
            message.obj = circleRsp.getMessage();
            handler.sendMessage(message);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.iv_camera:
                startActivityForResult(new Intent(this, MomentAddActivity.class), RC_ADD_MOMENT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_ADD_MOMENT) {

            // 选择 相册返回的数据
            Moment moment = MomentAddActivity.getMoment(data);

            // post 到服务器
            postMomentData(moment);
        }
    }

    public void updateMomentData(Moment moment) {
        MomentsInfo momentsInfo = new MomentsInfo();

        String url = ShareUtils.getString(this, "icon_url", "");
        momentsInfo.setThumb(url);
        UserInfo userInfo = JMessageClient.getMyInfo();
        momentsInfo.setUsername(userInfo.getNickname());
        momentsInfo.setContent(moment.getContent());
        momentsInfo.setImg_items(moment.getPhotos());

        mMomentAdapter.addFirstItem(momentsInfo);
        mMomentRv.smoothScrollToPosition(0);
    }

    private void postMomentData(final Moment moment) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();

                MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

                for (String path : moment.getPhotos()) {

                    File file = new File(path);
//                    // MediaType.parse() 里面是上传的文件类型。
                    RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                    String filename = file.getName();
//                    // 参数分别为， 请求key ，文件名称 ， RequestBody
                    requestBody.addFormDataPart("file[]", filename, body);
                }

                requestBody.addFormDataPart("content", moment.getContent());


                OkhttpResponse response = okHttp.post(MomentsActivity.this,
                        API.POST_CIRCLE, requestBody.build());

                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    Message message = new Message();
                    message.what = PUBLISH_SUC;
                    message.obj = moment;
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(PUBLISH_FAIL);
                }

            }
        }).start();
    }

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (mCurrentClickNpl == null) {
            return;
        }

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(this)
                    .saveImgDir(downloadDir); // 保存图片的目录，如果传 null，则没有保存图片功能

            if (mCurrentClickNpl.getItemCount() == 1) {
                // 预览单张图片
                photoPreviewIntentBuilder.previewPhoto(mCurrentClickNpl.getCurrentClickItem());
            } else if (mCurrentClickNpl.getItemCount() > 1) {
                // 预览多张图片
                photoPreviewIntentBuilder.previewPhotos(mCurrentClickNpl.getData())
                        .currentPosition(mCurrentClickNpl.getCurrentClickItemPosition()); // 当前预览图片的索引
            }
            startActivity(photoPreviewIntentBuilder.build());
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", PRC_PHOTO_PREVIEW, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PREVIEW) {
            T.show(this, "您拒绝了「图片预览」所需要的相关权限!");
        }
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }

    private class MomentAdapter extends BGARecyclerViewAdapter<MomentsInfo> implements View.OnClickListener {

        @SuppressLint("UseSparseArrays")
        public MomentAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.moment_item);

            mTextStateList = new SparseArray<>();
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, MomentsInfo momentsInfo) {

            ImageView mIcon = helper.getView(R.id.iv_icon);
            UtilTools.loadImage(MomentsActivity.this, momentsInfo.getThumb(), mIcon);

            TextView name = helper.getView(R.id.tv_name);
            name.setText(momentsInfo.getUsername());

            TextView time = helper.getView(R.id.tv_time);
            time.setText(momentsInfo.getCreate_time());

            TextView delete = helper.getView(R.id.tv_delete);
            if (momentsInfo.getCurrent_user()) {
                delete.setVisibility(View.VISIBLE);
                Log.d("LST", "position" + position + "id =>" + momentsInfo.getId());
            } else {
                delete.setVisibility(View.GONE);
            }
            delete.setTag(position);
            delete.setOnClickListener(this);

            TextView content = helper.getView(R.id.tv_content);
            content.setText(momentsInfo.getContent());
            setContent(helper, position);

            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(MomentsActivity.this);
            ninePhotoLayout.setData(momentsInfo.getImg_items());
        }

        @Override
        public void onClick(final View v) {
            AlertDialog dialog = new AlertDialog
                    .Builder(MomentsActivity.this)
                    .setTitle("提示")
                    .setMessage("确定删除吗？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendDeletedRequest((Integer) v.getTag());
                        }
                    })
                    .show();

        }
    }

    private void sendDeletedRequest(final Integer position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttp = new OkHttp();

                String id = mMomentAdapter.getData().get(position).getId();

                Log.d("LST", "deleted id=>" + id + "position=>" + position);
                RequestBody body = new FormBody.Builder()
                        .add("id", id)
                        .build();

                OkhttpResponse response = okHttp.post(MomentsActivity.this, API.DELETE_CIRCLE, body);
                if (response.getCode() == OkhttpResponse.STATE_OK) {
                    Message message = new Message();
                    message.what = DELETE_SUC;
                    message.obj = position;
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(NET_WORK);
                }
            }
        }).start();
    }

    private void setContent(BGAViewHolderHelper helper, final int position) {
        int state = mTextStateList.get(position, STATE_UNKNOW);

        final TextView mContent = helper.getView(R.id.tv_content);
        final TextView mQuan = helper.getView(R.id.tv_show_all);

        if (state == STATE_UNKNOW) {
            // 当一个视图树的布局发生改变时，可以被ViewTreeObserver监听到
            // ViewTreeObserver.OnPreDrawListener  当视图树将要被绘制时，会调用的接口
            mContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    //如果内容显示的行数大于限定显示行数
                    if (mContent.getLineCount() > MAX_LINE_COUNT) {
                        mContent.setMaxLines(MAX_LINE_COUNT);//设置最大显示行数
                        mQuan.setVisibility(View.VISIBLE);//让其显示全文的文本框状态为显示
                        mQuan.setText("[全文]");//设置其文字为全文
                        mTextStateList.put(position, STATE_COLLAPSED);
                    } else {
                        mQuan.setVisibility(View.GONE);//显示全文隐藏
                        mTextStateList.put(position, STATE_NOT_OVERFLOW);//让其不能超过限定的行数
                    }

                    //这个回调会调用多次，获取完行数后记得注销监听
                    mContent.getViewTreeObserver().removeOnPreDrawListener(this);

                    return true;
                }
            });
        } else {
            // 如果之前已经初始化过了，则使用保存的状态，无需在获取一次
            switch (state) {
                case STATE_NOT_OVERFLOW:
                    mQuan.setVisibility(View.GONE);
                    break;
                case STATE_COLLAPSED:
                    mContent.setMaxLines(MAX_LINE_COUNT);
                    mQuan.setVisibility(View.VISIBLE);
                    mQuan.setText("[全文]");
                    break;
                case STATE_EXPANDED:
                    mContent.setMaxLines(Integer.MAX_VALUE);
                    mQuan.setVisibility(View.VISIBLE);
                    mQuan.setText("[收起]");
                    break;
            }
        }


        mQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = mTextStateList.get(position, STATE_UNKNOW);
                if (state == STATE_COLLAPSED) {
                    mContent.setMaxLines(Integer.MAX_VALUE);
                    mQuan.setText("[收起]");
                    mTextStateList.put(position, STATE_EXPANDED);
                } else if (state == STATE_EXPANDED) {
                    mContent.setMaxLines(MAX_LINE_COUNT);
                    mQuan.setText("[全文]");
                    mTextStateList.put(position, STATE_COLLAPSED);
                }
            }
        });
    }
}

package zqx.rj.com.lovecar.utils;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  StaticClass
 * 创建者：  ZQX
 * 创建时间：2018/5/9 19:34
 * 描述：    状态信息
 */

public class StaticClass {

    // 未付款
    public static final String ORDER_UNPAID = "1";
    // 已取消
    public static final String ORDER_CANCEL = "2";
    // 已付款
    public static final String ORDER_ALREADY = "3";
    // 已完成
    public static final String ORDER_FINISH = "4";

    public static final int NETWORK_FAIL = 404;
    
    // 登录状态
    public static final int LOGIN_OUT = 1;
    public static final int LOGIN_FAIL = -2;
    public static final int LOGIN_SUCCESS = 2;

    public static final int IS_FIRST = 1000;
    // 判断程序是否第一次运行
    public static final String SHARE_IS_FIRST = "isFirst";

    public static final int RECEIVE_SUCCESS = 3;
    public static final int UPDATE_SUCCESS = -3;

    public static final int IMAGE_REQUEST_CODE = 4;
    // 头像文件
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 5;
    public static final int RESULT_REQUEST_CODE = 6;
    public static final int RESULT_ICON = 7;
    public static final int REQUEST_ICON = 8;

    public static final int RESULT_CODE_INPUTTIPS = 9;
    public static final int RESULT_CODE_KEYWORDS = 10;

    public static final int ORDER_SUCCESS = 11;

    // 注册信息
    public static final int REGISTER_SUCCESS = 12;
    public static final int REGISTER_ERROR = -12;

    // 搜索信息
    public static final int SEARCH_SUCCESS = 13;
    public static final int SEARCH_FAIL = -13;

    // 开始地点
    public static final int REQUEST_START = 14;
    // 目的地
    public static final int REQUEST_END = 15;

    // 获取车票信息
    public static final int GET_TICKETS_SUCCESS = 16;
    public static final int GET_TICKETS_FAIL = -16;

    // 开启验证码
    public static final int START_TIME = 17;
    public static final int END_TIME = -17;

    // 闪屏页
    public static final int HANDLER_SPLASH = 18;
    
    
    public static final int SAME_PEOPLE_SUC = 19;

    public static final int SAME_PEOPLE_FAIL = -19;

    public static final int GET_LIKE_SUC = 20;
    public static final int GET_LIKE_FAIL = -20;

    public static final int LOAD_FINISH = 21;
    public static final int REFRESH_FINISH = 22;


    public static final int GET_ORDER_SUCCESS = 23;
    public static final int GET_ORDER_EMPTY = -23;
    public static final int DIALOG_SHOW = 24;

    // 删除单个会话
    public static final int DELETE_CODE = 25;


}

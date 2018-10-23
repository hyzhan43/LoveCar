package zqx.rj.com.lovecar.utils;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  API
 * 创建者：  ZQX
 * 创建时间：2018/4/20 17:36
 * 描述：    数据 / 常量
 */

public class API {

    // 轮播图 url
    public static final String BANNER_PIC1 = "http://203.195.193.66/chartered_bus/Public/images/banner/1.jpg";
    public static final String BANNER_PIC2 = "http://203.195.193.66/chartered_bus/Public/images/banner/2.jpg";
    public static final String BANNER_PIC3 = "http://203.195.193.66/chartered_bus/Public/images/banner/3.jpg";
    public static final String BANNER_PIC4 = "http://203.195.193.66/chartered_bus/Public/images/banner/4.jpg";

//    private static final String BASE_URL = "http://203.195.193.66/index.php/Home";

    private static final String BASE_URL = "http://110.64.211.34/Api";

    // 最新路线 url
    public static final String NEW_ROUNTE = BASE_URL + "/Ticket/getTickets";

    // 获取单个车票信息
    public static final String GET_ONE_TICKET = BASE_URL + "/Ticket/getTicket";

    // 搜索车票
    public static final String SEARCH_TICKET = BASE_URL + "/Ticket/searchTicket";

    // 登录操作
    public static final String USER_LOGIN = BASE_URL + "/User/loginUser";

    // 注册操作
    public static final String USER_REGISTER = BASE_URL + "/User/registerUser";

    // 获取验证码
    public static final String SEND_CODE = BASE_URL + "/User/getVerification";

    // 获取用户订单信息  1未付款 2取消 3付款 4完成
    public static final String GET_ORDER = BASE_URL + "/User/getUserOrderInfos";

    // 获取用户订单详情
    public static final String ORDER_DETAIL = BASE_URL + "/User/getOrderInfo";

    // 获取同路人信息列表
    public static final String GET_SAME_PEOPLE = BASE_URL + "/Fellow/getFellowInfos";

    // 获取用户收藏信息
    public static final String GET_LIKE = BASE_URL + "/User/getCollectionInfos";

    // 更新用户头像
    public static final String UPDATE_ICON = BASE_URL + "/User/updateHeadPortrait";

    // 发布车票
    public static final String UPDATE_NEW_TICKETS = BASE_URL + "/Ticket/publishTicket";

    // 获取用户信息
    public static final String GET_USER_INFO = BASE_URL + "/User/getUserInfo";

    // 获取指南列表
    public static final String GET_ARTICLE = BASE_URL + "/Article/getArticleList";
}

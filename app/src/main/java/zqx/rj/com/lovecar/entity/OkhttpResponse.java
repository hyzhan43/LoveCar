package zqx.rj.com.lovecar.entity;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.entity
 * 文件名：  OkhttpResponse
 * 创建者：  ZQX
 * 创建时间：2018/5/10 19:21
 * 描述：    okhttp 返回类
 */

public class OkhttpResponse {

    // 未知错误
    public static final int STATE_UNKNOWN_ERROR = 400;
    // 正常返回
    public static final int STATE_OK = 200;
    // 状态码
    private int code;

    // 数据
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

package zqx.rj.com.lovecar.entity.response;

/**
 * author：  HyZhan
 * created： 2018/10/14 10:16
 * desc：    TODO
 */

public class BaseResponse {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

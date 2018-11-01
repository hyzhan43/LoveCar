package zqx.rj.com.lovecar.entity;

/**
 * author：  HyZhan
 * created： 2018/11/1 14:56
 * desc：    TODO
 */

public class UserInfo {

    private String id;
    private String username;
    private String phone;
    private String auth_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(String auth_status) {
        this.auth_status = auth_status;
    }
}

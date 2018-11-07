package com.topjet.common.user.modle.response;

/**
 * Created by yy on 2017/7/21.
 * 该接口由560配货平台用户【注册用户】时使用 返回类
 */

public class RegisterReponse {
    private String user_id;  //	用户id	是	String	注册后在系统内的id
    private String user_status;  //用户状态	是	String	注册后的用户状态
    private String version;  //	用户数据版本号	是	String	注册后的用户版本号

    public RegisterReponse() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "RegisterReponse{" +
                "user_id='" + user_id + '\'' +
                ", user_status='" + user_status + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

package com.topjet.common.base.net.exception;

/**
 * Created by tsj-004 on 2017/9/12.
 */

public class BaseException extends RuntimeException{
    public String errorCode = null;
    public String errorMsg = null;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

package com.topjet.common.base.net.response;

import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;

import java.io.Serializable;

/**
 * 服务端返回的最外层数据基类
 * Created by tsj004 on 2017/7/31.
 */

public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 3455624598684006501L;

    private String time_stamp;
    private String session_id;
    private String system_code;
    private String system_msg;
    private Result<T> result;

//    public boolean isPerfectRight() {
//        return CConstants.ErrorCode.SUCCESS.equals(system_code);
//    }

    public long getTimeStamp() {
        return StringUtils.getLongToString(time_stamp);
    }

    public void setTimeStamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getSystemCode() {
        return system_code;
    }

    public void setSystemCode(String system_code) {
        this.system_code = system_code;
    }

    public String getSystemMsg() {
        return system_msg;
    }

    public void setSystemMsg(String system_msg) {
        this.system_msg = system_msg;
    }

    public String getSessionId() {
        return session_id;
    }

    public void setSessionId(String session_id) {
        this.session_id = session_id;
    }

    public long getLongDate() {
        return NumberFormatUtils.strToLong(time_stamp);
    }

    public String getCommonDate() {
        long longDate = getLongDate();
        if (longDate != NumberFormatUtils.ERROR_TO_LONG) {
            return TimeUtils.getFormatDate(longDate, TimeUtils.COMMON_TIME_PATTERN);
        }
        return "";
    }

    public Result<T> getResult() {
        return result;
    }

    public void setResult(Result<T> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaseResponse [system_code=" + system_code + ", system_msg=" + system_msg + ", session_id=" + session_id
                + ", time_stamp=" + time_stamp + ", result=" + result + "]";
    }
}

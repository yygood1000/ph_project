package com.topjet.common.base.model;

/**
 * Created by tsj-004 on 2017/8/3.
 */

public class Session {
    private String session_id;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    @Override
    public String toString() {
        return "Session{" +
                "session_id='" + session_id + '\'' +
                '}';
    }
}

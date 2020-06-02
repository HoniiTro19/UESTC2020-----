package com.huidong.legalsys.domain;

/*
 * 用户登录日志
 * phone 登录用户的手机号
 * ip 登录用户的ip地址
 * attempt 用户尝试登录的次数
 * status 用户登录状态
 * timeLatest 用户最后登录的时间
 */

import java.util.Date;

public class Login {
    private String phone;
    private Integer attempt;
    private Integer status;
    private String freezetime;

    @Override
    public String toString(){
        return "LoginLog{" +
                "phone=" + phone +
                ",attempt=" + attempt +
                ",status=" + status +
                ",freezeTime=" + freezetime + "}";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFreezeTime() {
        return freezetime;
    }

    public void setFreezeTime(String freezeTime) {
        this.freezetime = freezeTime;
    }
}
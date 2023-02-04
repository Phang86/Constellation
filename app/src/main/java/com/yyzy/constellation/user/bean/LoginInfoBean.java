package com.yyzy.constellation.user.bean;

public class LoginInfoBean {
    private Integer id;
    private String userName;
    private String loginAddress;
    private String loginTime;
    private String deviceModel;
    private int state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginAddress() {
        return loginAddress;
    }

    public void setLoginAddress(String loginAddress) {
        this.loginAddress = loginAddress;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "LoginInfoBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", loginAddress='" + loginAddress + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", state=" + state +
                '}';
    }
}

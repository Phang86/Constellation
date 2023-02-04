package com.yyzy.constellation.user.feedback.bean;

public class FeedBackBean {
    private Integer id;
    private String userName;
    private String userPhone;
    private String userAddress;
    private String userContent;
    private Integer feedBackState;
    private String feedBackTime;

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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserContent() {
        return userContent;
    }

    public void setUserContent(String userContent) {
        this.userContent = userContent;
    }

    public Integer getFeedBackState() {
        return feedBackState;
    }

    public void setFeedBackState(Integer feedBackState) {
        this.feedBackState = feedBackState;
    }

    public String getFeedBackTime() {
        return feedBackTime;
    }

    public void setFeedBackTime(String feedBackTime) {
        this.feedBackTime = feedBackTime;
    }

    @Override
    public String toString() {
        return "FeedBackBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userContent='" + userContent + '\'' +
                ", feedBackState=" + feedBackState +
                ", feedBackTime='" + feedBackTime + '\'' +
                '}';
    }
}

package com.yyzy.constellation.tally.bean;

public class ChartLvItemBean {
    private int img;
    private String type;
    private float ratio;
    private float sumMoney;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(float sumMoney) {
        this.sumMoney = sumMoney;
    }

    public ChartLvItemBean(int img, String type, float ratio, float sumMoney) {
        this.img = img;
        this.type = type;
        this.ratio = ratio;
        this.sumMoney = sumMoney;
    }

    public ChartLvItemBean() {
    }
}

package com.yyzy.constellation.tally.bean;

public class TallyLvItemBean {
    private int id;
    private String typeName;
    private int img;
    private String beizhu;
    private float money;
    private String time;
    private int year;
    private int month;
    private int day;
    private int outOrIn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getOutOrIn() {
        return outOrIn;
    }

    public void setOutOrIn(int outOrIn) {
        this.outOrIn = outOrIn;
    }

    public TallyLvItemBean(int id, String typeName, int img, String beizhu, float money, String time, int year, int month, int day, int outOrIn) {
        this.id = id;
        this.typeName = typeName;
        this.img = img;
        this.beizhu = beizhu;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.outOrIn = outOrIn;
    }

    public TallyLvItemBean(String typeName, int img, String beizhu, float money, String time, int year, int month, int day, int outOrIn) {
        this.typeName = typeName;
        this.img = img;
        this.beizhu = beizhu;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.outOrIn = outOrIn;
    }

    public TallyLvItemBean() {
    }

    public void setMonth() {
    }

    @Override
    public String toString() {
        return "TallyLvItemBean{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", img=" + img +
                ", beizhu='" + beizhu + '\'' +
                ", money=" + money +
                ", time='" + time + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", outOrIn=" + outOrIn +
                '}';
    }
}

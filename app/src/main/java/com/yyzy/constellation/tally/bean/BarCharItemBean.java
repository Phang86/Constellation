package com.yyzy.constellation.tally.bean;

public class BarCharItemBean {
    private int year;
    private int month;
    private int day;
    private float sumMoney;

    public BarCharItemBean(int year, int month, int day, float sumMoney) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.sumMoney = sumMoney;
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

    public float getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(float sumMoney) {
        this.sumMoney = sumMoney;
    }

    public BarCharItemBean() {
    }
}

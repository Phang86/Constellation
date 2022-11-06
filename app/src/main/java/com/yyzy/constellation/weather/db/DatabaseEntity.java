package com.yyzy.constellation.weather.db;

public class DatabaseEntity {
    private int _id;
    private String city;
    private String content;

    public DatabaseEntity(int _id, String city, String content) {
        this._id = _id;
        this.city = city;
        this.content = content;
    }

    public DatabaseEntity() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DatabaseEntity{" +
                "_id=" + _id +
                ", city='" + city + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

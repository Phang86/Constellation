package com.yyzy.constellation.entity;

public class LocalMusicEntity {
    private String id;          //歌曲id
    private String geShou;      //歌手
    private String geMing;      //歌曲名称
    private String duration;    //歌曲时长
    private String dvd;         //歌曲专辑
    private String path;        //歌曲路径

    public LocalMusicEntity(String id, String geShou, String geMing, String duration, String dvd, String path) {
        this.id = id;
        this.geShou = geShou;
        this.geMing = geMing;
        this.duration = duration;
        this.dvd = dvd;
        this.path = path;
    }

    public LocalMusicEntity() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeShou() {
        return geShou;
    }

    public void setGeShou(String geShou) {
        this.geShou = geShou;
    }

    public String getGeMing() {
        return geMing;
    }

    public void setGeMing(String geMing) {
        this.geMing = geMing;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDvd() {
        return dvd;
    }

    public void setDvd(String dvd) {
        this.dvd = dvd;
    }
}

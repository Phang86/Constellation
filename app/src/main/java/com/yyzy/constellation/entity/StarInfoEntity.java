package com.yyzy.constellation.entity;

import java.io.Serializable;
import java.util.List;

public class StarInfoEntity implements Serializable {

    private List<StarinfoDTO> starinfo;

    public List<StarinfoDTO> getStarinfo() {
        return starinfo;
    }

    public void setStarinfo(List<StarinfoDTO> starinfo) {
        this.starinfo = starinfo;
    }

    public static class StarinfoDTO implements Serializable{
        private String logoname;
        private String name;
        private String date;
        private String td;
        private String gw;
        private String yy;
        private String tz;
        private String zg;
        private String ys;
        private String zb;
        private String hm;
        private String js;
        private String info;

        public String getLogoname() {
            return logoname;
        }

        public void setLogoname(String logoname) {
            this.logoname = logoname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTd() {
            return td;
        }

        public void setTd(String td) {
            this.td = td;
        }

        public String getGw() {
            return gw;
        }

        public void setGw(String gw) {
            this.gw = gw;
        }

        public String getYy() {
            return yy;
        }

        public void setYy(String yy) {
            this.yy = yy;
        }

        public String getTz() {
            return tz;
        }

        public void setTz(String tz) {
            this.tz = tz;
        }

        public String getZg() {
            return zg;
        }

        public void setZg(String zg) {
            this.zg = zg;
        }

        public String getYs() {
            return ys;
        }

        public void setYs(String ys) {
            this.ys = ys;
        }

        public String getZb() {
            return zb;
        }

        public void setZb(String zb) {
            this.zb = zb;
        }

        public String getHm() {
            return hm;
        }

        public void setHm(String hm) {
            this.hm = hm;
        }

        public String getJs() {
            return js;
        }

        public void setJs(String js) {
            this.js = js;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}

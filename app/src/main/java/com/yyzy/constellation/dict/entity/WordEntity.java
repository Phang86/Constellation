package com.yyzy.constellation.dict.entity;

import java.util.List;

public class WordEntity {
    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        private String id;
        private String zi;
        private String py;
        private String wubi;
        private String pinyin;
        private String bushou;
        private String bihua;
        private List<String> jijie;
        private List<String> xiangjie;

        public ResultBean(String id, String zi, String py, String wubi, String pinyin, String bushou, String bihua, List<String> jijie, List<String> xiangjie) {
            this.id = id;
            this.zi = zi;
            this.py = py;
            this.wubi = wubi;
            this.pinyin = pinyin;
            this.bushou = bushou;
            this.bihua = bihua;
            this.jijie = jijie;
            this.xiangjie = xiangjie;
        }

        public ResultBean() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getZi() {
            return zi;
        }

        public void setZi(String zi) {
            this.zi = zi;
        }

        public String getPy() {
            return py;
        }

        public void setPy(String py) {
            this.py = py;
        }

        public String getWubi() {
            return wubi;
        }

        public void setWubi(String wubi) {
            this.wubi = wubi;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getBushou() {
            return bushou;
        }

        public void setBushou(String bushou) {
            this.bushou = bushou;
        }

        public String getBihua() {
            return bihua;
        }

        public void setBihua(String bihua) {
            this.bihua = bihua;
        }

        public List<String> getJijie() {
            return jijie;
        }

        public void setJijie(List<String> jijie) {
            this.jijie = jijie;
        }

        public List<String> getXiangjie() {
            return xiangjie;
        }

        public void setXiangjie(List<String> xiangjie) {
            this.xiangjie = xiangjie;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "id='" + id + '\'' +
                    ", zi='" + zi + '\'' +
                    ", py='" + py + '\'' +
                    ", wubi='" + wubi + '\'' +
                    ", pinyin='" + pinyin + '\'' +
                    ", bushou='" + bushou + '\'' +
                    ", bihua='" + bihua + '\'' +
                    ", jijie=" + jijie +
                    ", xiangjie=" + xiangjie +
                    '}';
        }
    }
}

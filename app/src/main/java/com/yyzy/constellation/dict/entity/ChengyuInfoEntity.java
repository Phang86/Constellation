package com.yyzy.constellation.dict.entity;

import java.util.List;

public class ChengyuInfoEntity {


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
        private String name;
        private String pinyin;
        private List<String> jbsy;
        private List<String> xxsy;
        private String chuchu;
        private String liju;
        private List<String> jyc;
        private List<String> fyc;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public List<String> getJbsy() {
            return jbsy;
        }

        public void setJbsy(List<String> jbsy) {
            this.jbsy = jbsy;
        }

        public List<String> getXxsy() {
            return xxsy;
        }

        public void setXxsy(List<String> xxsy) {
            this.xxsy = xxsy;
        }

        public String getChuchu() {
            return chuchu;
        }

        public void setChuchu(String chuchu) {
            this.chuchu = chuchu;
        }

        public String getLiju() {
            return liju;
        }

        public void setLiju(String liju) {
            this.liju = liju;
        }

        public List<String> getJyc() {
            return jyc;
        }

        public void setJyc(List<String> jyc) {
            this.jyc = jyc;
        }

        public List<String> getFyc() {
            return fyc;
        }

        public void setFyc(List<String> fyc) {
            this.fyc = fyc;
        }
    }
}

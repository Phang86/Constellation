package com.yyzy.constellation.dict.entity;

import java.util.List;

public class PinBuEntity {

    private String reason;
    private List<ResultDTO> result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ResultDTO> getResult() {
        return result;
    }

    public void setResult(List<ResultDTO> result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultDTO {
        private String id;
        private String pinyin_key;
        private String pinyin;
        private String bihua;
        private String bushou;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPinyin_key() {
            return pinyin_key;
        }

        public void setPinyin_key(String pinyin_key) {
            this.pinyin_key = pinyin_key;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getBihua() {
            return bihua;
        }

        public void setBihua(String bihua) {
            this.bihua = bihua;
        }

        public String getBushou() {
            return bushou;
        }

        public void setBushou(String bushou) {
            this.bushou = bushou;
        }

        @Override
        public String toString() {
            return "ResultDTO{" +
                    "id='" + id + '\'' +
                    ", pinyin_key='" + pinyin_key + '\'' +
                    ", pinyin='" + pinyin + '\'' +
                    ", bihua='" + bihua + '\'' +
                    ", bushou='" + bushou + '\'' +
                    '}';
        }
    }


}

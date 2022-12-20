package com.yyzy.constellation.entity;

import java.util.List;

public class RandomImgBean {

    private int code;
    private String msg;
    private List<DataDTO> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        private String imageUrl;
        private String imageSize;
        private int imageFileLength;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageSize() {
            return imageSize;
        }

        public void setImageSize(String imageSize) {
            this.imageSize = imageSize;
        }

        public int getImageFileLength() {
            return imageFileLength;
        }

        public void setImageFileLength(int imageFileLength) {
            this.imageFileLength = imageFileLength;
        }

        @Override
        public String toString() {
            return "DataDTO{" +
                    "imageUrl='" + imageUrl + '\'' +
                    ", imageSize='" + imageSize + '\'' +
                    ", imageFileLength=" + imageFileLength +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RandomImgBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

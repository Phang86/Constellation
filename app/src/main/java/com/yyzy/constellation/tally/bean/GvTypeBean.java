package com.yyzy.constellation.tally.bean;

public class GvTypeBean {
    private int id;
    private String typeName;
    private int selectImg;  //选中时图片
    private int unSelectImg; //未选中时图片
    private int outOrIn;  //0：来自支出，1：来自收入

    public GvTypeBean(int id, String typeName, int selectImg, int unSelectImg, int outOrIn) {
        this.id = id;
        this.typeName = typeName;
        this.selectImg = selectImg;
        this.unSelectImg = unSelectImg;
        this.outOrIn = outOrIn;
    }

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

    public int getSelectImg() {
        return selectImg;
    }

    public void setSelectImg(int selectImg) {
        this.selectImg = selectImg;
    }

    public int getUnSelectImg() {
        return unSelectImg;
    }

    public void setUnSelectImg(int unSelectImg) {
        this.unSelectImg = unSelectImg;
    }

    public int getOutOrIn() {
        return outOrIn;
    }

    public void setOutOrIn(int outOrIn) {
        this.outOrIn = outOrIn;
    }

    public GvTypeBean() {
    }
}

package com.cgbsoft.lib.base.model.bean;

/**
 * @author chenlong
 */
public class BannerBean {

    public enum ViewType { // 横向或者圆点
        OVAL, RECTANGLE
    }

    private String ImageUrl;

    private String jumpUrl; // 跳转的Url

    private String title; // 标题

    private ViewType vierType = ViewType.OVAL; // 默认圆点

    private boolean isVideo; // 默认是图片， true表示是视频

    private int positon;

    public BannerBean(boolean isVideo, String imageUrl, ViewType vierType) {
        this.isVideo = isVideo;
        ImageUrl = imageUrl;
        this.vierType = vierType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public ViewType getVierType() {
        return vierType;
    }

    public void setVierType(ViewType vierType) {
        this.vierType = vierType;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}

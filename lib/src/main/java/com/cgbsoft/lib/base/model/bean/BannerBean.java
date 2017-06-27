package com.cgbsoft.lib.base.model.bean;

/**
 * @author chenlong
 */

public class BannerBean {

    private String ImageUrl;

    private String vierType; // 横向或者远点

    private int positon;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getVierType() {
        return vierType;
    }

    public void setVierType(String vierType) {
        this.vierType = vierType;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }
}

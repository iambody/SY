package com.cgbsoft.privatefund.bean.commui;

import java.io.Serializable;
import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class WebRightTopViewConfigBean implements Serializable {

    private int landscape;//1是横屏
    private int rightButtonsType; //1只显示文字 2是只显示图片（可能是多个图标需要判断数组长度）
    private List<RightBt> rightButtons;
    private boolean hideReturnButton;
    private String openWebUrl;

    public int getLandscape() {
        return landscape;
    }

    public void setLandscape(int landscape) {
        this.landscape = landscape;
    }

    public int getRightButtonType() {
        return rightButtonsType;
    }

    public void setRightButtonType(int rightButtonType) {
        this.rightButtonsType = rightButtonType;
    }

    public List<RightBt> getRightButtons() {
        return rightButtons;
    }

    public void setRightButtons(List<RightBt> rightButtons) {
        this.rightButtons = rightButtons;
    }

    public String getOpenWebUrl() {
        return openWebUrl;
    }

    public void setOpenWebUrl(String openWebUrl) {
        this.openWebUrl = openWebUrl;
    }

    public boolean isHideReturnButton() {
        return hideReturnButton;
    }

    public void setHideReturnButton(boolean hideReturnButton) {
        this.hideReturnButton = hideReturnButton;
    }
}

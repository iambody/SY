package app.privatefund.com.im.bean;

import java.io.Serializable;

/**
 * Created by lee on 2016/9/18.
 */
public class SMMessage implements Serializable {
    String jumpUrl;
    String dialogTitle;
    String showType;
    String receiverType;
    String dialogSummary;
    String content;
    String shareType;

    private boolean displayH5;
    private String buttonText;
    private String linkText;
    private String linkJumpUrl;
    private String linkTitle;
    private String buttonTitle;

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getDialogSummary() {
        return dialogSummary;
    }

    public void setDialogSummary(String dialogSummary) {
        this.dialogSummary = dialogSummary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public boolean isDisplayH5() {
        return displayH5;
    }

    public void setDisplayH5(boolean displayH5) {
        this.displayH5 = displayH5;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkJumpUrl() {
        return linkJumpUrl;
    }

    public void setLinkJumpUrl(String linkJumpUrl) {
        this.linkJumpUrl = linkJumpUrl;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }
}

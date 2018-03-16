package com.cgbsoft.privatefund.bean.commui;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class JsShareResourceBean {
    String text;
    String title;//: '测试测试',
    String conten;//: location.href, //分享html路径,
    String  pageURL;//
    String image;//可能是流或者是其他: 'https://d8-app.simuyun.com/awx/v2/share/png?param=%7B%22wxaType%22:%220%22,%22path%22:%22/pages/detailI/detailI?id=67cc8a8a3e3745b8af8c4c490a8b342b%26category=9%22%7D', //图片只支持全路径

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConten() {
        return conten;
    }

    public void setConten(String conten) {
        this.conten = conten;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }
}

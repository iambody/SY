package com.cgbsoft.privatefund.bean.commui;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class OpenWebBean {

    String title;//":"不正经",
    String HTMLTitle;//":"<span style="font-size: 18px; color: #ff0000;">主标题大字</span><br><span style="font-size: 14px; color: #ffffff;">副标题文字</span>",
    String autoTitle;//":true,
    String landscape;//":0,
    //    String navigationBar;//":{
    String barTextStyle;//":"#ff0000",
    String backgroundColor;//":"transparent"
    String ignoreMarginTop;//":true,
    String URL;//":"testsharing1.html"
    NavigationBar navigationBar;

    public void setNavigationBar(NavigationBar navigationBar) {
        this.navigationBar = navigationBar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHTMLTitle() {
        return HTMLTitle;
    }

    public void setHTMLTitle(String HTMLTitle) {
        this.HTMLTitle = HTMLTitle;
    }

    public String getAutoTitle() {
        return autoTitle;
    }

    public void setAutoTitle(String autoTitle) {
        this.autoTitle = autoTitle;
    }

    public String getLandscape() {
        return landscape;
    }

    public void setLandscape(String landscape) {
        this.landscape = landscape;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public String getBarTextStyle() {
        return barTextStyle;
    }

    public void setBarTextStyle(String barTextStyle) {
        this.barTextStyle = barTextStyle;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getIgnoreMarginTop() {
        return ignoreMarginTop;
    }

    public void setIgnoreMarginTop(String ignoreMarginTop) {
        this.ignoreMarginTop = ignoreMarginTop;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    class NavigationBar {
        public String barTextStyle;//":"#ff0000",
        public String backgroundColor;//":"transparent"

        public String getBarTextStyle() {
            return barTextStyle;
        }

        public void setBarTextStyle(String barTextStyle) {
            this.barTextStyle = barTextStyle;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
    }
}

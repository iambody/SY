package com.cgbsoft.privatefund.bean.commui;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class OpenWebBean {
    boolean hasHTMLTag;
    String title;//":"不正经",
    String autoTitle;//":true,
    //    String landscape;//":0,
    //    String navigationBar;//":{
    String barTextStyle;//":"#ff0000",
    String backgroundColor;//":"transparent"
    String ignoreMarginTop;//":true,
    String URL;//":"testsharing1.html"
    int switchTab = -1;
    NavigationBar navigationBar;

    public int getSwitchTab() {
        return switchTab;
    }

    public void setSwitchTab(int switchTab) {
        this.switchTab = switchTab;
    }

    public void setNavigationBar(NavigationBar navigationBar) {
        this.navigationBar = navigationBar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasHTMLTag() {
        return hasHTMLTag;
    }

    public void setHasHTMLTag(boolean hasHTMLTag) {
        this.hasHTMLTag = hasHTMLTag;
    }

    public String getAutoTitle() {
        return autoTitle;
    }

    public void setAutoTitle(String autoTitle) {
        this.autoTitle = autoTitle;
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

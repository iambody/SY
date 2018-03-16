package com.cgbsoft.privatefund.bean.commui;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class JsShareBean {
    String typ; //1:html（default）, 2:image, 3:miniprogram, 4:text, 5:video, 6:audio
    JsShareResourceBean resource;

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public JsShareResourceBean getResource() {
        return resource;
    }

    public void setResource(JsShareResourceBean resource) {
        this.resource = resource;
    }
}

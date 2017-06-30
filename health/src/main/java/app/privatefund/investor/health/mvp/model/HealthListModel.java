package app.privatefund.investor.health.mvp.model;

import com.cgbsoft.lib.widget.recycler.BaseModel;

import java.io.Serializable;

/**
 * @author chenlong
 */
public class HealthListModel extends BaseModel implements Serializable{

    public static final int BOTTOM = 1;

    private String id;

    private String code;

    private String title;

    private String imageUrl;

    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.cgbsoft.privatefund.bean.share;

import com.cgbsoft.privatefund.bean.BaseBean;

/**
 * desc  资讯bean
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/5-17:24
 */
public class NewsBean extends BaseBean {

    private String summary;

    private String category;

    private String title;

    private int likes;

    private String infoId;

//	private String date;
//
//	private String url;

    private String isLike;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }
}

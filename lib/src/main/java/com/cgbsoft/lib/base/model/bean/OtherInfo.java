package com.cgbsoft.lib.base.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 存储其他一些信息
 * Created by xiaoyu.zhang on 2016/11/24 09:58
 * Email:zhangxyfs@126.com
 *  
 */
@Entity
public class OtherInfo {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String title;
    private String content;

    @Generated(hash = 2053004557)
    public OtherInfo(Long id, @NotNull String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Generated(hash = 2064648337)
    public OtherInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

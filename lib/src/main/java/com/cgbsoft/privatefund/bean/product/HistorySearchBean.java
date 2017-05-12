package com.cgbsoft.privatefund.bean.product;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/12-10:39
 */
@Entity
public class HistorySearchBean {
    @Id(autoincrement = true)
    private long _id;
    private String id;

    private String name;

    private String type;

    private long time;

    private String userId;





    @Generated(hash = 1760155482)
    public HistorySearchBean(long _id, String id, String name, String type,
            long time, String userId) {
        this._id = _id;
        this.id = id;
        this.name = name;
        this.type = type;
        this.time = time;
        this.userId = userId;
    }

    @Generated(hash = 954352461)
    public HistorySearchBean() {
    }


    public HistorySearchBean(String id, String name, String type, long time, String userId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.time = time;
        this.userId = userId;
    }

    public long get_id() {
        return this._id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}

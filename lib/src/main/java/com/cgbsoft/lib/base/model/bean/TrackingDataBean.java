package com.cgbsoft.lib.base.model.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * desc  ${DESC}
 * author yangzonghui  yangzonghui@simuyun.com
 * 日期 2017/11/29-下午3:16
 */
@Entity
public class TrackingDataBean {
    @Id(autoincrement = true)
    Long _id;
    String e;
    Long t;
    String d;

    public TrackingDataBean(String e, Long t, String d) {
        this.e = e;
        this.t = t;
        this.d = d;
    }

    @Generated(hash = 1197713646)
    public TrackingDataBean(Long _id, String e, Long t, String d) {
        this._id = _id;
        this.e = e;
        this.t = t;
        this.d = d;
    }

    @Generated(hash = 709011296)
    public TrackingDataBean() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}

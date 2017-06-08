package com.cgbsoft.lib.base.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.json.JSONObject;
import org.greenrobot.greendao.annotation.Generated;

/**
 * desc
 * Created by yangzonghui on 2017/6/7 15:50
 * Email:yangzonghui@simuyun.com
 *  
 */
@Entity
public class DataStatisticsBean {
    @Id
    private Long id;
    private String time;
    private String jsonObject;


    @Generated(hash = 632278757)
    public DataStatisticsBean(Long id, String time, String jsonObject) {
        this.id = id;
        this.time = time;
        this.jsonObject = jsonObject;
    }

    @Generated(hash = 333109208)
    public DataStatisticsBean() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }
}

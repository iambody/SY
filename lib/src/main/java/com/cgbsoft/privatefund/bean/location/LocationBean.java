package com.cgbsoft.privatefund.bean.location;

import com.cgbsoft.lib.base.model.bean.BaseBean;

/**
 * desc
 * author
 * 日期 2017/6/9-16:44
 */
public class LocationBean extends BaseBean {
    //纬度
    private double locationlatitude;
    //经度
    private double locationlontitude;
    //城市
    private String locationcity;


    public LocationBean() {
    }

    public LocationBean(double locationlatitude, double locationlontitude, String locationcity) {
        this.locationlatitude = locationlatitude;
        this.locationlontitude = locationlontitude;
        this.locationcity = locationcity;
    }

    public double getLocationlatitude() {
        return locationlatitude;
    }

    public void setLocationlatitude(double locationlatitude) {
        this.locationlatitude = locationlatitude;
    }

    public double getLocationlontitude() {
        return locationlontitude;
    }

    public void setLocationlontitude(double locationlontitude) {
        this.locationlontitude = locationlontitude;
    }

    public String getLocationcity() {
        return locationcity;
    }

    public void setLocationcity(String locationcity) {
        this.locationcity = locationcity;
    }
}

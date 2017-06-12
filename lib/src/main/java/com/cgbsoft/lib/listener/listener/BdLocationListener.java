package com.cgbsoft.lib.listener.listener;

import com.cgbsoft.privatefund.bean.location.LocationBean;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/9-16:25
 */
public interface BdLocationListener {
    /**
     * 获取定位成功
     */
    public void getLocation(LocationBean locationBean);

    /**
     * 获取定位失败
     */
    public void getLocationerror();

}

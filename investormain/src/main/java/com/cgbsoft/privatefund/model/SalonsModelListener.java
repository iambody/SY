package com.cgbsoft.privatefund.model;

import com.cgbsoft.lib.base.model.SalonsEntity;

import java.util.List;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public interface SalonsModelListener {
    void getDataSuccess(List<SalonsEntity.SalonItemBean> salons, List<SalonsEntity.CityBean> citys);
    void getDataError(Throwable error);
}

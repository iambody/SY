package com.cgbsoft.privatefund.model;

import com.cgbsoft.lib.base.model.OldSalonsEntity;

import java.util.List;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public interface OldSalonsModelListener {
    void getDataSuccess( List<OldSalonsEntity.SalonItemBean> oldSalons);
    void getDataError(Throwable error);
}

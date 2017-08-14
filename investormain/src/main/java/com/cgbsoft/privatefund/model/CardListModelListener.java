package com.cgbsoft.privatefund.model;

import com.cgbsoft.lib.base.model.CardListEntity;

import java.util.List;

/**
 * Created by fei on 2017/8/10.
 */

public interface CardListModelListener {
    void getDataSuccess( List<CardListEntity.CardBean> cards);
    void getDataError(Throwable error);
}

package com.cgbsoft.privatefund.model;

import com.cgbsoft.lib.base.model.IndentityEntity;

import java.util.List;

/**
 * Created by fei on 2017/8/11.
 */

public interface IndentityModelListener {
    void getIndentityListSuccess( List<IndentityEntity.IndentityBean> indentityBeen);
    void getIndentityListError(Throwable error);
}

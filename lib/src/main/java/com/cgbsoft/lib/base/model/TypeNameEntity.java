package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * @author chenlong
 *
 * 通用的接受数据的模型
 */
public class TypeNameEntity extends BaseResult<TypeNameEntity.Result> {

    public static class Result{
        public String type;
    }
}

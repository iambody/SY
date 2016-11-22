package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * Created by xiaoyu.zhang on 2016/11/22 09:22
 * Email:zhangxyfs@126.com
 *  
 */
public class WXUnionIDCheckEntity extends BaseResult<WXUnionIDCheckEntity.Result> {
    public Result result;

    public static class Result {
        /**
         * isExist : 1
         */

        public String isExist;//是否存在判断结果,"0":该unionId不存在     "1":该unionId存在
    }
}

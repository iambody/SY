package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * @author chenlong
 *
 *
 */
public class UserPhoneNumEntity extends BaseResult<UserPhoneNumEntity.Result> {

    public static class Result{
        public String phoneNumber;
    }
}

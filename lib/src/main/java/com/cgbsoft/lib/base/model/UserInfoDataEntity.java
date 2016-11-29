package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * 用户信息
 * Created by xiaoyu.zhang on 2016/11/11 13:24
 * Email:zhangxyfs@126.com
 *  
 */
public class UserInfoDataEntity extends BaseResult<UserInfoDataEntity.Result> {

    public static class Result {
        public String token;
        public String userId;
        public String isBind;
        public UserInfo userInfo;
    }
}

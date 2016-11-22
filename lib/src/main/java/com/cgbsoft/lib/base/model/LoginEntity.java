package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * Created by xiaoyu.zhang on 2016/11/22 09:28
 * Email:zhangxyfs@126.com
 *  
 */
public class LoginEntity extends BaseResult<LoginEntity.Result> {
    public Result result;

    public static class Result {
        public String token;
        public String userId;
        public UserInfo userInfo;
    }
}

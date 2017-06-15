package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * 应用资源
 * Created by xiaoyu.zhang on 2016/11/16 18:01
 * Email:zhangxyfs@126.com
 *  
 */
public class AppResourcesEntity extends BaseResult<AppResourcesEntity.Result> {

    public static class Result {

        /**
         * img34 :
         * img916 :
         * version :
         * adverts :
         * downUrl :
         * isMustUpdate : n
         * versionFlag : 1
         * isStrict : 1
         */

        public String img34;
        public String img916;
        public String version;
        public String adverts;
        public String downUrl;
        public String isMustUpdate;
        public String versionFlag;
        public String isStrict;
        public int upgradeType;
    }
}

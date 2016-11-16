package com.cgbsoft.lib.utils.net;

import com.cgbsoft.lib.base.model.AppResourcesEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 *  * Created by xiaoyu.zhang on 2016/11/7 16:14
 *  
 */
public interface RequestManager {

    /**
     * 获取资源
     *
     * @return
     */
    @GET(NetConfig.GET_RES_URL)
    Observable<AppResourcesEntity> getAppResource(@QueryMap Map<String, String> map);
}

package com.cgbsoft.lib.utils.net;

import com.cgbsoft.lib.base.model.DataStatisticsEntity;
import com.cgbsoft.lib.base.model.bean.AppResources;
import com.cgbsoft.lib.base.model.bean.LoginBean;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 *  * Created by xiaoyu.zhang on 2016/11/7 16:14
 *  
 */
interface RequestManager {

    /**
     * 获取资源
     *
     * @return
     */
    @GET(NetConfig.GET_RES_URL)
    Observable<BaseResult<AppResources>> getAppResource(@QueryMap Map<String, String> map);

    /**
     * 数据统计
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.DATASTATISTICS_URL)
    Observable<DataStatisticsEntity> pushDataStatistics(@FieldMap() Map<String, String> paramsMap);

    /**
     * 获取ip
     *
     * @param map
     * @return
     */
    @GET(NetConfig.GETIP_URL)
    Observable<BaseResult<String>> getIP(@QueryMap Map<String, String> map);

    /**
     * 登录
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.LOGIN_URL)
    Observable<BaseResult<LoginBean>> toLogin(@FieldMap() Map<String, String> paramsMap);

    /**
     * 登录
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.GET_USERINFO_URL)
    Observable<BaseResult<UserInfo>> getUserInfo(@FieldMap() Map<String, String> paramsMap);
}

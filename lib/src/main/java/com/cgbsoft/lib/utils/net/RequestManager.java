package com.cgbsoft.lib.utils.net;

import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.WXUnionIDCheckEntity;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.Map;

import okhttp3.ResponseBody;
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
    Observable<BaseResult<AppResourcesEntity.Result>> getAppResource(@QueryMap Map<String, String> map);

    /**
     * 数据统计
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.DATASTATISTICS_URL)
    Observable<BaseResult<String>> pushDataStatistics(@FieldMap() Map<String, String> paramsMap);

    /**
     * 获取ip
     *
     * @param map
     * @return
     */
    @GET(NetConfig.GETIP_URL)
    Observable<ResponseBody> getIP(@QueryMap Map<String, String> map);

    /**
     * 登录
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.LOGIN_URL)
    Observable<BaseResult<UserInfoDataEntity.Result>> toLogin(@FieldMap() Map<String, String> paramsMap);

    /**
     * 获取用户信息
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.GET_USERINFO_URL)
    Observable<BaseResult<UserInfo>> getUserInfo(@FieldMap() Map<String, String> paramsMap);

    /**
     * 获取融云token
     *
     * @param map
     * @return
     */
    @GET(NetConfig.GET_RONG_TOKEN)
    Observable<BaseResult<RongTokenEntity.Result>> getRongToken(@QueryMap Map<String, String> map);

    /**
     * 微信 unioid 验证
     *
     * @param map
     * @return
     */
    @GET(NetConfig.USER.WX_UNIONID_CHECK)
    Observable<BaseResult<WXUnionIDCheckEntity.Result>> wxUnioIDCheck(@QueryMap Map<String, String> map);

    /**
     * 微信登陆
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.WX_LOGIN_URL)
    Observable<BaseResult<UserInfoDataEntity.Result>> toWxLogin(@FieldMap Map<String, String> paramsMap);

    /**
     * 获取协议
     * @return
     */
    @GET(NetConfig.USERAGENT_URL)
    Observable<ResponseBody> getProtocol();

    /**
     * 注册
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.REGISTER_URL)
    Observable<BaseResult<UserInfoDataEntity.Result>> toRegister(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.SENDCODE_URL)
    Observable<BaseResult<String>>sendCode(@FieldMap Map<String, String> paramsMap);

}

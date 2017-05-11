package com.cgbsoft.lib.utils.net;

import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.CollegeVideoEntity;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.SignInEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.model.VideoLikeEntity;
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


    @GET(NetConfig.GET_RES_URL)
    Observable<ResponseBody> getTestAppResource(@QueryMap Map<String, String> map);

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


    @FormUrlEncoded
    @POST(NetConfig.LOGIN_URL)
    Observable<ResponseBody> toTestLogin(@FieldMap() Map<String, String> paramsMap);
    /**
     * 获取用户信息
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.GET_USERINFO_URL)
    Observable<BaseResult<UserInfo>> getUserInfo(@FieldMap() Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.GET_USERINFO_URL)
    Observable<ResponseBody> getTestUserInfo(@FieldMap() Map<String, String> paramsMap);

    /**
     * 获取融云token
     *
     * @param map
     * @return
     */
    @GET(NetConfig.GET_RONG_TOKEN)
    Observable<BaseResult<RongTokenEntity.Result>> getRongToken(@QueryMap Map<String, String> map);

    @GET(NetConfig.GET_RONG_TOKEN)
    Observable<ResponseBody> getTestRongToken(@QueryMap Map<String, String> map);

    /**
     * 微信 unioid 验证
     *
     * @param map
     * @return
     */
    @GET(NetConfig.USER.WX_UNIONID_CHECK)
    Observable<BaseResult<WXUnionIDCheckEntity.Result>> wxUnioIDCheck(@QueryMap Map<String, String> map);

    @GET(NetConfig.USER.WX_UNIONID_CHECK)
    Observable<ResponseBody> wxTestUnioIDCheck(@QueryMap Map<String, String> map);

    /**
     * 微信登录
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.WX_LOGIN_URL)
    Observable<BaseResult<UserInfoDataEntity.Result>> toWxLogin(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.WX_LOGIN_URL)
    Observable<ResponseBody> toTestWxLogin(@FieldMap Map<String, String> paramsMap);

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
    @POST(NetConfig.USER.REGISTER_URL)
    Observable<ResponseBody> toTestRegister(@FieldMap Map<String, String> paramsMap);

    /**
     * 发送验证码
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.SENDCODE_URL)
    Observable<BaseResult<String>>sendCode(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.SENDCODE_URL)
    Observable<ResponseBody>sendTestCode(@FieldMap Map<String, String> paramsMap);

    /**
     * 验证验证码
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.CHECKCODE_URL)
    Observable<BaseResult<String>>checkCode(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.CHECKCODE_URL)
    Observable<ResponseBody>checkTestCode(@FieldMap Map<String, String> paramsMap);

    /**
     * 重置密码
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.RESETPWD_URL)
    Observable<BaseResult<String>>resetPwd(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.RESETPWD_URL)
    Observable<ResponseBody>resetTestPwd(@FieldMap Map<String, String> paramsMap);

    /**
     * 修改密码
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.MODIFY_PASSWORD_URL)
    Observable<BaseResult<String>>modifyPassword(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.MODIFY_PASSWORD_URL)
    Observable<ResponseBody>modifyTestPassword(@FieldMap Map<String, String> paramsMap);

    /**
     * 合并帐号--验证手机
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.WXMERGECHECK_URL)
    Observable<BaseResult<String>>wxMergePhone(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.WXMERGECHECK_URL)
    Observable<ResponseBody>wxTestMergePhone(@FieldMap Map<String, String> paramsMap);

    /**
     * 合并帐号--确认合并
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.WXMARGECONFIRM_URL)
    Observable<BaseResult<String>>wxMergeConfirm();

    @FormUrlEncoded
    @POST(NetConfig.USER.WXMARGECONFIRM_URL)
    Observable<ResponseBody>wxTestMergeConfirm();

    /**
     * 获取学院推荐数据
     * @return
     */
    @GET(NetConfig.INFORMATION.GET_COLLEGE_RECOMMEND_VIDEO)
    Observable<BaseResult<CollegeVideoEntity.Result>> getCollegeHeadList();

    @GET(NetConfig.INFORMATION.GET_COLLEGE_RECOMMEND_VIDEO)
    Observable<ResponseBody> getTestCollegeHeadList();

    /**
     * 获取学院其他数据
     * @return
     */
    @GET(NetConfig.INFORMATION.GET_COLLEGE_OTHER_VIDEO)
    Observable<BaseResult<CollegeVideoEntity.Result>> getCollegeOtherList(@QueryMap Map<String, String> map);

    @GET(NetConfig.INFORMATION.GET_COLLEGE_OTHER_VIDEO)
    Observable<ResponseBody> getTestCollegeOtherList(@QueryMap Map<String, String> map);

    /**
     * 获取视频详情
     * @param map
     * @return
     */
    @GET(NetConfig.INFORMATION.GET_VIDEO_INFO)
    Observable<BaseResult<VideoInfoEntity.Result>> getVideoInfo(@QueryMap Map<String, String> map);

    @GET(NetConfig.INFORMATION.GET_VIDEO_INFO)
    Observable<ResponseBody> getTestVideoInfo(@QueryMap Map<String, String> map);

    /**
     * 点赞
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.INFORMATION.TO_LIKE_VIDEO)
    Observable<BaseResult<VideoLikeEntity.Result>> toVideoLike(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.INFORMATION.TO_LIKE_VIDEO)
    Observable<ResponseBody> toTestVideoLike(@FieldMap Map<String, String> paramsMap);

    /**
     * 签到
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.SIGNIN_URL)
    Observable<BaseResult<SignInEntity.Result>> signIn(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.SIGNIN_URL)
    Observable<ResponseBody> testSignIn(@FieldMap Map<String, String> paramsMap);

    //获取产品筛选条件
    @GET(CNetConfig.PRODUCT.Get_PRODUCT_TAG)
    Observable<ResponseBody> getProductFilter();


    //获取产品列表数据

    @GET(CNetConfig.PRODUCT.Get_PRODUCTLS_TAG)
    Observable<ResponseBody> getProductls(@QueryMap Map<String, String> paramsMap);
}

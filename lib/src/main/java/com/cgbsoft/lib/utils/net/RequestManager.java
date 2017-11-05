package com.cgbsoft.lib.utils.net;

import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.base.model.CollegeVideoEntity;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.model.ElegantGoodsEntity;
import com.cgbsoft.lib.base.model.ElegantLivingEntity;
import com.cgbsoft.lib.base.model.GroupInfoEntity;
import com.cgbsoft.lib.base.model.GroupListEntity;
import com.cgbsoft.lib.base.model.GroupMemberEntity;
import com.cgbsoft.lib.base.model.GroupMemberNewEntity;
import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.model.IndentityEntity;
import com.cgbsoft.lib.base.model.OldSalonsEntity;
import com.cgbsoft.lib.base.model.OrgManagerEntity;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.RongUserEntity;
import com.cgbsoft.lib.base.model.SalonsEntity;
import com.cgbsoft.lib.base.model.SignInEntity;
import com.cgbsoft.lib.base.model.TypeNameEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.UserPhoneNumEntity;
import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.model.VideoLikeEntity;
import com.cgbsoft.lib.base.model.WXUnionIDCheckEntity;
import com.cgbsoft.lib.base.mvp.model.BaseResult;
import com.cgbsoft.privatefund.bean.living.IdentityCard;
import com.cgbsoft.privatefund.bean.living.LivingResultData;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
     * V2
     */
    @POST(NetConfig.AUTHOR.LOGIN_V2_URL)
    Observable<ResponseBody> toTestV2Login(@Body RequestBody paramsMap);

    /**
     * 获取用户信息
     *
     * @param
     * @return
     */
    @GET(NetConfig.USER.GET_USERINFO_URL)
    Observable<BaseResult<UserInfoDataEntity.UserInfo>> getUserInfo(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST(NetConfig.USER.GET_USERINFO_URL)
    Observable<ResponseBody> getTestUserInfo(@FieldMap() Map<String, String> paramsMap);

    /**
     * 获取融云token
     *
     * @param map
     * @return
     */
    @GET(NetConfig.Auth.GET_RONG_TOKEN)
    Observable<BaseResult<RongTokenEntity.Result>> getRongToken(@QueryMap Map<String, String> map);

    @GET(NetConfig.Auth.GET_RONG_TOKEN)
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
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.WX_LOGIN_URL)
    Observable<BaseResult<UserInfoDataEntity.Result>> toWxLogin(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.WX_LOGIN_URL)
    Observable<ResponseBody> toTestWxLogin(@FieldMap Map<String, String> paramsMap);


    @POST(NetConfig.USER.WX_LOGIN_URL)
    Observable<ResponseBody> toV2WxLogin(@Body RequestBody paramsbody);

    /**
     * 获取协议
     *
     * @return
     */
    @GET(NetConfig.USERAGENT_URL)
    Observable<ResponseBody> getProtocol();

    /**
     * 注册
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.REGISTER_URL)
    Observable<BaseResult<UserInfoDataEntity.Result>> toRegister(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.REGISTER_URL)
    Observable<ResponseBody> toTestRegister(@FieldMap Map<String, String> paramsMap);


    @POST(NetConfig.USER.REGISTER_URL)
    Observable<ResponseBody> toV2Register(@Body RequestBody paramsMap);

    /**
     * 发送验证码
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.SENDCODE_URL)
    Observable<BaseResult<String>> sendCode(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.SENDCODE_URL)
    Observable<ResponseBody> sendTestCode(@FieldMap Map<String, String> paramsMap);

    @POST(NetConfig.USER.SENDCODE_URL)
    Observable<ResponseBody> sendV2Code(@Body RequestBody paramsMap);

    /**
     * 验证验证码
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.CHECKCODE_URL)
    Observable<BaseResult<String>> checkCode(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.CHECKCODE_URL)
    Observable<ResponseBody> checkTestCode(@FieldMap Map<String, String> paramsMap);


    @POST(NetConfig.USER.CHECKCODE_URL)
    Observable<ResponseBody> checkV2Code(@Body RequestBody paramsMap);

    /**
     * 重置密码
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.RESETPWD_URL)
    Observable<BaseResult<String>> resetPwd(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.RESETPWD_URL)
    Observable<ResponseBody> resetTestPwd(@FieldMap Map<String, String> paramsMap);


    @POST(NetConfig.USER.RESETPWD_URL)
    Observable<ResponseBody> resetV2Pwd(@Body RequestBody paramsMap);

    /**
     * 修改密码
     *
     * @param
     * @return
     */
    @POST(NetConfig.USER.MODIFY_PASSWORD_URL)
    Observable<BaseResult<CommonEntity.Result>> modifyPassword(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST(NetConfig.USER.MODIFY_PASSWORD_URL)
    Observable<ResponseBody> modifyTestPassword(@FieldMap Map<String, String> paramsMap);

    /**
     * 合并帐号--验证手机
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.WXMERGECHECK_URL)
    Observable<BaseResult<String>> wxMergePhone(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetConfig.USER.WXMERGECHECK_URL)
    Observable<ResponseBody> wxTestMergePhone(@FieldMap Map<String, String> paramsMap);

    @POST(NetConfig.USER.WXMERGECHECK_URL)
    Observable<ResponseBody> wxV2MergePhone(@Body RequestBody paramsMap);

    /**
     * 合并帐号--确认合并
     *
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.WXMARGECONFIRM_URL)
    Observable<BaseResult<String>> wxMergeConfirm();


    //    @FormUrlEncoded
    @POST(NetConfig.USER.WXMARGECONFIRM_URL)
    Observable<ResponseBody> wxTestMergeConfirm();

    /**
     * 获取学院推荐数据
     *
     * @return
     */
    @GET(NetConfig.INFORMATION.GET_COLLEGE_RECOMMEND_VIDEO)
    Observable<BaseResult<CollegeVideoEntity.Result>> getCollegeHeadList();

    @GET(NetConfig.INFORMATION.GET_COLLEGE_RECOMMEND_VIDEO)
    Observable<ResponseBody> getTestCollegeHeadList();

    /**
     * 获取学院其他数据
     *
     * @return
     */
    @GET(NetConfig.INFORMATION.GET_COLLEGE_OTHER_VIDEO)
    Observable<BaseResult<CollegeVideoEntity.Result>> getCollegeOtherList(@QueryMap Map<String, String> map);

    @GET(NetConfig.INFORMATION.GET_COLLEGE_OTHER_VIDEO)
    Observable<ResponseBody> getTestCollegeOtherList(@QueryMap Map<String, String> map);

    /**
     * 获取视频详情
     *
     * @param map
     * @return
     */
    @GET(NetConfig.INFORMATION.GET_VIDEO_INFO)
    Observable<BaseResult<VideoInfoEntity.Result>> getVideoInfo(@QueryMap Map<String, String> map);

    @GET(NetConfig.INFORMATION.GET_VIDEO_INFO)
    Observable<ResponseBody> getTestVideoInfo(@QueryMap Map<String, String> map);

    /**
     * 点赞
     *
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
     *
     * @param paramsMap
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.USER.SIGNIN_URL)
    Observable<BaseResult<SignInEntity.Result>> signIn(@FieldMap Map<String, String> paramsMap);

    @POST(NetConfig.USER.SIGNIN_URL)
    Observable<ResponseBody> testSignIn(@Body RequestBody requestBody);

    /**
     * 关联资产
     *
     * @param requestBody
     * @return
     */
    @POST(NetConfig.USER.RELATED_ASSET_URL)
    Observable<BaseResult<CommonEntity.Result>> relatedAsset(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST(NetConfig.USER.RELATED_ASSET_URL)
    Observable<ResponseBody> toTestRelatedAsset(@FieldMap Map<String, String> paramsMap);

    /**
     * 资产证明
     *
     * @param
     * @return
     */
    @POST(NetConfig.USER.ASSET_PROVET_URL)
    Observable<BaseResult<CommonEntity.Result>> assetProve(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST(NetConfig.USER.ASSET_PROVET_URL)
    Observable<ResponseBody> toTestAssetProve(@FieldMap Map<String, String> paramsMap);

    /**
     * 更新用户信息
     *
     * @param
     * @return
     */
    @POST(NetConfig.USER.UPDATE_USER_INFO_URL)
    Observable<BaseResult<CommonEntity.Result>> updateUserInfo(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST(NetConfig.USER.UPDATE_USER_INFO_URL)
    Observable<ResponseBody> toTestupdateUserInfo(@FieldMap Map<String, String> paramsMap);

    /**
     * 验证用户信息
     *
     * @param paramsMap
     * @return
     */
    @GET(NetConfig.USER.VALIDATE_USER_PASSWORD_URL)
    Observable<BaseResult<CommonEntity.Result>> validateUserPasswrod(@QueryMap Map<String, String> paramsMap);

    @GET(NetConfig.USER.VALIDATE_USER_PASSWORD_URL)
    Observable<ResponseBody> toTestValidateUserPasswrod(@QueryMap Map<String, String> paramsMap);

    /**
     * 用户反馈信息
     *
     * @param
     * @return
     */
    @POST(NetConfig.USER.USER_FEED_BACK_URL)
    Observable<BaseResult<CommonEntity.Result>> feedBackUser(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST(NetConfig.USER.USER_FEED_BACK_URL)
    Observable<ResponseBody> toTestFeedBackUser(@FieldMap Map<String, String> paramsMap);

    /**
     * 风险评测提交
     *
     * @param
     * @return
     */
    @POST(NetConfig.API.RISK_EVALUTION)
    Observable<BaseResult<TypeNameEntity.Result>> riskEvalutionCommit(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST(NetConfig.API.RISK_EVALUTION)
    Observable<ResponseBody> toTestRiskEvalutionCommit(@FieldMap Map<String, String> paramsMap);

    /**
     * 获取融云用户信息
     *
     * @param map
     * @return
     */
    @GET(NetConfig.Auth.RONGYUN_USERINFO)
    Observable<BaseResult<RongUserEntity.Result>> getRongUserInfo(@QueryMap Map<String, String> map);

    @GET(NetConfig.Auth.RONGYUN_USERINFO)
    Observable<ResponseBody> getTestGetRongUserInfo(@QueryMap Map<String, String> map);

    /**
     * 获取群组信息
     *
     * @param map
     * @return
     */
    @GET(NetConfig.API.GROUP_INFO)
    Observable<BaseResult<GroupInfoEntity.Result>> getGroupInfo(@QueryMap Map<String, String> map);

    @GET(NetConfig.API.GROUP_INFO)
    Observable<ResponseBody> getTestGetGroupInfo(@QueryMap Map<String, String> map);

    /**
     * 获取群成员信息
     *
     * @param map
     * @return
     */
    @GET(NetConfig.API.GROUP_MEMBERS)
    Observable<BaseResult<GroupMemberEntity.Result>> getGroupMember(@QueryMap Map<String, String> map);

    @GET(NetConfig.API.GROUP_MEMBERS)
    Observable<ResponseBody> getTestGetGroupMember(@QueryMap Map<String, String> map);

    /**
     * 获取用户手机号码
     *
     * @param map
     * @return
     */
    @GET(NetConfig.API.GROUP_MEMBER_PHONE)
    Observable<BaseResult<UserPhoneNumEntity.Result>> getUserPhoneNumber(@QueryMap Map<String, String> map);

    @GET(NetConfig.API.GROUP_MEMBER_PHONE)
    Observable<ResponseBody> getTestGetUserPhoneNumber(@QueryMap Map<String, String> map);

    /**
     * 获取群组成员新的接口，返回数据结构不一样
     *
     * @param map
     * @return
     */
    @GET(NetConfig.API.GROUP_MEMBER_BY_DATE)
    Observable<BaseResult<GroupMemberNewEntity.Result>> getGroupMemberByBytes(@QueryMap Map<String, String> map);

    @GET(NetConfig.API.GROUP_MEMBER_BY_DATE)
    Observable<ResponseBody> getTestGetGroupMemberByBytes(@QueryMap Map<String, String> map);

    /**
     * 获取用户所属群组列表
     *
     * @param map
     * @return
     */
    @GET(NetConfig.API.CHATE_GROUP_LIST)
    Observable<BaseResult<GroupListEntity.Result>> getGroupList(@QueryMap Map<String, String> map);

    @GET(NetConfig.API.CHATE_GROUP_LIST)
    Observable<ResponseBody> getTestGetGroupList(@QueryMap Map<String, String> map);

    /**
     * 获取热门产品
     *
     * @param map
     * @return
     */
    @GET(NetConfig.API.HOT_SEARCH_PRODUCT)
    Observable<BaseResult<CommonEntity.Result>> getHotProduct(@QueryMap Map<String, String> map);

    @GET(NetConfig.API.HOT_SEARCH_PRODUCT)
    Observable<ResponseBody> getTestGetHotProduct(@QueryMap Map<String, String> map);

    /**
     * 获取平台客户
     *
     * @param map
     * @return
     */
    @GET(NetConfig.Auth.PLATFORM_CUSTOMER)
    Observable<BaseResult<CommonEntity.Result>> getPlatformCustomer(@QueryMap Map<String, String> map);

    @GET(NetConfig.Auth.PLATFORM_CUSTOMER)
    Observable<ResponseBody> getTestGetPlatformCustomer(@QueryMap Map<String, String> map);

    /**
     * 获取机构经理
     *
     * @param map
     * @return
     */
    @GET(NetConfig.Auth.ORIGNATION_MANAGER)
    Observable<BaseResult<OrgManagerEntity.Result>> getOrgMnager(@QueryMap Map<String, String> map);

    @GET(NetConfig.Auth.ORIGNATION_MANAGER)
    Observable<ResponseBody> getTestGetOrgMnager(@QueryMap Map<String, String> map);

    //获取产品筛选条件

    /**
     * 获取产品筛选条件
     *
     * @return
     */

    @GET(NetConfig.PRODUCT.Get_PRODUCT_TAG)
    Observable<ResponseBody> getProductFilter();

    /**
     * 获取产品列表数据
     *
     * @param paramsMap
     * @return
     */
    @GET(NetConfig.PRODUCT.Get_PRODUCTLS_TAG)
    Observable<ResponseBody> getProductls(@QueryMap Map<String, String> paramsMap);

    /**
     * 产品模块进行搜索的数据
     *
     * @param paramsMap
     * @return
     */
    @GET(NetConfig.SOUSOU.Get_PRODUCTLS_SOU)
    Observable<ResponseBody> getSousouResult(@QueryMap Map<String, String> paramsMap);

    /**
     * 热门搜索的标签
     *
     * @param paramsMap
     * @return
     */
    @GET(NetConfig.SOUSOU.Get_HOT_SOU)
    Observable<ResponseBody> getHotSousouResult(@QueryMap Map<String, String> paramsMap);

    /**
     * 产品详情
     */
    @GET(NetConfig.PRODUCT.Get_PRODUCTDETAIL_URL)
    Observable<ResponseBody> getProductDetail(@QueryMap Map<String, String> paramMap);

    /**
     * C端视频播放页获取视频播放
     *
     * @param parmsMap
     * @return
     */

    @GET(CNetConfig.VIDEO.GetVideoDetail)
    Observable<ResponseBody> getCvideoDetail(@QueryMap Map<String, String> parmsMap);

    /**
     * 视频点赞
     */
    @GET(NetConfig.VIDEO.VIDEO_DIANZAN)
    Observable<ResponseBody> videoDianZan(@QueryMap Map<String, String> parmsMap);

    /**
     * 视频添加评论
     */
    @POST(NetConfig.VIDEO.VIDEO_COMMENT_ADD)
    Observable<ResponseBody> videoCommentAdd(@QueryMap Map<String, String> parmsMap);

    @POST(NetConfig.VIDEO.VIDEO_COMMENT_ADD)
    Observable<ResponseBody> videoV2CommentAdd(@Body RequestBody parms);

    /**
     * 视频评论列表
     */
    @GET(NetConfig.VIDEO.VIDEO_COMMENT_LS)
    Observable<ResponseBody> videoCommentLs(@QueryMap Map<String, String> parmsMap);

    /**
     * 新增私享云视频学院的全部数据
     */
    @GET(NetConfig.INFORMATION.GET_VIDEO_ALLINF)
    Observable<ResponseBody> videoSchoolAllInf(@QueryMap Map<String, String> parmsMap);

    /**
     * 新增私享云视频学院的列表
     */
    @GET(NetConfig.INFORMATION.GET_VIDEO_LIST)
    Observable<ResponseBody> videoSchoolLs(@QueryMap Map<String, String> parmsMap);


    //编辑商城收货地址
    @POST(NetConfig.MALL.MALL_SAVE_ADDRESS)
    Observable<ResponseBody> saveAddress(@Body RequestBody responseBody);

    //新增商城收货地址
    @POST(NetConfig.MALL.MALL_ADD_ADDRESS)
    Observable<ResponseBody> addAddress(@Body RequestBody responseBody);

    //删除商城收货地址
    @POST(NetConfig.MALL.MALL_DETELE_ADDRESS)
    Observable<ResponseBody> deleteAddress(@Body RequestBody requestBody);


    //获取商城收货地址列表
    @GET(NetConfig.MALL.MALL_ADDRESS_LIST)
    Observable<ResponseBody> getAddressList(@QueryMap Map<String, String> paramsMap);

    //设置默认收货地址
    @POST(NetConfig.MALL.MALL_SET_DEFAULT)
    Observable<ResponseBody> setDefaultMallAddress(@Body RequestBody responseBody);


    //获取直播签名
    @GET(NetConfig.LIVE.GET_LIVE_SIGN)
    Observable<ResponseBody> getLiveSign(@QueryMap Map<String, String> paramsMap);

    //获取房间ID
    @GET(NetConfig.LIVE.GET_ROOM_NUM)
    Observable<ResponseBody> getRoomNum(@QueryMap Map<String, String> paramsMap);

    //获取直播列表
    @GET(NetConfig.LIVE.GET_LIVE_LIST)
    Observable<ResponseBody> getLiveList(@QueryMap Map<String, String> paramsMap);

    //获取直播预告
    @GET(NetConfig.LIVE.GET_PRO_LIST)
    Observable<ResponseBody> getProLiveList(@QueryMap Map<String, String> paramsMap);

    //主播开房间
    @POST(NetConfig.LIVE.HOST_OPEN_LIVE)
    Observable<ResponseBody> hostOpenLive(@Body RequestBody responseBody);

    //主播关闭房间
    @POST(NetConfig.LIVE.HOST_CLOSE_LIVE)
    Observable<ResponseBody> hostCloseLive(@Body RequestBody responseBody);

    //获取房间成员
    @GET(NetConfig.LIVE.GET_ROOM_MEMBER)
    Observable<ResponseBody> getRoomMenber(@QueryMap Map<String, String> paramsMap);

    //主播心跳
    @POST(NetConfig.LIVE.LIVE_HOST_HEART)
    Observable<ResponseBody> liveHostHeart(@Body RequestBody requestBody);

    //客户进入房间
    @POST(NetConfig.LIVE.CUSTOM_JOIN_ROOM)
    Observable<ResponseBody> customJoin(@Body RequestBody requestBody);

    //客户退出房间
    @POST(NetConfig.LIVE.CUSTOM_EXIT_ROOM)
    Observable<ResponseBody> custonExit(@Body RequestBody requestBody);

    //获取预告
    @GET(NetConfig.LIVE.GET_LIVE_NOTICE)
    Observable<ResponseBody> getNoticeLive(@QueryMap Map<String, String> paramsMap);

    //获取直播资料
    @GET(NetConfig.LIVE.GET_LIVE_PDF)
    Observable<ResponseBody> getLivePdf(@QueryMap Map<String, String> paramsMap);

    //直播发送评论
    @POST(NetConfig.LIVE.SENT_COMMENT)
    Observable<ResponseBody> sendLiveMsg(@Body RequestBody responseBody);

    //支付配置
    @GET(NetConfig.PAY.GET_PAY_CONFIG)
    Observable<ResponseBody> getRechargeConfig(@QueryMap Map<String, String> paramsMap);

    //校验支付结果
    @GET(NetConfig.PAY.CHECK_RECHARGE_SIGN)
    Observable<ResponseBody> checkRechargeSign(@QueryMap Map<String, String> paramsMap);

    //云豆充值
    @GET(NetConfig.PAY.YD_RECHARGE)
    Observable<ResponseBody> ydRecharge(@QueryMap Map<String, String> paramsMap);

    //获取每日任务完成情况
    @GET(NetConfig.TASK.TASK_LIST)
    Observable<ResponseBody> getDayTask(@QueryMap Map<String, String> paramsMap);

    //添加云豆
    @POST(NetConfig.TASK.GET_COIN)
    Observable<ResponseBody> taskAddCoin(@Body RequestBody responseBody);

    //登录前获取公钥
    @GET(NetConfig.AUTHOR.GET_PUBLIC_KEY)
    Observable<ResponseBody> getPublicKey();

    @GET(NetConfig.ACTION_POINT)
    Observable<ResponseBody> ActionPoint(@QueryMap Map<String, String> paramsMap);

    /************************6.0私享云新增***********************/

    @GET(NetConfig.SXY.GETHOME)
    Observable<BaseResult<HomeEntity.Result>> getSxyHome(@QueryMap Map<String, String> paramsMap);

    @GET(NetConfig.SXY.GETHOME)
    Observable<ResponseBody> getSxyHomeTest(@QueryMap Map<String, String> paramsMap);

    //根据手机硬件地址获取用户ID
    @POST(NetConfig.SXY.VISITORLOGIN)
    Observable<ResponseBody> visitor_get_UserId(@Body RequestBody responseBody);

    // 获取健康检测/医疗
//    @GET(NetConfig.SXY.HEALTH_GET_URL)
//    Observable<BaseResult<HealthEntity.Result>> getHealthList(@QueryMap Map<String, String> paramsMap);
    @GET(NetConfig.Health.HEALTH_GET_URL)
    Observable<ResponseBody> getHealthList(@QueryMap Map<String, String> paramsMap);

    // 健康项目列表
    @GET(NetConfig.Health.HEALTH_PROJECT_LIST)
    Observable<ResponseBody> getHealthProjectList(@QueryMap Map<String, String> paramsMap);

    // 健康课程
    @GET(NetConfig.Health.HEALTH_COURSE_GET_URL)
    Observable<ResponseBody> getHealthCourseList(@QueryMap Map<String, String> paramsMap);

    @GET(NetConfig.Health.HEALTH_INTRODUCE_URL)
    Observable<ResponseBody> getHealthIntruduce(@QueryMap Map<String, String> paramsMap);

    // 预约健康检测/医疗
    @POST(NetConfig.Health.HEALTH_FREE_BESPEAK_URL)
    Observable<ResponseBody> bespeakHealth(@Body RequestBody responseBody);

    // 预约健康检 短信验证
    @POST(NetConfig.Health.HEALTH_INFO_VALIDATE_URL)
    Observable<ResponseBody> bespeakHealthInfoValidate(@Body RequestBody responseBody);

    // 资讯首页数据
    @GET(NetConfig.Discovery.DISCOVERY_FIRST_PAGE)
    Observable<ResponseBody> getDiscoverFirstPage(@QueryMap Map<String, String> paramsMap);

    // 资讯列表数据
    @GET(NetConfig.Discovery.DISCOVERY_LIST_PAGE)
    Observable<ResponseBody> getDiscoverListPage(@QueryMap Map<String, String> paramsMap);

    // 我的
    @GET(NetConfig.Mine.GET_MINE)
    Observable<ResponseBody> getMineData(@QueryMap Map<String, String> paramsMap);

    // 我的活动
    @GET(NetConfig.Mine.ACTIVITES)
    Observable<ResponseBody> getMineActivitesList(@QueryMap Map<String, String> paramsMap);

    // 提交投资账号
    @POST(NetConfig.Mine.CommitInvisitAccount)
    Observable<ResponseBody> commitInvisitAccount(@Body RequestBody responseBody);

    /**
     * 生活家banner列表
     */
    @GET(NetConfig.ELEGANT.GETBANNER)
    Observable<BaseResult<ElegantLivingEntity.Result>> elegantLivingBanners(@QueryMap Map<String, String> paramsMap);

    /**
     * 尚品首页第一次加载数据
     */
    @GET(NetConfig.ELEGANT.GETGOODSFIRST)
    Observable<BaseResult<ElegantGoodsEntity.Result>> elegantGoodsFirst(@QueryMap Map<String, String> paramsMap);

    @GET(NetConfig.ELEGANT.GETGOODSMORE)
    Observable<BaseResult<ElegantGoodsEntity.ResultMore>> elegantGoodsMore(@QueryMap Map<String, String> params);

    //获取全局导航
    @GET(NetConfig.NAVIGATION)
    Observable<ResponseBody> getNavigation();

    //获取全局导航
    @GET(NetConfig.NAVIGATION_THREE)
    Observable<ResponseBody> getNavigationThird(@QueryMap Map<String, String> params);

    //签到
    @POST(NetConfig.SXY.SIGNIN)
    Observable<ResponseBody> sign(@Body RequestBody responseBody);

    /**
     * 修改用户登录密码
     *
     * @param responseBody
     * @return
     */
    @POST(NetConfig.ChangePsd.CHANGE_PSD)
    Observable<ResponseBody> changeLoginPsd(@Body RequestBody responseBody);

    /**
     * 更新用户信息
     *
     * @param requestBody
     * @return
     */
    @POST(NetConfig.ChangePsd.UPDATE_USERINFO)
    Observable<ResponseBody> updateUserInfoNewC(@Body RequestBody requestBody);

    /**
     * 上传头像的远程路径给服务端
     */
    @POST(NetConfig.ChangePsd.UPDATE_USERICON)
    Observable<ResponseBody> uploadIconRemotePath(@Body RequestBody requestBody);

    /**
     * 获取沙龙和城市
     */
    @GET(NetConfig.Salon.UPDATE_SALON_AND_CITY)
    Observable<BaseResult<SalonsEntity.Result>> getSalonsAndCitys(@QueryMap Map<String, String> params);

    @GET(NetConfig.Salon.UPDATE_SALON)
    Observable<BaseResult<OldSalonsEntity.SalonBean>> getOldSalons(@QueryMap Map<String, String> params);

    /**
     * 获取身份列表
     *
     * @return
     */
    @GET(NetConfig.Indentity.GET_INDENTITY_TYPE_LIST)
    Observable<BaseResult<List<IndentityEntity.IndentityBean>>> getIndentity();

    /**
     * 验证身份
     */
    @GET(NetConfig.Indentity.VERIFY_INDENTITY)
    Observable<ResponseBody> verifyIndentityInOkhttp();

    /**
     * 获取身份信息
     */
    @GET(NetConfig.Indentity.VERIFY_INDENTITY_V3)
    Observable<ResponseBody> verifyIndentityInOkhttpV3();

    /**
     * 获取证件列表
     *
     * @param params
     * @return
     */
    @GET(NetConfig.Indentity.GET_INDENTITY_LIST)
    Observable<BaseResult<CardListEntity.Result>> getCardList(@QueryMap Map<String, String> params);

    /**
     * 上传证件远程地址
     *
     * @param requestBody
     * @return
     */
    @POST(NetConfig.Indentity.UPLOAD_REMOTE_PATHS)
    Observable<ResponseBody> uploadRemotePath(@Body RequestBody requestBody);

    @GET(NetConfig.Indentity.CREDENTIALS_DETILS)
    Observable<ResponseBody> getCredentialDetial(@QueryMap Map<String,String> params);

    @GET(NetConfig.Indentity.GET_INDENTITY_LIST_ADD)
    Observable<BaseResult<CardListEntity.Result>> getCardListAdd(@QueryMap Map<String, String> programObject);

    /**
     * 红包雨
     */
    @GET(NetConfig.RED_PACKET)
    Observable<ResponseBody> loadRedPacket(@QueryMap Map<String, String> programObject);


    /**
     * 活体检测获取sign
     */
    @GET(NetConfig.Compliance.COMPLIANCE_LIVING_SIGN)
    Observable<ResponseBody> getLivingSign(@QueryMap Map<String, String> programObject);

    /**
     * 活体检测的结果通知 post
     */
    @POST(NetConfig.Compliance.COMPLIANCE_LIVING_RESULT)
    Observable<ResponseBody> queryLivingResult(@Body RequestBody responseBody);

    /**
     * 活体检测结果通知处理
     */
    @POST(NetConfig.Compliance.COMPLIANCE_BEAN_RESULT)
    Observable<BaseResult<LivingResultData> > queryDataResult(@Body RequestBody requestBody);
    /**
     * 活体检测公用锁的通知接口
     */
    @POST(NetConfig.Compliance.COMPLIANCE_BEAN_COMMENT_RESULT)
    Observable<BaseResult<LivingResultData>> queryComontDataResult(@Body RequestBody requestBody);

    /**
     * orc获取sign
     */
    @GET(NetConfig.Compliance.COMPLIANCE_OCR_SIGN)
    Observable<ResponseBody> getOcrSign(@QueryMap Map<String, String> programObject);

    /**
     * 自定义人脸识别OCR
     */
    @POST(NetConfig.Compliance.COMPLIANCE_OCR)
    Observable<BaseResult<IdentityCard>> getOcrResult(@Body RequestBody responseBody);
    /**
     * person对比
     */
    @GET(NetConfig.Compliance.COMPLIANCE_PERSON_COMPARE)
    Observable<ResponseBody>getPersonCompare(@QueryMap Map<String, String> programObject);

    /**
     * 通用的Get请求接口
     */
    @GET("{address}")
    Observable<ResponseBody> commonGetRequest(@Path("address") String url, @QueryMap Map<String, String> paramsMap);

    /**
     * 通用的Post请求接口
     */
    @POST("{address}")
    Observable<ResponseBody> commonPostRequest(@Path("address") String url, @Body RequestBody requestBody);

    /**
     * 获取资源文件的相关信息
     *
     * @param
     */
    @GET(NetConfig.RESOURCE_FILE_INFO)
    Observable<ResponseBody> requestResourceInfo();
//sss
}

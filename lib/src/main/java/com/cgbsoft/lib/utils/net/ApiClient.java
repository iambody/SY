package com.cgbsoft.lib.utils.net;

import android.location.Address;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.CollegeVideoEntity;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.SignInEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.model.VideoLikeEntity;
import com.cgbsoft.lib.base.model.WXUnionIDCheckEntity;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import rx.Observable;

/**
 *  * Created by xiaoyu.zhang on 2016/11/10 17:54
 *  
 */
public class ApiClient {

    /**
     * 获取服务器资源
     *
     * @return
     */
    public static Observable<AppResourcesEntity.Result> getAppResources() {
        Map<String, String> params = new HashMap<>();
        params.put("os", "1");
        params.put("version", Utils.getVersionName(BaseApplication.getContext()));
        params.put("client", SPreference.getIdtentify(BaseApplication.getContext()) + "");

        return OKHTTP.getInstance().getRequestManager().getAppResource(createProgram(params)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestAppResources() {
        Map<String, String> params = new HashMap<>();
        params.put("os", "1");
        params.put("version", Utils.getVersionName(BaseApplication.getContext()));
        params.put("client", SPreference.getIdtentify(BaseApplication.getContext()) + "");

        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestAppResource(createProgram(params)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

    }

    /**
     * 数据统计
     *
     * @param json
     * @return
     */
    public static Observable<String> pushDataStatistics(String json) {
        Map<String, String> map = new HashMap<>();
        map.put("contents", json);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_DS).pushDataStatistics(checkNull(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 获取ip
     *
     * @return
     */
    public static Observable<String> getIP() {
        Map<String, String> map = new HashMap<>();
        map.put("ie", "utf-8");
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_IP, false).getIP(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param pwdMD5   md5密码
     * @return
     */
    public static Observable<UserInfoDataEntity.Result> toLogin(String username, String pwdMD5) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", username);
        map.put("password", pwdMD5);
        return OKHTTP.getInstance().getRequestManager().toLogin(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    //todo 测试时候调用该接口
    public static Observable<String> toTestLogin(String username, String pwdMD5) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", username);
        map.put("password", pwdMD5);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestLogin(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取用户信息
     *
     * @param userid 用户id
     * @return
     */
    public static Observable<UserInfo> getUserInfo(String userid) {
        Map<String, String> map = new HashMap<>();
        map.put("adviserId", userid);
        return OKHTTP.getInstance().getRequestManager(true).getUserInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestUserInfo(String userid) {
        Map<String, String> map = new HashMap<>();
        map.put("adviserId", userid);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestUserInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取容云token
     *
     * @param rongExpired
     * @param rongUID
     * @return
     */
    public static Observable<RongTokenEntity.Result> getRongToken(String rongExpired, String rongUID) {
        Map<String, String> map = new HashMap<>();
        if (rongExpired != null)
            map.put("tokenExpired", rongExpired);
        map.put("uid", rongUID);
        return OKHTTP.getInstance().getRequestManager().getRongToken(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestRongToken(String rongExpired, String rongUID) {
        Map<String, String> map = new HashMap<>();
        if (rongExpired != null)
            map.put("tokenExpired", rongExpired);
        map.put("uid", rongUID);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestRongToken(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }


    /**
     * 微信 unioid 验证
     *
     * @param unionid
     * @return
     */
    public static Observable<WXUnionIDCheckEntity.Result> wxUnioIDCheck(String unionid) {
        Map<String, String> map = new HashMap<>();
        map.put("unionid", unionid);
        return OKHTTP.getInstance().getRequestManager().wxUnioIDCheck(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> wxTestUnioIDCheck(String unionid) {
        Map<String, String> map = new HashMap<>();
        map.put("unionid", unionid);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).wxTestUnioIDCheck(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 微信登录
     *
     * @param sex
     * @param nickName
     * @param unionid
     * @param headimgurl
     * @return
     */
    public static Observable<UserInfoDataEntity.Result> toWxLogin(String sex, String nickName, String unionid, String headimgurl) {
        Map<String, String> map = new HashMap<>();
        map.put("sex", sex);
        map.put("nickName", nickName);
        map.put("unionid", unionid);
        map.put("headImageUrl", headimgurl);
        return OKHTTP.getInstance().getRequestManager().toWxLogin(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestWxLogin(String sex, String nickName, String unionid, String headimgurl) {
        Map<String, String> map = new HashMap<>();
        map.put("sex", sex);
        map.put("nickName", nickName);
        map.put("unionid", unionid);
        map.put("headImageUrl", headimgurl);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestWxLogin(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取协议
     *
     * @return
     */
    public static Observable<String> getProtocol() {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_WWW, false).getProtocol().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 注册
     *
     * @param userName
     * @param pwdMd5
     * @param code
     * @return
     */
    public static Observable<UserInfoDataEntity.Result> toRegister(String userName, String pwdMd5, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password", pwdMd5);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager().toRegister(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestRegister(String userName, String pwdMd5, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password", pwdMd5);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestRegister(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    public static Observable<String> sendCode(String phone, int which) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("checkPhoneDuplicate", String.valueOf(which));
        return OKHTTP.getInstance().getRequestManager().sendCode(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> sendTestCode(String phone, int which) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("checkPhoneDuplicate", String.valueOf(which));
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).sendTestCode(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 验证验证码
     *
     * @param phone
     * @param code
     * @return
     */
    public static Observable<String> checkCode(String phone, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", phone);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager().checkCode(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> checkTestCode(String phone, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", phone);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).checkTestCode(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 重置密码
     *
     * @param un
     * @param pwdMd5
     * @param code
     * @return
     */
    public static Observable<String> resetPwd(String un, String pwdMd5, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", un);
        map.put("newPassword", pwdMd5);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager().resetPwd(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> resetTestPwd(String un, String pwdMd5, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", un);
        map.put("newPassword", pwdMd5);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).resetTestPwd(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 修改密码
     * @param userName 用户名称
     * @param pwdMd5Old 旧的密码 md5加密后
     * @param pwdMd5New 新的密码 md5加密后
     * @return
     */
    public static Observable<String>  modifyPassword(String userName, String pwdMd5Old, String pwdMd5New) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("oldPassword", pwdMd5Old);
        map.put("newPassword", pwdMd5New);
        return OKHTTP.getInstance().getRequestManager().modifyPassword(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String>  toTestModifyPassword(String userName, String pwdMd5Old, String pwdMd5New) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("oldPassword", pwdMd5Old);
        map.put("newPassword", pwdMd5New);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).modifyTestPassword(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 合并帐号--验证手机
     *
     * @param un
     * @param code
     * @return
     */
    public static Observable<String> wxMergePhone(String un, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("mergePhone", un);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager().wxMergePhone(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> wxTestMergePhone(String un, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("mergePhone", un);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).wxTestMergePhone(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 合并手机账户－－确认合并
     *
     * @return
     */
    public static Observable<String> wxMergeConfirm() {
        return OKHTTP.getInstance().getRequestManager().wxMergeConfirm().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> wxTestMergeConfirm() {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).wxTestMergeConfirm().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }


    /**
     * 获取学院头部数据
     *
     * @return
     */
    public static Observable<CollegeVideoEntity.Result> getCollegeHeadList() {
        return OKHTTP.getInstance().getRequestManager().getCollegeHeadList().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestCollegeHeadList() {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestCollegeHeadList().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取学院其他数据
     *
     * @return
     */
    public static Observable<CollegeVideoEntity.Result> getVideoOtherList(int offset, String type) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(type))
            map.put("type", type);
        map.put("offset", String.valueOf(offset));
        map.put("limit", "20");
        return OKHTTP.getInstance().getRequestManager().getCollegeOtherList(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestVideoOtherList(int offset, String type) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(type))
            map.put("type", type);
        map.put("offset", String.valueOf(offset));
        map.put("limit", "20");
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestCollegeOtherList(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取视频详情
     *
     * @param videoId
     * @return
     */
    public static Observable<VideoInfoEntity.Result> getVideoInfo(String videoId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", videoId);
        return OKHTTP.getInstance().getRequestManager().getVideoInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestVideoInfo(String videoId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", videoId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestVideoInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 视频点赞
     * @param videoId
     * @return
     */
    public static Observable<VideoLikeEntity.Result> toVideoLike(String videoId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", videoId);
        return OKHTTP.getInstance().getRequestManager().toVideoLike(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestVideoLike(String videoId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", videoId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestVideoLike(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 签到
     *
     * @param userId
     * @return
     */
    public static Observable<SignInEntity.Result> signIn(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("adviserId", userId);
        return OKHTTP.getInstance().getRequestManager().signIn(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> testSignIn(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("adviserId", userId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).testSignIn(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }


    // 产品模块=》的 获取产品的筛选条件
    public static Observable<String> getProductFiltrtDate() {
        return OKHTTP.getInstance().getRequestManager().getProductFilter().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }


    // 产品模块=》的 获取产品的列表
    public static Observable<String> getProductlsDate(Map<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().getProductls(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //产品模块=》的 搜索
    public static Observable<String> getSousouData(Map<String, String> map) {

        return OKHTTP.getInstance().getRequestManager().getSousouResult(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

    }

    //产品模块=》热门 搜索
    public static Observable<String> getHotSousouData(Map<String, String> map) {

        return OKHTTP.getInstance().getRequestManager().getHotSousouResult(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

    }
    /**
     * 重新生成Get 方式的value值
     *
     * @param map
     * @return
     */
    private static Map<String, String> createProgram(@NonNull Map<String, String> map) {
        String paramValue = getParamJSON(map);
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(paramValue))
            params.put("param", paramValue);
        return params;
    }

    /**
     * 生成json
     *
     * @param map
     * @return
     */
    private static String getParamJSON(@NonNull Map<String, String> map) {
        String paramValue = "";
        JSONObject jsonObject = null;
        Set<String> set = map.keySet();
        try {
            jsonObject = new JSONObject();
            for (String key : set) {
                String value = map.get(key);
                jsonObject.put(key, value);
            }
            paramValue = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return paramValue;
    }


    private static Map<String, String> checkNull(Map<String, String> map) {
        if (map != null) {
            Set<String> set = map.keySet();
            LinkedList<String> list = new LinkedList<>();
            for (String key : set) {
                String value = map.get(key);
                if (TextUtils.isEmpty(value)) {
                    list.add(key);
                }
            }
            for (String key : list) {
                map.remove(key);
            }
            if (!map.containsKey("appVersion")) {
                map.put("appVersion", Utils.getVersionName(BaseApplication.getContext()));
            }
            if (!map.containsKey("appPlatform")) {
                map.put("appPlatform", "android");
            }

        } else {
            map = new HashMap<>();
            if (!map.containsKey("appVersion")) {
                map.put("appVersion", Utils.getVersionName(BaseApplication.getContext()));
            }
            if (!map.containsKey("appPlatform")) {
                map.put("appPlatform", "android");
            }
        }
        return map;
    }

    //保存商城收货地址信息
    public static Observable<String> saveMallAddress(Map<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().saveAddress(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //删除商城收货地址
    public static Observable<String> deleteMallAddress(Map<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().deleteAddress(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //新增商城收货地址
    public static Observable<String> addAddress(Map<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().addAddress(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //获取商城收货地址列表
    public static Observable<String> getMallAddress(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().getAddressList(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    public static Observable<String> setDefauleMallAddress(String userId, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().setDefaultMallAddress(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

}

package com.cgbsoft.lib.utils.net;

import android.location.Address;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.CollegeVideoEntity;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.model.GroupInfoEntity;
import com.cgbsoft.lib.base.model.GroupListEntity;
import com.cgbsoft.lib.base.model.GroupMemberEntity;
import com.cgbsoft.lib.base.model.GroupMemberNewEntity;
import com.cgbsoft.lib.base.model.OrgManagerEntity;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.RongUserEntity;
import com.cgbsoft.lib.base.model.SignInEntity;
import com.cgbsoft.lib.base.model.TypeNameEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.UserPhoneNumEntity;
import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.model.VideoLikeEntity;
import com.cgbsoft.lib.base.model.WXUnionIDCheckEntity;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.encrypt.RSAUtils;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.tools.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 *  * Created by xiaoyu.zhang on 2016/11/10 17:54
 *  
 */
public class ApiClient {

    /**
     * map转换为json 用于生成RequestBody用于V2接口是使用
     *
     * @param map
     * @return
     */
//    public static RequestBody mapToBody(Map<String, String> map) {
//        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
//        JSONObject object = new JSONObject();
//        try {
//            while (iterator.hasNext()) {
//                Map.Entry<String, String> entry = iterator.next();
//                object.put(entry.getKey(), entry.getValue());
//            }
//        } catch (Exception e) {
//
//        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
//        return body;
//    }

    /**
     * map转换为json 用于生成RequestBody用于V2接口是使用
     *
     * @param map
     * @return
     */
    public static RequestBody mapToBody(Map map) {
        Iterator<Map.Entry> iterator = map.entrySet().iterator();
        JSONObject object = new JSONObject();
        try {
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                object.put(entry.getKey().toString(), entry.getValue());
            }
        } catch (Exception e) {

        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        return body;
    }

    /**
     * Json 转换为RequestBody
     *
     * @param object
     * @return
     */
    public static RequestBody jsonToBody(JSONObject object) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        return body;
    }

    /**
     * hashMap转换成string
     */
    public static String mapToObjStr(Map<String, String> map) {
        JSONObject obj = new JSONObject();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                obj.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
        }

        return obj.toString();
    }

    /**
     * 获取服务器资源
     *
     * @return
     */
    public static Observable<AppResourcesEntity.Result> getAppResources() {
        Map<String, String> params = new HashMap<>();
        params.put("os", "1");
        params.put("version", Utils.getVersionName(BaseApplication.getContext()));
        params.put("client", AppManager.isAdViser(BaseApplication.getContext()) ? "2" : "1");

        return OKHTTP.getInstance().getRequestManager().getAppResource(createProgram(params)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestAppResources() {
        Map<String, String> params = new HashMap<>();
        params.put("os", "1");
        params.put("version", Utils.getVersionName(BaseApplication.getContext()));
        params.put("client", AppManager.isAdViser(BaseApplication.getContext()) ? "2" : "1");

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

    public static Observable<String> toV2Login(HashMap<String, String> rsaString) {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestV2Login(mapToBody(rsaString)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
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

    public static Observable<String> toTestWxLogin(Context context, String sex, String nickName, String unionid, String headimgurl, String openId, String publicKey) {
//        Map<String, String> map = new HashMap<>();
//        map.put("sex", sex);
//        map.put("nickName", nickName);
//        map.put("unionid", unionid);
//        map.put("headImageUrl", headimgurl);
//        map.put("openId", openId);
//        map.put("recommendId", "");
//        map.put("client", AppManager.isInvestor(context) ? "C" : "B");

        JSONObject obj = new JSONObject();
        try {
            obj.put("sex", sex);
            obj.put("nickName", nickName);
            obj.put("unionid", unionid);
            obj.put("headImageUrl", headimgurl);
            obj.put("openId", openId);
            obj.put("recommendId", "");
            obj.put("client", AppManager.isInvestor(context) ? "C" : "B");

        } catch (Exception e) {
        }

        Map<String, String> mapParate = new HashMap<>();

        try {
            mapParate.put("sign", RSAUtils.GetRsA(context, obj.toString(), publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toV2WxLogin(mapToBody(mapParate)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

//        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestWxLogin(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
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
        JSONObject object = new JSONObject();
        try {
            object.put("userName", userName);
            object.put("password", pwdMd5);
            object.put("captcha", code);
        } catch (Exception e) {

        }
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toV2Register(jsonToBody(object)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
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
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone);
            object.put("checkPhoneDuplicate", String.valueOf(which));
        } catch (Exception e) {
        }
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).sendV2Code(jsonToBody(object)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
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
//        Map<String, String> map = new HashMap<>();
//        map.put("phoneNum", phone);
//        map.put("captcha", code);
        JSONObject object = new JSONObject();
        try {
            object.put("phoneNum", phone);
            object.put("captcha", code);
        } catch (Exception e) {

        }
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).checkV2Code(jsonToBody(object)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
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
//        map.put("userName", un);
//        map.put("newPassword", pwdMd5);
//        map.put("captcha", code);
        JSONObject object = new JSONObject();
        try {
            object.put("userName", un);
            object.put("newPassword", pwdMd5);
            object.put("captcha", code);
        } catch (Exception e) {

        }
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).resetV2Pwd(jsonToBody(object)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 修改密码
     *
     * @param userName  用户名称
     * @param pwdMd5Old 旧的密码 md5加密后
     * @param pwdMd5New 新的密码 md5加密后
     * @return
     */
    public static Observable<CommonEntity.Result> modifyPassword(String userName, String pwdMd5Old, String pwdMd5New) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("oldPassword", pwdMd5Old);
            jsonObject.put("newPassword", pwdMd5New);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return OKHTTP.getInstance().getRequestManager().modifyPassword(formatRequestBody(jsonObject)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestModifyPassword(String userName, String pwdMd5Old, String pwdMd5New) {
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

        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).wxV2MergePhone(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

//        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).wxTestMergePhone(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
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

    public static Observable<String> getToCVideoInfo(String videoId, Context context, String from) {
        Map<String, String> map = new HashMap<>();
        map.put("id", videoId);
//        map.put("from", from);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestVideoInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 关联资产
     *
     * @param customId    客户id
     * @param assertImage 上传图片路径
     * @return
     */
    public static Observable<CommonEntity.Result> relatedAsset(String customId, String assertImage) {
        JSONObject map = new JSONObject();
        try {
            map.put("customerId", customId);
            map.put("myAssetImage", assertImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return OKHTTP.getInstance().getRequestManager().relatedAsset(formatRequestBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestRelatedAsset(String customId, String assertImage) {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", customId);
        map.put("myAssetImage", assertImage);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestRelatedAsset(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    private static RequestBody formatRequestBody(JSONObject jsonObject) {
        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonObject.toString());
    }

    /**
     * 资产证明
     *
     * @param customId    客户id
     * @param assertImage 上传图片路径
     * @return
     */
    public static Observable<CommonEntity.Result> assertProve(String customId, JSONArray assertImage, String investmentType) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerId", customId);
            jsonObject.put("assetImage", assertImage);
            jsonObject.put("investmentType", investmentType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return OKHTTP.getInstance().getRequestManager().assetProve(formatRequestBody(jsonObject)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestassertProve(String customId, JSONArray assertImage, String investmentType) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customId);
        map.put("assetImage", assertImage);
        map.put("investmentType", investmentType);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestAssetProve(createProgramObject(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 修改用户信息
     *
     * @param hashMap 需要更新的用户字段信息
     * @return
     */
    public static Observable<CommonEntity.Result> updateUserInfo(HashMap<String, String> hashMap) {
        return OKHTTP.getInstance().getRequestManager().updateUserInfo(formatRequestBody(ApiBusParam.formatHashMapToJSONObject(hashMap))).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestUpdateUserInfo(HashMap<String, String> hashMap) {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestupdateUserInfo(createProgram(hashMap)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 验证用户密码
     *
     * @param hashMap 用户密码
     * @return
     */
    public static Observable<CommonEntity.Result> validateUserPassword(HashMap<String, String> hashMap) {
        return OKHTTP.getInstance().getRequestManager().validateUserPasswrod(createProgram(hashMap)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestValidateUserPassword(HashMap<String, String> hashMap) {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestValidateUserPasswrod(createProgram(hashMap)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 用户反馈
     *
     * @param hashMap
     * @return
     */
    public static Observable<CommonEntity.Result> feedBackUser(HashMap<String, Object> hashMap) {
        return OKHTTP.getInstance().getRequestManager().feedBackUser(formatRequestBody(ApiBusParam.formatHashMapToJSONObject(hashMap))).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestFeedBackUser(HashMap<String, Object> hashMap) {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestFeedBackUser(createProgramObject(hashMap)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 提交风险评测
     *
     * @param hashMap
     * @return
     */
    public static Observable<TypeNameEntity.Result> commitRistResult(HashMap<String, String> hashMap) {
        return OKHTTP.getInstance().getRequestManager().riskEvalutionCommit(formatRequestBody(ApiBusParam.formatHashMapToJSONObject(hashMap))).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> toTestCommitRistResult(HashMap<String, Object> hashMap) {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).toTestRiskEvalutionCommit(createProgramObject(hashMap)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取融云用户信息
     *
     * @param userId
     * @return
     */
    public static Observable<RongUserEntity.Result> getRongUserInfo(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", userId);
        return OKHTTP.getInstance().getRequestManager().getRongUserInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> goTestGetRongUserInfo(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", userId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetRongUserInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取群组信息
     *
     * @param groupId
     */
    public static Observable<GroupInfoEntity.Result> getGroupInfo(String groupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        return OKHTTP.getInstance().getRequestManager().getGroupInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestGetGroupInfo(String groupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetGroupInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取群成员
     *
     * @param groupId
     */
    public static Observable<GroupMemberEntity.Result> getGroupMember(String groupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        return OKHTTP.getInstance().getRequestManager().getGroupMember(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestGetGroupMember(String groupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetGroupMember(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取群成员 新增接口，返回数据结构不一样
     *
     * @param groupId
     */
    public static Observable<GroupMemberNewEntity.Result> getGroupMemberNew(String groupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        return OKHTTP.getInstance().getRequestManager().getGroupMemberByBytes(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestGetGroupMemberNew(String groupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetGroupMemberByBytes(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }


    /**
     * 获取用户所属群组列表
     *
     * @param userId
     */
    public static Observable<GroupListEntity.Result> getGroupList(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        return OKHTTP.getInstance().getRequestManager().getGroupList(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestGetGroupList(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetGroupList(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取用户手机号码
     *
     * @param memberId
     */
    public static Observable<UserPhoneNumEntity.Result> getUserPhoneNumber(String memberId) {
        Map<String, String> map = new HashMap<>();
        map.put("memberId", memberId);
        return OKHTTP.getInstance().getRequestManager().getUserPhoneNumber(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestGetUserPhoneNumber(String memberId) {
        Map<String, String> map = new HashMap<>();
        map.put("memberId", memberId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetUserPhoneNumber(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取热门产品
     */
    public static Observable<CommonEntity.Result> getHotProduct() {
        Map<String, String> map = new HashMap<>();
        return OKHTTP.getInstance().getRequestManager().getHotProduct(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestGetHotProduct() {
        Map<String, String> map = new HashMap<>();
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetHotProduct(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取平台客户
     *
     * @param userId
     */
    public static Observable<CommonEntity.Result> getPlatformCustomer(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", userId);
        return OKHTTP.getInstance().getRequestManager().getPlatformCustomer(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestGetPlatformCustomer(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", userId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetPlatformCustomer(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取机构经理
     *
     * @param userId
     */
    public static Observable<OrgManagerEntity.Result> getOrgManager(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", userId);
        return OKHTTP.getInstance().getRequestManager().getOrgMnager(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    public static Observable<String> getTestOrgManager(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", userId);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).getTestGetOrgMnager(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 视频点赞
     *
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
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_ADD, false).testSignIn(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 产品模块=》的 获取产品的筛选条件
     *
     * @return
     */
    public static Observable<String> getProductFiltrtDate() {
        return OKHTTP.getInstance().getRequestManager().getProductFilter().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 产品模块=》的 获取产品的列表
     *
     * @param map
     * @return
     */
    public static Observable<String> getProductlsDate(Map<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().getProductls(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 产品模块=》的 搜索
     *
     * @param map
     * @return
     */
    public static Observable<String> getSousouData(Map<String, String> map) {

        return OKHTTP.getInstance().getRequestManager().getSousouResult(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

    }

    /**
     * 产品模块=》热门 搜索
     *
     * @param map
     * @return
     */
    public static Observable<String> getHotSousouData(Map<String, String> map) {

        return OKHTTP.getInstance().getRequestManager().getHotSousouResult(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

    }

    public static Observable<String> getProductDetail(String SchemeId) {
        Map<String, String> map = new HashMap<>();
        map.put("schemeId", SchemeId);
        return OKHTTP.getInstance().getRequestManager().getProductDetail(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 视频模块
     */
    public static Observable<String> VideoDianZan(String VideoId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", VideoId);
        return OKHTTP.getInstance().getRequestManager().videoDianZan(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 视频添加评论
     */
    public static Observable<String> videoCommentAdd(String commnetContent, String SenderId, String id) {
        Map<String, String> map = new HashMap();
        map.put("commentContent", commnetContent);
        map.put("senderId", SenderId);
        map.put("id", id);
        return OKHTTP.getInstance().getRequestManager().videoV2CommentAdd(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

//        return OKHTTP.getInstance().getRequestManager().videoCommentAdd(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 评论列表
     */
    public static Observable<String> videoCommentLs(String id, String commentId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("commentId", commentId);
        map.put("limit", "" + Contant.VIDEO_COMMENT_LIMIT);
        return OKHTTP.getInstance().getRequestManager().videoCommentLs(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());

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
     * 重新生成Post 方式的值
     *
     * @param map
     * @return
     */
    private static Map<String, String> createProgramObject(@NonNull Map<String, Object> map) {
        String paramValue = getParamJSONArray(map);
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

    /**
     * 生成json
     *
     * @param map
     * @return
     */
    private static String getParamJSONArray(@NonNull Map<String, Object> map) {
        String paramValue = "";
        JSONObject jsonObject = null;
        Set<String> set = map.keySet();
        try {
            jsonObject = new JSONObject();
            for (String key : set) {
                Object value = map.get(key);
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
        return OKHTTP.getInstance().getRequestManager().saveAddress(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //删除商城收货地址
    public static Observable<String> deleteMallAddress(Map<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().deleteAddress(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //新增商城收货地址
    public static Observable<String> addAddress(Map<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().addAddress(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //获取商城收货地址列表
    public static Observable<String> getMallAddress(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().getAddressList(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //设为默认地址
    public static Observable<String> setDefauleMallAddress(String userId, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().setDefaultMallAddress(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    //获取直播列表
    public static Observable<String> getLiveList(String userId) {
        Map<String, String> map = new ArrayMap<>();
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().getLiveList(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 主播开房间
     * key-->value
     * id -->roomId
     * chat --> chatId
     * title -->roomTitle
     * user_id --> userId
     * room_type -->
     * image --> 直播封面
     * equipment --> 2
     */
    public static Observable<String> hostOpenLive(Map<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().hostOpenLive(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 主播关闭房间
     *
     * @return
     */
    public static Observable<String> hostCloseLive(String roomId, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().hostCloseLive(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取直播签名
     *
     * @param userId
     * @return
     */
    public static Observable<String> getLiveSign(String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", userId);
        return OKHTTP.getInstance().getRequestManager().getLiveSign(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    public static Observable<String> getLivePDF(String roomId){
        HashMap<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        return OKHTTP.getInstance().getRequestManager().getLivePdf(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取支付配置
     */
    public static Observable<String> getRechargeConfig() {
        Map<String, String> map = new HashMap<>();
        return OKHTTP.getInstance().getRequestManager().getRechargeConfig(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 校验支付结果
     */
    public static Observable<String> checkRecharge(Map<String, Object> map) {
        return OKHTTP.getInstance().getRequestManager().checkRechargeSign(createProgramObject(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 充值
     */
    public static Observable<String> ydRecharge(Map<String, Object> map) {
        return OKHTTP.getInstance().getRequestManager().ydRecharge(createProgramObject(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }


    /**
     * 获取每日任务
     */
    public static Observable<String> initDayTask() {
        return OKHTTP.getInstance().getRequestManager().getDayTask(new HashMap<>()).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 任务完成添加云豆
     */
    public static Observable<String> addTaskCoin(String taskType, String taskName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskName", taskName);
        map.put("taskType", taskType);
        return OKHTTP.getInstance().getRequestManager().taskAddCoin(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取登录前的publickey
     */
    public static Observable<String> getLoginPublic() {
        return OKHTTP.getInstance().getRequestManager().getPublicKey().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 成员进入直播
     */
    public static Observable<String> memberJoinRoom(String roomId, String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().customJoin(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 成员退出直播
     *
     * @param roomId
     * @param userId
     * @return
     */
    public static Observable<String> memberExitRoom(String roomId, String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().custonExit(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取房间号
     *
     * @return
     */
    public static Observable<String> getLiveRoomNum(HashMap<String, String> map) {
        return OKHTTP.getInstance().getRequestManager().getRoomNum(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 主播开房间
     *
     * @param roomId
     * @param userId
     * @return
     */
    public static Observable<String> hostCreatRoom(String roomId, String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().hostOpenLive(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 主播结束直播
     *
     * @param roomId
     * @param userId
     * @return
     */
    public static Observable<String> hostCloseRoom(String roomId, String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().hostCloseLive(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 获取房间成员
     *
     * @return
     */
    public static Observable<String> getLiveMember(HashMap<String, Object> map) {
        return OKHTTP.getInstance().getRequestManager().getRoomMenber(createProgramObject(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 主播心跳
     */
    public static Observable<String> hostHeart(String roomId, String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("user_id", userId);
        return OKHTTP.getInstance().getRequestManager().liveHostHeart(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 发评论
     */
    public static Observable<String> sendLiveMsg(HashMap<String, Object> map) {
        return OKHTTP.getInstance().getRequestManager().sendLiveMsg(mapToBody(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }


}

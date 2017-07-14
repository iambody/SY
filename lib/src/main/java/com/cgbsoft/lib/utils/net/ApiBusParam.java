package com.cgbsoft.lib.utils.net;

import com.cgbsoft.lib.utils.tools.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author chenlong
 */

public class ApiBusParam {

    /**
     * 设置手势密码
     *
     * @param userId
     * @param gesturePassword
     * @return
     */
    public static HashMap<String, String> gesturePasswordSetParams(String userId, String gesturePassword) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("gesturePassword", gesturePassword);
        hashMap.put("adviserId", userId);
        hashMap.put("gestureSwitch", "1");
        return hashMap;
    }

    /**
     * 关闭手势密码
     *
     * @param userId
     * @return
     */
    public static HashMap<String, String> gesturePasswordCloseParams(String userId) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("adviserId", userId);
        hashMap.put("gestureSwitch", "2");
        return hashMap;
    }

    /**
     * 验证登录密码
     *
     * @param userId
     * @param logPassword
     * @return
     */
    public static HashMap<String, String> gesturePasswordValidateParams(String userId, String logPassword) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("adviserId", userId);
        hashMap.put("password", MD5Utils.getShortMD5(logPassword));
        return hashMap;
    }

    /**
     * 风险评测提交
     *
     * @param userId
     * @param result
     * @param riskEvaluationName
     * @param riskEvaluationIdnum
     * @param riskEvaluationPhone
     * @return
     */
    public static HashMap<String, String> riskEvalutionParams(String userId, String result, String riskEvaluationName, String riskEvaluationIdnum, String riskEvaluationPhone) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", userId);
        hashMap.put("answer", result);
        hashMap.put("riskEvaluationName", riskEvaluationName);
        hashMap.put("riskEvaluationIdnum", riskEvaluationIdnum);
        hashMap.put("riskEvaluationPhone", riskEvaluationPhone);
        return hashMap;
    }

    public static JSONObject formatHashMapToJSONObject(HashMap hashMap) {
        Set<Map.Entry> set = hashMap.entrySet();
        JSONObject jsonObject = new JSONObject();
        try {
            for (Map.Entry entry : set) {
                jsonObject.putOpt((String)entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /** *****************以下为6.0私享云 新增接口参数******************* **/

    public static HashMap getHealthDataParams(String category, int offset, int limit) {
        HashMap hashMap = new HashMap<>();
        hashMap.put("category", category);
        hashMap.put("offset", String.valueOf(offset));
        hashMap.put("limit", String.valueOf(limit));
        return hashMap;
    }

    public static HashMap getBespeakHealthParams(String id, String name, String phone, String captcha) {
        HashMap hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("name", name);
        hashMap.put("phone", phone);
        hashMap.put("captcha", captcha);
        return hashMap;
    }

    public static HashMap getBespeakHealthValidateParams(String phone) {
        HashMap hashMap = new HashMap<>();
        hashMap.put("phone", phone);
        hashMap.put("checkPhoneDuplicate", "6");
        return hashMap;
    }

    public static HashMap getDiscoverListDataParams(String offset, String category) {
        HashMap hashMap = new HashMap<>();
        hashMap.put("offset", offset);
        hashMap.put("category", category);
        return hashMap;
    }

    public static HashMap getActivitesListData(int offset, int limit) {
        HashMap hashMap = new HashMap<>();
        hashMap.put("offset", String.valueOf(offset));
        hashMap.put("limit", String.valueOf(limit));
        return hashMap;
    }
}

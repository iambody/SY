package com.cgbsoft.lib.utils.net;

import com.cgbsoft.lib.utils.tools.MD5Utils;

import java.util.HashMap;

/**
 * @author chenlong
 */

public class ApiBusParam {

    /**
     * 设置手势密码
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
}

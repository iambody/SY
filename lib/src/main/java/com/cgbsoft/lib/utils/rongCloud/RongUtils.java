package com.cgbsoft.lib.utils.rongCloud;

import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;

/**
 * 融云工具类
 * Created by xiaoyu.zhang on 2016/11/21 11:50
 * Email:zhangxyfs@126.com
 *  
 */
public class RongUtils {


    private void getRongToken() {
        String rongExpired = OtherDataProvider.getRongTokenExpired(Appli.getContext());
        String rongUID = OtherDataProvider.getRongUid(Appli.getContext());
        String rongToken = OtherDataProvider.getRongToken(Appli.getContext());

        String userId = SPreference.getUserId(Appli.getContext());

        if (!TextUtils.equals(rongUID, userId) || !TextUtils.equals("2", rongExpired)) {
            OtherDataProvider.saveRongUid(Appli.getContext(), userId);
            OtherDataProvider.saveRongExpired(Appli.getContext(), "2");

            String needExpired = TextUtils.equals(rongExpired, "1") ? "1" : null;
            ApiClient.getRongToken(needExpired, userId);
        } else {
            if (SPreference.getUserInfoData(Appli.getContext()) != null){

            }
        }
    }
}

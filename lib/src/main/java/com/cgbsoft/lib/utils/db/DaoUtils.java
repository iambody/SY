package com.cgbsoft.lib.utils.db;

import android.content.Context;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.utils.db.dao.OtherInfoDao;
import com.cgbsoft.lib.utils.db.dao.UserInfoDao;

/**
 * 数据库工具
 * Created by xiaoyu.zhang on 2016/11/24 14:15
 * Email:zhangxyfs@126.com
 *  
 */
public class DaoUtils {
    private OtherInfoDao otherInfoDao;
    private UserInfoDao userInfoDao;

    public static final int W_OTHER = 1;
    public static final int W_USER = 2;

    public DaoUtils(Context context, int which) {
        switch (which) {
            case W_OTHER:
                otherInfoDao = ((Appli) context.getApplicationContext()).getDaoSession().getOtherInfoDao();
                break;
            case W_USER:
                userInfoDao = ((Appli) context.getApplicationContext()).getDaoSession().getUserInfoDao();
                break;
        }
    }

    public OtherInfo getOtherInfo(String title) {
        return otherInfoDao.queryBuilder().where(OtherInfoDao.Properties.Title.eq(title)).build().unique();
    }


    public void saveOrUpdataOther(String title, String value) {
        OtherInfo info = getOtherInfo(title);
        if (info != null) {
            info.setContent(value);
            otherInfoDao.update(info);
        } else
            otherInfoDao.insert(new OtherInfo(null, title, value));
    }

    public void destory() {
        otherInfoDao = null;
        userInfoDao = null;
    }

}

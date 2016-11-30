package com.cgbsoft.lib.utils.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.base.model.bean.ToBBean;
import com.cgbsoft.lib.base.model.bean.ToCBean;
import com.cgbsoft.lib.base.model.bean.UserInfo;

import com.cgbsoft.lib.utils.db.dao.OtherInfoDao;
import com.cgbsoft.lib.utils.db.dao.ToBBeanDao;
import com.cgbsoft.lib.utils.db.dao.ToCBeanDao;
import com.cgbsoft.lib.utils.db.dao.UserInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig otherInfoDaoConfig;
    private final DaoConfig toBBeanDaoConfig;
    private final DaoConfig toCBeanDaoConfig;
    private final DaoConfig userInfoDaoConfig;

    private final OtherInfoDao otherInfoDao;
    private final ToBBeanDao toBBeanDao;
    private final ToCBeanDao toCBeanDao;
    private final UserInfoDao userInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        otherInfoDaoConfig = daoConfigMap.get(OtherInfoDao.class).clone();
        otherInfoDaoConfig.initIdentityScope(type);

        toBBeanDaoConfig = daoConfigMap.get(ToBBeanDao.class).clone();
        toBBeanDaoConfig.initIdentityScope(type);

        toCBeanDaoConfig = daoConfigMap.get(ToCBeanDao.class).clone();
        toCBeanDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        otherInfoDao = new OtherInfoDao(otherInfoDaoConfig, this);
        toBBeanDao = new ToBBeanDao(toBBeanDaoConfig, this);
        toCBeanDao = new ToCBeanDao(toCBeanDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);

        registerDao(OtherInfo.class, otherInfoDao);
        registerDao(ToBBean.class, toBBeanDao);
        registerDao(ToCBean.class, toCBeanDao);
        registerDao(UserInfo.class, userInfoDao);
    }
    
    public void clear() {
        otherInfoDaoConfig.clearIdentityScope();
        toBBeanDaoConfig.clearIdentityScope();
        toCBeanDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
    }

    public OtherInfoDao getOtherInfoDao() {
        return otherInfoDao;
    }

    public ToBBeanDao getToBBeanDao() {
        return toBBeanDao;
    }

    public ToCBeanDao getToCBeanDao() {
        return toCBeanDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

}

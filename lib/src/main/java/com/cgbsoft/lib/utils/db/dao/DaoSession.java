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
import com.cgbsoft.lib.base.model.bean.VideoInfo;
import com.cgbsoft.privatefund.bean.product.HistorySearchBean;
import com.cgbsoft.privatefund.bean.commui.DayTaskBean;

import com.cgbsoft.lib.utils.db.dao.OtherInfoDao;
import com.cgbsoft.lib.utils.db.dao.ToBBeanDao;
import com.cgbsoft.lib.utils.db.dao.ToCBeanDao;
import com.cgbsoft.lib.utils.db.dao.UserInfoDao;
import com.cgbsoft.lib.utils.db.dao.VideoInfoDao;
import com.cgbsoft.lib.utils.db.dao.DayTaskBeanDao;
import com.cgbsoft.lib.utils.db.dao.HistorySearchBeanDao;

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
    private final DaoConfig videoInfoDaoConfig;
    private final DaoConfig historySearchBeanDaoConfig;
    private final DaoConfig dayTaskBeanDaoConfig;

    private final OtherInfoDao otherInfoDao;
    private final ToBBeanDao toBBeanDao;
    private final ToCBeanDao toCBeanDao;
    private final UserInfoDao userInfoDao;
    private final VideoInfoDao videoInfoDao;
    private final DayTaskBeanDao dayTaskBeanDao;
    private final HistorySearchBeanDao historySearchBeanDao;

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

        videoInfoDaoConfig = daoConfigMap.get(VideoInfoDao.class).clone();
        videoInfoDaoConfig.initIdentityScope(type);

        historySearchBeanDaoConfig = daoConfigMap.get(HistorySearchBeanDao.class).clone();
        historySearchBeanDaoConfig.initIdentityScope(type);

        dayTaskBeanDaoConfig = daoConfigMap.get(DayTaskBeanDao.class).clone();
        dayTaskBeanDaoConfig.initIdentityScope(type);

        otherInfoDao = new OtherInfoDao(otherInfoDaoConfig, this);
        toBBeanDao = new ToBBeanDao(toBBeanDaoConfig, this);
        toCBeanDao = new ToCBeanDao(toCBeanDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);
        videoInfoDao = new VideoInfoDao(videoInfoDaoConfig, this);
        dayTaskBeanDao = new DayTaskBeanDao(dayTaskBeanDaoConfig, this);
        historySearchBeanDao = new HistorySearchBeanDao(historySearchBeanDaoConfig, this);

        registerDao(OtherInfo.class, otherInfoDao);
        registerDao(ToBBean.class, toBBeanDao);
        registerDao(ToCBean.class, toCBeanDao);
        registerDao(UserInfo.class, userInfoDao);
        registerDao(VideoInfo.class, videoInfoDao);
        registerDao(HistorySearchBean.class, historySearchBeanDao);
        registerDao(DayTaskBean.class, dayTaskBeanDao);
    }
    
    public void clear() {
        otherInfoDaoConfig.clearIdentityScope();
        toBBeanDaoConfig.clearIdentityScope();
        toCBeanDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
        videoInfoDaoConfig.clearIdentityScope();
        dayTaskBeanDaoConfig.clearIdentityScope();
        historySearchBeanDaoConfig.clearIdentityScope();
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

    public VideoInfoDao getVideoInfoDao() {
        return videoInfoDao;
    }

    public HistorySearchBeanDao getHistorySearchBeanDao() {
        return historySearchBeanDao;
    }

    public DayTaskBeanDao getDayTaskBeanDao() {
        return dayTaskBeanDao;
    }

}

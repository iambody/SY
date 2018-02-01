package com.cgbsoft.lib.utils.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.cgbsoft.privatefund.bean.product.HistorySearchBean;
import com.cgbsoft.lib.base.model.bean.ToCBean;
import com.cgbsoft.lib.base.model.bean.TrackingDataBean;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.model.bean.VideoInfo;
import com.cgbsoft.lib.base.model.bean.ToBBean;
import com.cgbsoft.lib.base.model.bean.DataStatisticsBean;

import com.cgbsoft.lib.utils.db.dao.HistorySearchBeanDao;
import com.cgbsoft.lib.utils.db.dao.ToCBeanDao;
import com.cgbsoft.lib.utils.db.dao.TrackingDataBeanDao;
import com.cgbsoft.lib.utils.db.dao.OtherInfoDao;
import com.cgbsoft.lib.utils.db.dao.UserInfoDao;
import com.cgbsoft.lib.utils.db.dao.VideoInfoDao;
import com.cgbsoft.lib.utils.db.dao.ToBBeanDao;
import com.cgbsoft.lib.utils.db.dao.DataStatisticsBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig historySearchBeanDaoConfig;
    private final DaoConfig toCBeanDaoConfig;
    private final DaoConfig trackingDataBeanDaoConfig;
    private final DaoConfig otherInfoDaoConfig;
    private final DaoConfig userInfoDaoConfig;
    private final DaoConfig videoInfoDaoConfig;
    private final DaoConfig toBBeanDaoConfig;
    private final DaoConfig dataStatisticsBeanDaoConfig;

    private final HistorySearchBeanDao historySearchBeanDao;
    private final ToCBeanDao toCBeanDao;
    private final TrackingDataBeanDao trackingDataBeanDao;
    private final OtherInfoDao otherInfoDao;
    private final UserInfoDao userInfoDao;
    private final VideoInfoDao videoInfoDao;
    private final ToBBeanDao toBBeanDao;
    private final DataStatisticsBeanDao dataStatisticsBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        historySearchBeanDaoConfig = daoConfigMap.get(HistorySearchBeanDao.class).clone();
        historySearchBeanDaoConfig.initIdentityScope(type);

        toCBeanDaoConfig = daoConfigMap.get(ToCBeanDao.class).clone();
        toCBeanDaoConfig.initIdentityScope(type);

        trackingDataBeanDaoConfig = daoConfigMap.get(TrackingDataBeanDao.class).clone();
        trackingDataBeanDaoConfig.initIdentityScope(type);

        otherInfoDaoConfig = daoConfigMap.get(OtherInfoDao.class).clone();
        otherInfoDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        videoInfoDaoConfig = daoConfigMap.get(VideoInfoDao.class).clone();
        videoInfoDaoConfig.initIdentityScope(type);

        toBBeanDaoConfig = daoConfigMap.get(ToBBeanDao.class).clone();
        toBBeanDaoConfig.initIdentityScope(type);

        dataStatisticsBeanDaoConfig = daoConfigMap.get(DataStatisticsBeanDao.class).clone();
        dataStatisticsBeanDaoConfig.initIdentityScope(type);

        historySearchBeanDao = new HistorySearchBeanDao(historySearchBeanDaoConfig, this);
        toCBeanDao = new ToCBeanDao(toCBeanDaoConfig, this);
        trackingDataBeanDao = new TrackingDataBeanDao(trackingDataBeanDaoConfig, this);
        otherInfoDao = new OtherInfoDao(otherInfoDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);
        videoInfoDao = new VideoInfoDao(videoInfoDaoConfig, this);
        toBBeanDao = new ToBBeanDao(toBBeanDaoConfig, this);
        dataStatisticsBeanDao = new DataStatisticsBeanDao(dataStatisticsBeanDaoConfig, this);

        registerDao(HistorySearchBean.class, historySearchBeanDao);
        registerDao(ToCBean.class, toCBeanDao);
        registerDao(TrackingDataBean.class, trackingDataBeanDao);
        registerDao(OtherInfo.class, otherInfoDao);
        registerDao(UserInfo.class, userInfoDao);
        registerDao(VideoInfo.class, videoInfoDao);
        registerDao(ToBBean.class, toBBeanDao);
        registerDao(DataStatisticsBean.class, dataStatisticsBeanDao);
    }
    
    public void clear() {
        historySearchBeanDaoConfig.clearIdentityScope();
        toCBeanDaoConfig.clearIdentityScope();
        trackingDataBeanDaoConfig.clearIdentityScope();
        otherInfoDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
        videoInfoDaoConfig.clearIdentityScope();
        toBBeanDaoConfig.clearIdentityScope();
        dataStatisticsBeanDaoConfig.clearIdentityScope();
    }

    public HistorySearchBeanDao getHistorySearchBeanDao() {
        return historySearchBeanDao;
    }

    public ToCBeanDao getToCBeanDao() {
        return toCBeanDao;
    }

    public TrackingDataBeanDao getTrackingDataBeanDao() {
        return trackingDataBeanDao;
    }

    public OtherInfoDao getOtherInfoDao() {
        return otherInfoDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public VideoInfoDao getVideoInfoDao() {
        return videoInfoDao;
    }

    public ToBBeanDao getToBBeanDao() {
        return toBBeanDao;
    }

    public DataStatisticsBeanDao getDataStatisticsBeanDao() {
        return dataStatisticsBeanDao;
    }

}

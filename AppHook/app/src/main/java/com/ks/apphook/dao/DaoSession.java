package com.ks.apphook.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.ks.apphook.AppInfo;

import com.ks.apphook.dao.AppInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig appInfoDaoConfig;

    private final AppInfoDao appInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        appInfoDaoConfig = daoConfigMap.get(AppInfoDao.class).clone();
        appInfoDaoConfig.initIdentityScope(type);

        appInfoDao = new AppInfoDao(appInfoDaoConfig, this);

        registerDao(AppInfo.class, appInfoDao);
    }
    
    public void clear() {
        appInfoDaoConfig.clearIdentityScope();
    }

    public AppInfoDao getAppInfoDao() {
        return appInfoDao;
    }

}

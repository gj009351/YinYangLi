package com.duke.yinyangli.bean.database;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.duke.yinyangli.bean.database.Rgnm;
import com.duke.yinyangli.bean.database.Rysmn;
import com.duke.yinyangli.bean.database.ShuXiang;

import com.duke.yinyangli.bean.database.RgnmDao;
import com.duke.yinyangli.bean.database.RysmnDao;
import com.duke.yinyangli.bean.database.ShuXiangDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig rgnmDaoConfig;
    private final DaoConfig rysmnDaoConfig;
    private final DaoConfig shuXiangDaoConfig;

    private final RgnmDao rgnmDao;
    private final RysmnDao rysmnDao;
    private final ShuXiangDao shuXiangDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        rgnmDaoConfig = daoConfigMap.get(RgnmDao.class).clone();
        rgnmDaoConfig.initIdentityScope(type);

        rysmnDaoConfig = daoConfigMap.get(RysmnDao.class).clone();
        rysmnDaoConfig.initIdentityScope(type);

        shuXiangDaoConfig = daoConfigMap.get(ShuXiangDao.class).clone();
        shuXiangDaoConfig.initIdentityScope(type);

        rgnmDao = new RgnmDao(rgnmDaoConfig, this);
        rysmnDao = new RysmnDao(rysmnDaoConfig, this);
        shuXiangDao = new ShuXiangDao(shuXiangDaoConfig, this);

        registerDao(Rgnm.class, rgnmDao);
        registerDao(Rysmn.class, rysmnDao);
        registerDao(ShuXiang.class, shuXiangDao);
    }
    
    public void clear() {
        rgnmDaoConfig.clearIdentityScope();
        rysmnDaoConfig.clearIdentityScope();
        shuXiangDaoConfig.clearIdentityScope();
    }

    public RgnmDao getRgnmDao() {
        return rgnmDao;
    }

    public RysmnDao getRysmnDao() {
        return rysmnDao;
    }

    public ShuXiangDao getShuXiangDao() {
        return shuXiangDao;
    }

}
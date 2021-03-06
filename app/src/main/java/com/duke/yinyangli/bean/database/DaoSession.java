package com.duke.yinyangli.bean.database;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.duke.yinyangli.bean.database.Astro;
import com.duke.yinyangli.bean.database.Rgnm;
import com.duke.yinyangli.bean.database.Rysmn;
import com.duke.yinyangli.bean.database.ShuXiang;
import com.duke.yinyangli.bean.database.ShuXiangLove;
import com.duke.yinyangli.bean.database.XingZuo;
import com.duke.yinyangli.bean.database.XingZuoLove;
import com.duke.yinyangli.bean.database.Zgjm;
import com.duke.yinyangli.bean.database.Zhuge;

import com.duke.yinyangli.bean.database.AstroDao;
import com.duke.yinyangli.bean.database.RgnmDao;
import com.duke.yinyangli.bean.database.RysmnDao;
import com.duke.yinyangli.bean.database.ShuXiangDao;
import com.duke.yinyangli.bean.database.ShuXiangLoveDao;
import com.duke.yinyangli.bean.database.XingZuoDao;
import com.duke.yinyangli.bean.database.XingZuoLoveDao;
import com.duke.yinyangli.bean.database.ZgjmDao;
import com.duke.yinyangli.bean.database.ZhugeDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig astroDaoConfig;
    private final DaoConfig rgnmDaoConfig;
    private final DaoConfig rysmnDaoConfig;
    private final DaoConfig shuXiangDaoConfig;
    private final DaoConfig shuXiangLoveDaoConfig;
    private final DaoConfig xingZuoDaoConfig;
    private final DaoConfig xingZuoLoveDaoConfig;
    private final DaoConfig zgjmDaoConfig;
    private final DaoConfig zhugeDaoConfig;

    private final AstroDao astroDao;
    private final RgnmDao rgnmDao;
    private final RysmnDao rysmnDao;
    private final ShuXiangDao shuXiangDao;
    private final ShuXiangLoveDao shuXiangLoveDao;
    private final XingZuoDao xingZuoDao;
    private final XingZuoLoveDao xingZuoLoveDao;
    private final ZgjmDao zgjmDao;
    private final ZhugeDao zhugeDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        astroDaoConfig = daoConfigMap.get(AstroDao.class).clone();
        astroDaoConfig.initIdentityScope(type);

        rgnmDaoConfig = daoConfigMap.get(RgnmDao.class).clone();
        rgnmDaoConfig.initIdentityScope(type);

        rysmnDaoConfig = daoConfigMap.get(RysmnDao.class).clone();
        rysmnDaoConfig.initIdentityScope(type);

        shuXiangDaoConfig = daoConfigMap.get(ShuXiangDao.class).clone();
        shuXiangDaoConfig.initIdentityScope(type);

        shuXiangLoveDaoConfig = daoConfigMap.get(ShuXiangLoveDao.class).clone();
        shuXiangLoveDaoConfig.initIdentityScope(type);

        xingZuoDaoConfig = daoConfigMap.get(XingZuoDao.class).clone();
        xingZuoDaoConfig.initIdentityScope(type);

        xingZuoLoveDaoConfig = daoConfigMap.get(XingZuoLoveDao.class).clone();
        xingZuoLoveDaoConfig.initIdentityScope(type);

        zgjmDaoConfig = daoConfigMap.get(ZgjmDao.class).clone();
        zgjmDaoConfig.initIdentityScope(type);

        zhugeDaoConfig = daoConfigMap.get(ZhugeDao.class).clone();
        zhugeDaoConfig.initIdentityScope(type);

        astroDao = new AstroDao(astroDaoConfig, this);
        rgnmDao = new RgnmDao(rgnmDaoConfig, this);
        rysmnDao = new RysmnDao(rysmnDaoConfig, this);
        shuXiangDao = new ShuXiangDao(shuXiangDaoConfig, this);
        shuXiangLoveDao = new ShuXiangLoveDao(shuXiangLoveDaoConfig, this);
        xingZuoDao = new XingZuoDao(xingZuoDaoConfig, this);
        xingZuoLoveDao = new XingZuoLoveDao(xingZuoLoveDaoConfig, this);
        zgjmDao = new ZgjmDao(zgjmDaoConfig, this);
        zhugeDao = new ZhugeDao(zhugeDaoConfig, this);

        registerDao(Astro.class, astroDao);
        registerDao(Rgnm.class, rgnmDao);
        registerDao(Rysmn.class, rysmnDao);
        registerDao(ShuXiang.class, shuXiangDao);
        registerDao(ShuXiangLove.class, shuXiangLoveDao);
        registerDao(XingZuo.class, xingZuoDao);
        registerDao(XingZuoLove.class, xingZuoLoveDao);
        registerDao(Zgjm.class, zgjmDao);
        registerDao(Zhuge.class, zhugeDao);
    }
    
    public void clear() {
        astroDaoConfig.clearIdentityScope();
        rgnmDaoConfig.clearIdentityScope();
        rysmnDaoConfig.clearIdentityScope();
        shuXiangDaoConfig.clearIdentityScope();
        shuXiangLoveDaoConfig.clearIdentityScope();
        xingZuoDaoConfig.clearIdentityScope();
        xingZuoLoveDaoConfig.clearIdentityScope();
        zgjmDaoConfig.clearIdentityScope();
        zhugeDaoConfig.clearIdentityScope();
    }

    public AstroDao getAstroDao() {
        return astroDao;
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

    public ShuXiangLoveDao getShuXiangLoveDao() {
        return shuXiangLoveDao;
    }

    public XingZuoDao getXingZuoDao() {
        return xingZuoDao;
    }

    public XingZuoLoveDao getXingZuoLoveDao() {
        return xingZuoLoveDao;
    }

    public ZgjmDao getZgjmDao() {
        return zgjmDao;
    }

    public ZhugeDao getZhugeDao() {
        return zhugeDao;
    }

}

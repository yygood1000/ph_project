package com.topjet.common.db.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.topjet.common.db.bean.ImageMessageBean;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.db.bean.PhoneNumberBean;
import com.topjet.common.db.bean.SensitiveWordBean;

import com.topjet.common.db.greendao.ImageMessageBeanDao;
import com.topjet.common.db.greendao.IMUserBeanDao;
import com.topjet.common.db.greendao.PhoneNumberBeanDao;
import com.topjet.common.db.greendao.SensitiveWordBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig imageMessageBeanDaoConfig;
    private final DaoConfig iMUserBeanDaoConfig;
    private final DaoConfig phoneNumberBeanDaoConfig;
    private final DaoConfig sensitiveWordBeanDaoConfig;

    private final ImageMessageBeanDao imageMessageBeanDao;
    private final IMUserBeanDao iMUserBeanDao;
    private final PhoneNumberBeanDao phoneNumberBeanDao;
    private final SensitiveWordBeanDao sensitiveWordBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        imageMessageBeanDaoConfig = daoConfigMap.get(ImageMessageBeanDao.class).clone();
        imageMessageBeanDaoConfig.initIdentityScope(type);

        iMUserBeanDaoConfig = daoConfigMap.get(IMUserBeanDao.class).clone();
        iMUserBeanDaoConfig.initIdentityScope(type);

        phoneNumberBeanDaoConfig = daoConfigMap.get(PhoneNumberBeanDao.class).clone();
        phoneNumberBeanDaoConfig.initIdentityScope(type);

        sensitiveWordBeanDaoConfig = daoConfigMap.get(SensitiveWordBeanDao.class).clone();
        sensitiveWordBeanDaoConfig.initIdentityScope(type);

        imageMessageBeanDao = new ImageMessageBeanDao(imageMessageBeanDaoConfig, this);
        iMUserBeanDao = new IMUserBeanDao(iMUserBeanDaoConfig, this);
        phoneNumberBeanDao = new PhoneNumberBeanDao(phoneNumberBeanDaoConfig, this);
        sensitiveWordBeanDao = new SensitiveWordBeanDao(sensitiveWordBeanDaoConfig, this);

        registerDao(ImageMessageBean.class, imageMessageBeanDao);
        registerDao(IMUserBean.class, iMUserBeanDao);
        registerDao(PhoneNumberBean.class, phoneNumberBeanDao);
        registerDao(SensitiveWordBean.class, sensitiveWordBeanDao);
    }
    
    public void clear() {
        imageMessageBeanDaoConfig.clearIdentityScope();
        iMUserBeanDaoConfig.clearIdentityScope();
        phoneNumberBeanDaoConfig.clearIdentityScope();
        sensitiveWordBeanDaoConfig.clearIdentityScope();
    }

    public ImageMessageBeanDao getImageMessageBeanDao() {
        return imageMessageBeanDao;
    }

    public IMUserBeanDao getIMUserBeanDao() {
        return iMUserBeanDao;
    }

    public PhoneNumberBeanDao getPhoneNumberBeanDao() {
        return phoneNumberBeanDao;
    }

    public SensitiveWordBeanDao getSensitiveWordBeanDao() {
        return sensitiveWordBeanDao;
    }

}
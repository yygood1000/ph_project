package com.topjet.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.topjet.common.App;
import com.topjet.common.db.greendao.DaoMaster;
import com.topjet.common.db.greendao.DaoSession;


/**
 * creator: zhulunjun
 * time:    2017/11/28
 * describe: 数据库管理类
 */

public class DBManager {
    private final static String dbName = "db_560";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;

    public DBManager() {
        init();
    }


    public void init(){
        openHelper = new DaoMaster.DevOpenHelper(App.getInstance(), dbName);
        daoMaster = new DaoMaster(openHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        db = openHelper.getWritableDatabase();
    }

    /**
     * 获取单例引用
     *
     */
    public static DBManager getInstance() {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }
}

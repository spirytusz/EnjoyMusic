package com.zspirytus.enjoymusic.db;

import android.database.sqlite.SQLiteDatabase;

import com.zspirytus.enjoymusic.db.greendao.DaoMaster;
import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.global.MainApplication;

public class DBManager {

    private SQLiteDatabase mDb;
    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static class Singleton {
        static DBManager INSTANCE = new DBManager();
    }

    private DBManager() {
    }

    public static DBManager getInstance() {
        return Singleton.INSTANCE;
    }

    public void init(MainApplication application) {
        mHelper = new DaoMaster.DevOpenHelper(application, "db_enjoymusic", null);
        mDb = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDataBase() {
        return mDb;
    }
}

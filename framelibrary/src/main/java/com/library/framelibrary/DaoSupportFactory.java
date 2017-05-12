package com.library.framelibrary;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.library.baselibrary.database.DaoSupport;
import com.library.baselibrary.database.IDaoSupport;

import java.io.File;

/**
 * Created by 花歹 on 2017/5/8.
 * Email:   gatsbywang@126.com
 * Description: 工厂模式+单例
 * Thought:
 */

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;
    private final String TAG = DaoSupportFactory.class.getName();
    private SQLiteDatabase mSqLiteDatabase;

    //持有外部数据库的引用
    private DaoSupportFactory() {

        //数据库存储到内存卡中 1、需要判断内存卡的状态 2、6.0以上需要动态申请权限
        File dbDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "rental" + File.separator + "database");

        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        //创建数据库文件
        File dbFile = new File(dbDir, "rental.db");
        Log.e(TAG, dbFile.getAbsolutePath());
        //打开或者创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public static DaoSupportFactory getFactory() {
        synchronized (DaoSupportFactory.class) {
            if (mFactory == null) {
                mFactory = new DaoSupportFactory();
            }
            return mFactory;
        }
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport = new DaoSupport();
        daoSupport.init(mSqLiteDatabase, clazz);
        return daoSupport;
    }

}

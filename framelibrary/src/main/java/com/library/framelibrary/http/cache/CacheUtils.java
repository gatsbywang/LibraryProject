package com.library.framelibrary.http.cache;

import android.util.Base64;

import com.library.baselibrary.database.IDaoSupport;
import com.library.framelibrary.db.DaoSupportFactory;

import java.util.List;

/**
 * Created by 花歹 on 2017/5/14.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class CacheUtils {

    public static String getLocalResultJson(String column) {
        //需要缓存，从数据库取
        IDaoSupport<CacheData> cacheDao = DaoSupportFactory.getFactory().getDao(CacheData.class);
        //查询数据库数据，column为http:xx，这会出问题，需要转换
        List<CacheData> caches = cacheDao.querySupport()
                .selection("mUrlKey=?")
                .selectionArgs(Base64.encodeToString(column.getBytes(),Base64.DEFAULT))
                .query();

        //判断caches有没有数据，再取
        if (!caches.isEmpty()) {
            CacheData cacheData = caches.get(0);
            return cacheData.getResultJson();
        }
        return null;
    }

    public static long saveLocalData(String column, String param) {
        //缓存数据
        IDaoSupport<CacheData> cacheDao = DaoSupportFactory.getFactory().getDao(CacheData.class);
        //先删除
        cacheDao.delete("mUrlKey=?", Base64.encodeToString(column.getBytes(),Base64.DEFAULT));
        //再缓存
        long insert = cacheDao.insert(new CacheData(Base64.encodeToString(column.getBytes(),Base64.DEFAULT), param));
        return insert;
    }

}

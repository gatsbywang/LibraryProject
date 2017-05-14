package com.library.baselibrary.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by 花歹 on 2017/5/8.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought: 定义Dao的规范
 */

public interface IDaoSupport<T> {

    /**
     * 初始化
     *
     * @param sqLiteDatabase
     * @param clazz
     */
    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    /**
     * 插入数据
     *
     * @param t
     * @return
     */
    public long insert(T t);

    /**
     * 批量插入数据
     *
     * @param datas
     * @return
     */
    public void insert(List<T> datas);


//    /**
//     * TODO 查询，优化成下面这种方式
//     * List<Person> persons = daoSupport.querySupport().queryAll();
//     * List<Person> persons = daoSupport.querySupport()
//     * .selection("age = ?").selectionArgs("23").query();
//     *
//     * @return
//     */
//    public List<T> query();


    /**
     * 获取专门查询的支持类
     * @return
     */
    QuerySupport<T> querySupport();

    /**
     * 删除
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int delete(String whereClause, String... whereArgs);

    /**
     * 更新
     *
     * @param obj
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int update(T obj, String whereClause, String... whereArgs);

}

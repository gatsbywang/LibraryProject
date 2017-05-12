package com.library.baselibrary.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 花歹 on 2017/5/12.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */
public class QuerySupport<T> {

    private final Class<T> mClazz;

    private final SQLiteDatabase mSQLiteDatabase;

    //查询的列
    private String[] mQueryColumns;

    //查询的参数，如23 与Selection的？相对应
    private String[] mQuerySelectionArgs;

    //查询的条件 如age = ?
    private String mQuerySelection;

    //查询分组
    private String mQueryGroupBy;

    //查询对结果集进行过滤
    private String mQueryHaving;

    //查询排序
    private String mQueryOrderBy;

    //查询可用于分页
    private String mQueryLimit;

    public QuerySupport(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mClazz = clazz;
        this.mSQLiteDatabase = sqLiteDatabase;
    }

    /*Cursor query(boolean distinct, String table, String[] columns,
                         String selection, String[] selectionArgs, String groupBy,
                         String having, String orderBy, String limit, CancellationSignal cancellationSignal)
 */
    public QuerySupport columns(String... columns) {
        this.mQueryColumns = columns;
        return this;
    }

    public QuerySupport selection(String selection) {
        this.mQuerySelection = selection;
        return this;
    }

    public QuerySupport selectionArgs(String... selectionArgs) {
        this.mQuerySelectionArgs = selectionArgs;
        return this;
    }

    public QuerySupport groupBy(String groupBy) {
        this.mQueryGroupBy = groupBy;
        return this;
    }

    public QuerySupport having(String having) {
        this.mQueryHaving = having;
        return this;
    }

    public QuerySupport orderBy(String orderBy) {
        this.mQueryOrderBy = orderBy;
        return this;
    }

    public QuerySupport limit(String limit) {
        this.mQueryLimit = limit;
        return this;
    }


    public List<T> query() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtils.getTableName(mClazz), mQueryColumns, mQuerySelection,
                mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy, mQueryLimit);
        clearQueryParams();
        return cursorToList(cursor);

    }

    public List<T> queryAll() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtils.getTableName(mClazz), null, null, null, null, null, null);
        return cursorToList(cursor);
    }

    /**
     * 清空参数
     */
    private void clearQueryParams() {
        mQueryColumns = null;
        mQuerySelectionArgs = null;
        mQuerySelection = null;
        mQueryGroupBy = null;
        mQueryHaving = null;
        mQueryLimit = null;
        mQueryOrderBy = null;
    }


    /**
     * 将cursor中的数据转换为List集合
     *
     * @param cursor
     * @return
     */
    private List<T> cursorToList(Cursor cursor) {

        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            //不断从游标获取数据
            do {
                try {
                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String name = field.getName();

                        //获取在第几列
                        int columnIndex = cursor.getColumnIndex(name);
                        if (columnIndex == -1) {

                        }

                        Method cursorMethod = cursorMethod(field.getType());

                        if (cursorMethod != null) {
                            //通过反射执行方法,得到
                            Object value = cursorMethod.invoke(cursor, columnIndex);
                            if (value == null) {
                                continue;
                            }

                            //处理特殊部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                if ("0".equals(String.valueOf(value))) {
                                    value = false;
                                } else if ("1".equals(String.valueOf(value))) {
                                    value = true;
                                }
                            } else if (field.getType() == char.class || field.getType() == Character.class) {
                                value = ((String) value).charAt(0);

                            } else if (field.getType() == Date.class) {
                                long date = (long) value;
                                if (date < 0) {
                                    value = null;
                                } else {
                                    value = new Date(date);
                                }
                            }

                            //反射注入
                            field.set(instance, value);
                        }
                    }


                    //
                    list.add(instance);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 通过类型获取游标的方法（如curosr.getInt,还是curosr.getLong）
     *
     * @param type
     * @return
     */
    private Method cursorMethod(Class<?> type) throws NoSuchMethodException {
        //cursor.getInt(),cursor.getLong，返回的的是getInt或者getgLong，或者其他
        String methodName = getColumnMethodName(type);

        Method method = Cursor.class.getMethod(methodName, int.class);
        return method;
    }

    /**
     * 拼接cursor方法，如getLong,则应该为"get"+"Long"
     *
     * @param type T的属性变量类型
     * @return
     */
    private String getColumnMethodName(Class<?> type) {
        String typeName;
        if (type.isPrimitive()) {
            typeName = DaoUtils.capitalize(type.getName());
        } else {
            typeName = type.getSimpleName();
        }
        String methodName = "get" + typeName;
        switch (methodName) {
            case "getBoolean":
                methodName = "getInt";
                break;
            case "getChar":
            case "getCharacter":
                methodName = "getString";
                break;
            case "getDate":
                methodName = "getLong";
                break;
            case "getInteger":
                methodName = "getInt";
                break;
        }
        return methodName;
    }

}

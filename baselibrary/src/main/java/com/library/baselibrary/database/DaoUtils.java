package com.library.baselibrary.database;

/**
 * Created by 花歹 on 2017/5/9.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class DaoUtils {

    private static final String TAG = DaoUtils.class.getName();

    public static String getColumnType(String type) {
        String value = null;
        switch (type) {
            case "String":
                value = " text";
                break;
            case "int":
            case "Integer":
                value = " integer";
                break;
            case "boolean":
            case "Boolean":
                value = " boolean";
                break;
            case "float":
            case "Float":
                value = " float";
                break;
            case "double":
            case "Double":
                value = " double";
                break;
            case "char":
            case "Character":
                value = " varchar";
                break;
            case "long":
            case "Long":
                value = " long";
                break;

        }
//        if (value == null) {
//            throw new IllegalArgumentException(type + " not support! ");
//        }
        return value;
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String capitalize(String fieldType) {
        switch (fieldType) {
            case "int":
                fieldType = "Int";
                break;
            case "long":
                fieldType = "Long";
                break;
            case "float":
                fieldType = "Float";
                break;
            case "double":
                fieldType = "Double";
                break;
            case "char":
                fieldType = "Char";
                break;
            case "byte":
                fieldType = "Byte";
                break;
            case "short":
                fieldType = "Short";
                break;
            case "boolean":
                fieldType = "Boolean";
                break;
        }
        return fieldType;
    }
}

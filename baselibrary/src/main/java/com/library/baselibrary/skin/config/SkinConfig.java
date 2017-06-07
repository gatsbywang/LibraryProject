package com.library.baselibrary.skin.config;

/**
 * Created by 花歹 on 2017/6/6.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class SkinConfig {
    //sp 的文件名称
    public static final String SKIN_INFO_NAME = "skinInfo";
    //保存文件的路径
    public static final String SKIN_PATH_NAME = "skinPath";
    //没有皮肤
    public static final int SKIN_CHANGE_NOTHING = -1;
    //换肤成功
    public static final int SKIN_CHANGE_SUCCESS = 1;
    //代表文件不存在
    public static final int SKIN_FILE_NOEXIST = -2;
    //皮肤文件有错误，可能不是一个apk文件
    public static final int SKIN_FILE_ERROR = -3;
}

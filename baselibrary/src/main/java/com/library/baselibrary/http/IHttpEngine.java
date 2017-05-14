package com.library.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by 花歹 on 2017/5/4.
 * Email:   gatsbywang@126.com
 * Description: http引擎规范
 * Thought: get和post请求，三个参数：url,map,callback
 */

public interface IHttpEngine {

    //get请求
    void get(boolean isCache, Context context, String url, Map<String, String> header, Map<String, Object> params, EngineCallback callback);

    //post请求
    void post(boolean isCache, Context context, String url, Map<String, String> header, Map<String, Object> params, EngineCallback callback);
    //上传

    //下载

    //https
}

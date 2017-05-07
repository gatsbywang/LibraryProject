package com.library.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by 花歹 on 2017/5/4.
 * Email:   gatsbywang@126.com
 * Description: 引擎回调
 * Thinking: 为啥不进行封装？比如加个泛型T，让result返回T。为了避免奇葩的后台返回，success: data{"xx","xx"}
 * error :data:"",成功返回object,失败返回string。
 */

public interface EngineCallback {

    /**
     * 执行请求前所需要完成的事情，可以在此方法里进行处理
     *
     * @param context
     * @param header
     * @param params
     */
    void onPreExecute(Context context, Map<String, String> header, Map<String, Object> params);

    void onError(Exception e);

    void onSuccess(String result);

    public final EngineCallback DEFAULT_CALL_BACK = new EngineCallback() {
        @Override
        public void onPreExecute(Context context, Map<String, String> header, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}

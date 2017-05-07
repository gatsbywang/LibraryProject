package com.library.framelibrary;

import android.content.Context;

import com.library.baselibrary.http.EngineCallback;

import java.util.Map;

/**
 * Created by 花歹 on 2017/5/7.
 * Email:   gatsbywang@126.com
 * Description: 带有业务的回调
 * Thought:
 */

public abstract class HttpCallback implements EngineCallback {
    @Override
    public final void onPreExecute(Context context, Map<String, String> header, Map<String, Object> params) {
        //请求前的做的事情，如公共参数（公共参数涉及到了业务，公共参数的添加不能写到HttpUtils里）

        onPreExecute();
    }

    public void onPreExecute() {

    }
}

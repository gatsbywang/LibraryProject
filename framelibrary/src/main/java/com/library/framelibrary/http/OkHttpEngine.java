package com.library.framelibrary.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.library.baselibrary.http.EngineCallback;
import com.library.baselibrary.http.HttpUtils;
import com.library.baselibrary.http.IHttpEngine;
import com.library.framelibrary.http.cache.CacheUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 花歹 on 2017/5/5.
 * Email:   gatsbywang@126.com
 * Description: okhttp引擎
 * Thought: 缓存 只针对某些
 */

public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();
    private final String TAG = OkHttpEngine.class.getName();

    @Override
    public void get(final boolean isCache, Context context, final String url, Map<String, String> header, Map<String, Object> params, final EngineCallback callback) {
        final String finalUrl = HttpUtils.jointParams(url, params);
        Log.e(TAG, "[Request] Url: " + finalUrl + "\n" + "Params: " + params.toString() + "\n"
                + "Type: Get");

        //1、判断是否需要缓存,TODO 这种缓存方式只能针对特定情况
        if (isCache) {
            String localResultJson = CacheUtils.getLocalResultJson(finalUrl);
            if (!TextUtils.isEmpty(localResultJson)) {
                callback.onSuccess(CacheUtils.getLocalResultJson(finalUrl));
            }
        }

        final Request request = new Request.Builder().url(finalUrl).tag(context).get().build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "[Response] result: failure" + "\n" + e.getMessage() + "\n"
                        + "Type: Get");
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                //1、每次获取到数据判断是否需要缓存
                //1、判断是否需要缓存
                if (isCache) {
                    //2、取出缓存比对上次缓存的内容
                    String resultJsonCache = CacheUtils.getLocalResultJson(finalUrl);
                    if (!TextUtils.isEmpty(resultJsonCache)) {
                        if (resultJsonCache.equals(result)) {
                            //TODO 比对成功不需要进行缓存,但需要回调
                            return;
                        }
                    }
                }
                callback.onSuccess(result);
                if (isCache) {
                    CacheUtils.saveLocalData(finalUrl, result);
                }
                Log.i(TAG, "[Response] result: success" + "\n"
                        + result + "\n" + "Type: Get");
            }
        });
    }

    @Override
    public void post(boolean isCache, Context context, String url, Map<String, String> header, Map<String, Object> params, final EngineCallback callback) {
        String jointUrl = HttpUtils.jointParams(url, params);
        Log.e(TAG, "[Request] Url: " + url + "\n" + "Params: " + params.toString() + "\n"
                + "Type: Post");


        RequestBody requestBody = appendBody(params);
        final Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //非主线程
                Log.i(TAG, "[Response] result: failure" + "\n" + e.getMessage() + "\n"
                        + "Type: Post");
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //非主线程
                String result = response.body().string();
                Log.i(TAG, "[Response] result: success" + "\n"
                        + result + "\n" + "Type: Post");
                callback.onSuccess(response.body().string());
            }
        });
    }

    /**
     * 获取请求body
     *
     * @param params
     * @return
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    /**
     * 封装参数
     *
     * @param builder
     * @param params
     */
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
//                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(
                            MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                } else if (value instanceof List) {

                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 获取文件类型
     *
     * @param path
     * @return
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

}

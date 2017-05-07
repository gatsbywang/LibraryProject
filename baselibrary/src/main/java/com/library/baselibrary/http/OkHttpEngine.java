package com.library.baselibrary.http;

import android.content.Context;
import android.util.Log;

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
 * Thought:
 */

public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();
    private final String TAG = OkHttpEngine.class.getName();

    @Override
    public void get(Context context, String url, Map<String, String> header, Map<String, Object> params, final EngineCallback callback) {
        url = HttpUtils.jointParams(url, params);
        Log.e(TAG, "[Request] Url: " + url + "\n" + "Params: " + params.toString() + "\n"
                + "Type: Get");
        final Request request = new Request.Builder().url(url).tag(context).get().build();
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
                Log.i(TAG, "[Response] result: success" + "\n"
                        + result + "\n" + "Type: Get");
                callback.onSuccess(response.body().string());
            }
        });
    }

    @Override
    public void post(Context context, String url, Map<String, String> header, Map<String, Object> params, final EngineCallback callback) {
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

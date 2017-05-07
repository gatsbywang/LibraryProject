package com.library.baselibrary.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by 花歹 on 2017/5/4.
 * Email:   gatsbywang@126.com
 * Description: Http工具类
 * Thought: http重要两个指标，1、回调 2、请求。
 */

public class HttpUtils {

    //默认引擎
    private static IHttpEngine mHttpEngine = new OkHttpEngine();


    private final Context mContext;

    //请求url
    private String mUrl;

    //请求参数
    private Map<String, Object> mParams;

    //请求头
    private Map<String, String> mHeader;

    //请求方式
    private int mType;

    private static final int POST_TYPE = 0x0011;

    private static final int GET_TYPE = 0x0012;

    private HttpUtils(Context context) {
        this.mContext = context;
        mParams = new ArrayMap<>();
        mHeader = new ArrayMap<>();
    }

    //在application初始化引擎
    public static void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    /**
     * 链式调用 初始化httputil
     *
     * @param context
     * @return
     */
    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    /**
     * 构造url
     *
     * @param url
     * @return
     */
    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    /**
     * 构造头部
     *
     * @param header
     * @return
     */
    public HttpUtils header(Map<String, String> header) {
        this.mHeader.putAll(header);
        return this;
    }

    /**
     * 构造参数
     *
     * @param params
     * @return
     */
    public HttpUtils params(Map<String, Object> params) {
        this.mParams.putAll(params);
        return this;
    }

    /**
     * 构造参数
     *
     * @param key
     * @param value
     * @return
     */
    public HttpUtils params(String key, Object value) {
        this.mParams.put(key, value);
        return this;
    }

    /**
     * get请求
     *
     * @return
     */
    public HttpUtils get() {
        this.mType = GET_TYPE;
        return this;
    }

    /**
     * post请求
     *
     * @return
     */
    public HttpUtils post() {
        this.mType = POST_TYPE;
        return this;
    }

    /**
     * 执行请求
     *
     * @param callback
     */
    public void execute(EngineCallback callback) {
        if (callback == null) {
            callback = EngineCallback.DEFAULT_CALL_BACK;
        }
        callback.onPreExecute(mContext, mHeader, mParams);
        switch (mType) {
            case GET_TYPE:
                get(mUrl, mHeader, mParams, callback);
                break;

            case POST_TYPE:
                post(mUrl, mHeader, mParams, callback);
                break;
        }
    }

    /**
     * 无回调的请求
     */
    public void execute() {
        execute(null);
    }

    /**
     * 设置http引擎
     *
     * @param httpEngine
     */
    public void exchangeEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    /**
     * 调用引擎的get请求
     *
     * @param url
     * @param header
     * @param params
     * @param callback
     */
    private void get(String url, Map<String, String> header, Map<String, Object> params, EngineCallback callback) {
        mHttpEngine.get(mContext, url, header, params, callback);
    }

    /**
     * 调用引擎的post请求
     *
     * @param url
     * @param header
     * @param params
     * @param callback
     */
    private void post(String url, Map<String, String> header, Map<String, Object> params, EngineCallback callback) {
        mHttpEngine.post(mContext, url, header, params, callback);
    }

    /**
     * 将url与参数拼接
     *
     * @param url
     * @param params
     * @return
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();

    }

    /**
     * 获取父类的class
     *
     * @param object
     * @return
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}

package com.library.framelibrary.http.cache;

/**
 * Created by 花歹 on 2017/5/14.
 * Email:   gatsbywang@126.com
 * Description: 缓存类
 * Thought:
 */

public class CacheData {


    //请求链接
    private String mUrlKey;

    //后台返回json数据
    private String mResultJson;

    public CacheData() {
    }

    public CacheData(String urlKey, String resultJson) {
        this.mUrlKey = urlKey;
        this.mResultJson = resultJson;
    }

    public String getUrlKey() {
        return mUrlKey;
    }

    public void setUrlKey(String urlKey) {
        this.mUrlKey = urlKey;
    }

    public String getResultJson() {
        return mResultJson;
    }

    public void setResultJson(String resultJson) {
        this.mResultJson = resultJson;
    }
}

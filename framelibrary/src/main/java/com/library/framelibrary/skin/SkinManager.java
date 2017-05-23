package com.library.framelibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.ArrayMap;


import com.library.framelibrary.skin.attr.SkinView;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 花歹 on 2017/5/22.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class SkinManager {
    private static SkinManager mInstance;

    private Context mContext;

    private Map<Activity, List<SkinView>> mSkinViews
            = new ArrayMap<>();


    static {
        mInstance = new SkinManager();
    }

    private SkinResource mSkinResource;

    public static SkinManager getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 加载皮肤
     *
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {
        //校验签名（增量更新）

        //初始化资源管理器
        mSkinResource = new SkinResource(mContext, skinPath);

        //改变皮肤
        Set<Activity> activities = mSkinViews.keySet();
        for (Activity activity : activities) {
            List<SkinView> skinViews = mSkinViews.get(activity);

            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
        return 0;
    }

    /**
     * 恢复默认
     *
     * @return
     */
    public int restoreDefault() {

        return 0;
    }

    /**
     * 通过Activity 获取skinviews
     *
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    /**
     * 注册
     *
     * @param activity
     * @param skinViews
     */
    public void register(Activity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity, skinViews);
    }

    public SkinResource getSkinResource() {
        return mSkinResource;
    }
}

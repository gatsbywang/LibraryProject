package com.library.baselibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.library.baselibrary.skin.attr.SkinView;
import com.library.baselibrary.skin.callback.ISkinChangeListener;
import com.library.baselibrary.skin.config.SkinConfig;
import com.library.baselibrary.skin.config.SkinPreUtils;

import java.io.File;
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

    private Map<ISkinChangeListener, List<SkinView>> mSkinViews
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
        //为了避免皮肤被恶意删除，需要做一些措施
        String currentSkinPath = SkinPreUtils.getInstance(context).getSkinPath();
        File file = new File(currentSkinPath);
        if (!file.exists()) {
            //不存在，清空皮肤
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }

        //最好做一下 能不能获取包名
        String packageName = context.getPackageManager()
                .getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES)
                .packageName;
        if (TextUtils.isEmpty(packageName)) {
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }

        //为了恶意破坏，需要校验签名

        //做一些初始化的工作
        mSkinResource = new SkinResource(mContext, currentSkinPath);
    }

    /**
     * 加载皮肤
     *
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {

        //判断皮肤文件是否存在
        File file = new File(skinPath);
        if (!file.exists()) {
            //不存在，
            return SkinConfig.SKIN_FILE_NOEXIST;
        }

        //最好做一下 能不能获取包名
        String packageName = mContext.getPackageManager()
                .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                .packageName;
        if (TextUtils.isEmpty(packageName)) {
            return SkinConfig.SKIN_FILE_ERROR;
        }

        //1、如果当前皮肤是一样就不要换
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (skinPath.equals(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        //校验签名（增量更新）

        //初始化资源管理器
        mSkinResource = new SkinResource(mContext, skinPath);

        //改变皮肤
        changeSkin();

        //保存皮肤的状态
        saveSkinStatus(skinPath);
        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 改变皮肤
     */
    private void changeSkin() {

        Set<ISkinChangeListener> listeners = mSkinViews.keySet();
        for (ISkinChangeListener key : listeners) {
            List<SkinView> skinViews = mSkinViews.get(key);

            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
            //通知activity
            key.changerSkin(mSkinResource);
        }

    }


    private void saveSkinStatus(String skinPath) {
        //注意一点 不要嵌套（加入此处用上次写的数据库保存，就嵌套library了）
        SkinPreUtils.getInstance(mContext).saveSkinPath(skinPath);
    }

    /**
     * 恢复默认
     *
     * @return
     */
    public int restoreDefault() {
        //判断当前有没有皮肤，没有皮肤就不要执行任何方法
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        //当前运行的app的apk路径
        String skinPath = mContext.getPackageResourcePath();
        mSkinResource = new SkinResource(mContext, skinPath);

        //改变皮肤
        changeSkin();

        //把皮肤信息清空
        SkinPreUtils.getInstance(mContext).clearSkinInfo();

        return SkinConfig.SKIN_CHANGE_SUCCESS;
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
     * @param skinChangeListener
     * @param skinViews
     */
    public void register(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mSkinViews.put(skinChangeListener, skinViews);
    }

    /**
     * 获取当前的皮肤资源
     *
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }


    /**
     * 检测要不要换肤
     *
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        //如果当前有皮肤，也就是保存了皮肤路径，就换以下皮肤
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (!TextUtils.isEmpty(currentSkinPath)) {
            //不是空 就换一下
            skinView.skin();
        }
    }

    /**
     * 防止内存泄露
     *
     * @param skinChangeListener
     */
    public void unRegister(ISkinChangeListener skinChangeListener) {
        mSkinViews.remove(skinChangeListener);
    }
}

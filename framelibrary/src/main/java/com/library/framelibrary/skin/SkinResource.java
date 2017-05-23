package com.library.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 花歹 on 2017/5/22.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class SkinResource {
    private String mPackageName;
    //本地下载的皮肤资源，通过此resource获取
    private Resources mSkinResource;

    public SkinResource(Context context, String skinPath) {
        try {
            //拿本地resource
            Resources superResource = context.getResources();
            //创建AssertManager
            AssetManager assetManager = AssetManager.class.newInstance();

            //获取addAssetPath方法
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath",
                    String.class);

            addAssetPathMethod.setAccessible(true);

            //反射执行方法，添加本地下载的皮肤
            addAssetPathMethod.invoke(assetManager, Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "change.skin");
            //创建属于皮肤的resource
            mSkinResource = new Resources(assetManager, superResource.getDisplayMetrics(),
                    superResource.getConfiguration());

            //获取包名
            mPackageName = context.getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                    .packageName;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过名字获取drawable
     *
     * @param resName
     * @return
     */
    public Drawable getDrawablebyName(String resName) {
        try {
            //TODO 注意mipmap的情况
            int resId = mSkinResource.getIdentifier(resName, "drawable", mPackageName);
            Drawable drawable = mSkinResource.getDrawable(resId);
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过名字获取颜色
     *
     * @param resName
     * @return
     */
    public ColorStateList getColorbyName(String resName) {
        try {
            int resId = mSkinResource.getIdentifier(resName, "color", mPackageName);
            ColorStateList colorStateList = mSkinResource.getColorStateList(resId);
            return colorStateList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

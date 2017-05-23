package com.library.framelibrary.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.library.framelibrary.skin.attr.SkinAttr;
import com.library.framelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 花歹 on 2017/5/22.
 * Email:   gatsbywang@126.com
 * Description: 皮肤属性解析的支持类
 * Thought:
 */

public class SkinAttrSupport {

    private static final String TAG = SkinAttrSupport.class.getName();

    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //background src textColor
        List<SkinAttr> skinAttrs = new ArrayList<>();

        int attrLength = attrs.getAttributeCount();
        for (int i = 0; i < attrLength; i++) {
            // 获取名称
            String attrName = attrs.getAttributeName(i);
            //获取值
            String attrValue = attrs.getAttributeValue(i);
            Log.e(TAG, "attrName - >" + attrName + "\n" +
                    "attrValue - >" + attrValue);
            SkinType skinType = getSkinType(attrName);
            if (skinType != null) {
                //资源名称 attrValue是一个@int类型
                String resName = getResName(context, attrValue);
                if (TextUtils.isEmpty(resName)) {
                    continue;
                }

                SkinAttr skinAttr = new SkinAttr(resName, skinType);
                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }

    /**
     * 获取资源名称（AssertManager的源码解析）
     *
     * @param context
     * @param attrValue
     * @return
     */
    private static String getResName(Context context, String attrValue) {

        if (attrValue.startsWith("@")) {
            attrValue = attrValue.substring(1);
            int resId = Integer.parseInt(attrValue);
            return context.getResources().getResourceEntryName(resId);

        }
        return null;
    }

    /**
     * 通过名称 获取 SkinType
     *
     * @param attrName
     * @return
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if (skinType.getResName().equals(attrName)) {
                return skinType;
            }
        }
        return null;
    }
}

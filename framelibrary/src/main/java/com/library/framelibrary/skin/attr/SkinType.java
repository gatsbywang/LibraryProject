package com.library.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.framelibrary.skin.SkinManager;
import com.library.framelibrary.skin.SkinResource;


/**
 * Created by 花歹 on 2017/5/22.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public enum SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorbyName(resName);
            if (color == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setTextColor(color);
        }
    },
    BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            //背景可能是图片，也有可能是颜色
            //图片情况
            Drawable drawable = skinResource.getDrawablebyName(resName);
            if (drawable != null) {
                view.setBackground(drawable);
                return;
            }
            //颜色情况
            ColorStateList color = skinResource.getColorbyName(resName);
            if (color != null) {
                view.setBackgroundColor(color.getDefaultColor());
            }

        }
    },
    SRC("src") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawablebyName(resName);
            if (drawable != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
            }
        }
    };

    private static SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }

    private String mResName;

    SkinType(String resName) {
        this.mResName = resName;
    }

    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }
}

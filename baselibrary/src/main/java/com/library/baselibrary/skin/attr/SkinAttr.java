package com.library.baselibrary.skin.attr;

import android.view.View;

/**
 * Created by 花歹 on 2017/5/22.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class SkinAttr {
    private String mResName;
    private SkinType mType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName = resName;
        this.mType = skinType;
    }

    public void skin(View view) {
        mType.skin(view, mResName);
    }

}

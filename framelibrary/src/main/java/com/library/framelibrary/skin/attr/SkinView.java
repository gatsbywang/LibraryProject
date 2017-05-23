package com.library.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by 花歹 on 2017/5/22.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class SkinView {
    private View mView;
    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttrs = skinAttrs;
    }

    public void skin() {
        for (SkinAttr attr : mSkinAttrs) {
            attr.skin(mView);
        }
    }
}

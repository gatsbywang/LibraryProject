package com.library.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.library.widget.R;

/**
 * Created by 花歹 on 2017/5/2.
 * Email:   gatsbywang@126.com
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {
    public DefaultNavigationBar(Builder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.layout_bar;
    }

    @Override
    public void applyView() {
        setText(R.id.center, getParams().mTitle);
        setText(R.id.right, getParams().mRightText);

        setOnClickListener(R.id.right, getParams().mRightClickListener);
        setOnClickListener(R.id.left, getParams().mLeftClickListener);
    }


    public static class Builder extends AbsNavigationBar.Builder<DefaultNavigationBar> {

        private final DefaultNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
            P = new DefaultNavigationParams(context, null);
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        /**
         * 设置右边文本
         *
         * @param text
         * @return
         */
        public Builder setRightText(String text) {
            P.mRightText = text;
            return this;
        }

        /**
         * 设置右边文本
         *
         * @param resId
         * @return
         */
        public Builder setRightIcon(int resId) {
            P.mRightResId = resId;
            return this;
        }

        /**
         * 设置右边点击
         *
         * @param listener
         * @return
         */
        public Builder setRightClickListener(View.OnClickListener listener) {
            P.mRightClickListener = listener;
            return this;
        }

        /**
         * 设置右边点击,默认为返回
         *
         * @param listener
         * @return
         */
        public Builder setLeftClickListener(View.OnClickListener listener) {
            P.mLeftClickListener = listener;
            return this;
        }

        @Override
        public DefaultNavigationBar build() {
            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(P);
            return defaultNavigationBar;
        }

        public static class DefaultNavigationParams extends AbsNavigationParams {

            public String mTitle;

            public String mRightText;

            public int mRightResId;

            public View.OnClickListener mRightClickListener;

            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关闭
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).onBackPressed();
                    }
                }
            };

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}

package com.library.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by 花歹 on 2017/5/2.
 * Email:   gatsbywang@126.com
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;
    private View mNavigationBar;

    public AbsNavigationBar(P params) {
        mParams = params;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    public void setText(int viewId, String text) {
        TextView textView = findViewById(viewId);
        if (textView != null && !TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        if (view != null && listener != null) {
            view.setOnClickListener(listener);
        }
    }

    public <T extends View> T findViewById(int viewId) {
        View view = mNavigationBar.findViewById(viewId);
        return (T) view;
    }

    /**
     * 绑定和创建view
     */
    private void createAndBindView() {
        if (mParams.mParent == null) {
            if (mParams.mContext instanceof Activity) {
//                ViewGroup root = (ViewGroup) ((Activity) (mParams.mContext)).findViewById(android.R.id.content);
                ViewGroup root = (ViewGroup) ((Activity) (mParams.mContext)).getWindow().getDecorView();
                mParams.mParent = (ViewGroup) root.getChildAt(0);
            }
        }

        mNavigationBar = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(),
                mParams.mParent, false);
        mParams.mParent.addView(mNavigationBar, 0);
        applyView();
    }

    /**
     * builder 类
     *
     * @param <T>
     */
    public abstract static class Builder<T extends AbsNavigationBar> {

        public Builder(Context context, ViewGroup parent) {

        }

        /**
         * 实例化AbsNavigationBar
         *
         * @return
         */
        public abstract T build();


        /**
         * 参数类
         */
        public static class AbsNavigationParams {

            protected final Context mContext;
            protected ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }

    }
}

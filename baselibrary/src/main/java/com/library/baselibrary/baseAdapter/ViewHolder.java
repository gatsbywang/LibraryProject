package com.library.baselibrary.baseAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 花歹 on 2017/4/24.
 * Email:   gatsbywang@126.com
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;
    }


    //通用功能封装

    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    public ViewHolder setImageUri(int viewId, HolderImageLoader imageLoader) {
        ImageView imageView = getView(viewId);
        imageLoader.loadImage(imageView, imageLoader.getPath());
        return this;
    }

    public abstract class HolderImageLoader {

        private String mPath;

        public HolderImageLoader(String path) {
            mPath = path;
        }

        public abstract void loadImage(ImageView imageView, String path);

        public String getPath() {
            return mPath;
        }
    }


}

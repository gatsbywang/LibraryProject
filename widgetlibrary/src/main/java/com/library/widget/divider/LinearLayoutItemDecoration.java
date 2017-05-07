package com.library.widget.divider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 只能应对垂直列表
 * Created by 花歹 on 2017/4/21.
 * Email:   gatsbywang@126.com
 */

public class LinearLayoutItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDrivider;

    public LinearLayoutItemDecoration(Context context, int drawableResourceId) {
        mDrivider = ContextCompat.getDrawable(context, drawableResourceId);
    }

    //留出位置
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //代表在每个底部的位置留出10px绘制分割线，最后一个位置不需要分割线
        int position = parent.getChildAdapterPosition(view);
        //getChildCount是不断变化的。采取对每个头部添加
        if (position != 0) {
            outRect.top = mDrivider.getIntrinsicHeight();

        }
    }

    //绘制 分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();
        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 1; i < childCount; i++) {
            rect.bottom = parent.getChildAt(i).getTop();
            rect.top = rect.bottom - mDrivider.getIntrinsicHeight();
            mDrivider.setBounds(rect);
            mDrivider.draw(c);
        }
    }
}

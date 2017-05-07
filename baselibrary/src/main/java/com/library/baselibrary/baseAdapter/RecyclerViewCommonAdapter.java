package com.library.baselibrary.baseAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Created by 花歹 on 2017/4/24.
 * Email:   gatsbywang@126.com
 */

public abstract class RecyclerViewCommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private final int mLayoutId;
    private MultiTypeSupport mMultiTypeSupport;
    private LayoutInflater mInflater;
    private List<T> mData;
    private ItemClickListener mItemClickListener;

    public RecyclerViewCommonAdapter(Context context, List<T> data, int layoutId) {
        mData = data;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
    }

    public RecyclerViewCommonAdapter(Context context, List<T> data, MultiTypeSupport multiTypeSupport) {
        this(context, data, -1);
        mMultiTypeSupport = multiTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mLayoutId;
        if (mMultiTypeSupport != null) {
            layoutId = viewType;
        }
        View itemView = mInflater.inflate(layoutId, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        convert(holder, mData.get(position), position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public abstract void convert(ViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }
}

package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e16din.handyholder.holder.HandyHolder;
import com.e16din.handyholder.listeners.click.OnClickListener;
import com.e16din.handyholder.listeners.click.OnViewsClickListener;
import com.e16din.handyholder.wrapper.SimpleHandy;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleClickAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleInsertsAdapter<HOLDER, MODEL> {

    private OnClickListener<MODEL> mOnItemClickListener;
    private OnViewsClickListener<MODEL> mOnItemViewsClickListener;

    private List<Integer> mClickableViewsList;
    private int[] mClickableViewsArray;


    public SimpleClickAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleClickAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_INSERTION:
                return super.onCreateViewHolder(parent, viewType);
        }//else

        final RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (isInflatedHandyHolder(holder)) return holder;

        final SimpleHandy<MODEL> handy = new SimpleHandy<MODEL>(this, parent) {
            @Override
            public RecyclerView.ViewHolder newHolder(ViewGroup viewGroup) {
                return holder;
            }
        };

        if (mOnItemClickListener != null) {
            handy.clickListener(mOnItemClickListener);
        }
        if (mOnItemViewsClickListener != null) {
            if (mClickableViewsArray != null)
                handy.setViewsClickListener(mClickableViewsArray, mOnItemViewsClickListener);
            if (mClickableViewsList != null)
                handy.setViewsClickListener(mClickableViewsList, mOnItemViewsClickListener);
        }

        return handy.init();
    }

    protected boolean isInflatedHandyHolder(RecyclerView.ViewHolder holder) {
        if (holder instanceof HandyHolder) {
            HandyHolder h = (HandyHolder) holder;
            if (h.isInflated()) {//already inited
                return true;
            }
        }
        return false;
    }

    public void setOnItemClickListener(OnClickListener<MODEL> listener) {
        mOnItemClickListener = listener;
    }


    public void setOnItemViewsClickListener(List<Integer> clickableViews, OnViewsClickListener<MODEL> listener) {
        mClickableViewsList = clickableViews;
        mOnItemViewsClickListener = listener;
    }

    public void setOnItemViewsClickListener(int[] clickableViews, OnViewsClickListener<MODEL> listener) {
        mClickableViewsArray = clickableViews;
        mOnItemViewsClickListener = listener;
    }


    public OnClickListener<MODEL> getOnItemClickListener() {
        return mOnItemClickListener;
    }


    public OnViewsClickListener<MODEL> getOnViewsClickListener() {
        return mOnItemViewsClickListener;
    }


    public List<Integer> getClickableViewsList() {
        return mClickableViewsList;
    }
}

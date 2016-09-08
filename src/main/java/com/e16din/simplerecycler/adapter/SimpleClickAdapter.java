package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.e16din.handyholder.holder.HandyHolder;
import com.e16din.handyholder.listeners.click.OnClickListener;
import com.e16din.handyholder.listeners.click.OnViewsClickListener;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleClickAdapter<MODEL> extends SimpleListAdapter<MODEL> {

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
    public void onBindViewHolder(HandyHolder holder, int position) {
        if (holder.getClickListener() == null) {
            holder.clickListener(mOnItemClickListener);
        } else {
            Log.w(TAG, "OnClickListener already exist, check it in the newViewHolder() method.");
        }

        if (holder.getViewsClickListener() == null) {
            holder.setViewsClickListener(mClickableViewsArray, mOnItemViewsClickListener);
            holder.setViewsClickListener(mClickableViewsList, mOnItemViewsClickListener);
        } else {
            Log.w(TAG, "OnViewsClickListener already exist, check it in the newViewHolder() method.");
        }

        super.onBindViewHolder(holder, position);
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

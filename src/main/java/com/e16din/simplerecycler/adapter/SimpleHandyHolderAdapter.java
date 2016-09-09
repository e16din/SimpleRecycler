package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e16din.handyholder.listeners.click.OnClickListener;
import com.e16din.handyholder.listeners.click.OnViewsClickListener;
import com.e16din.handyholder.wrapper.Handy;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleHandyHolderAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleClickAdapter<HOLDER, MODEL> {

    public SimpleHandyHolderAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleHandyHolderAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == TYPE_INSERTION) {
            return super.onCreateViewHolder(parent, viewType);
        }// else

        final Handy<MODEL> handy = new Handy<MODEL>(this, parent) {
            @Override
            public RecyclerView.ViewHolder newHolder(ViewGroup viewGroup) {
                return newViewHolder(viewGroup, viewType);
            }
        };

        if (!handy.set().isAlreadySetAsyncInflating()) {
            handy.set().asyncInflating(isAsyncInflating());
        }

        if (!handy.set().isAlreadySetRippleEffect()) {
            handy.set().rippleEffect(isRippleEffect());
        }

        //click listeners
        if (mOnItemClickListener != null) {
            handy.set().clickListener(mOnItemClickListener);
        }

        if (mOnItemViewsClickListener != null) {
            if (mClickableViewsArray != null)
                handy.set().setViewsClickListener(mClickableViewsArray, mOnItemViewsClickListener);
            if (mClickableViewsList != null)
                handy.set().setViewsClickListener(mClickableViewsList, mOnItemViewsClickListener);
        }

        return handy.set().init();
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

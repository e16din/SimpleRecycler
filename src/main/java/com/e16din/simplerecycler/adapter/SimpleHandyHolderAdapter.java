package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.e16din.handyholder.AlreadyBox;
import com.e16din.handyholder.holder.HandyHolder;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleHandyHolderAdapter<HOLDER extends HandyHolder, MODEL>
        extends SimpleClickAdapter<HOLDER, MODEL> {

    public SimpleHandyHolderAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleHandyHolderAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public HandyHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == TYPE_INSERTION) {
            return super.onCreateViewHolder(parent, viewType);
        }// else

        final HandyHolderWrapper<MODEL> handy = new HandyHolderWrapper<MODEL>(this, parent) {
            @Override
            public HandyHolder newHolder(ViewGroup viewGroup) {
                return newViewHolder(viewGroup, viewType);
            }
        };

        if (!handy.set().isAlreadyInited()) {
            updateHandyHolderSettings(handy.set());
            return (HandyHolder) handy.set().init();
        }

        return (HandyHolder) handy.set().holder();
    }

    @Override
    public void onBindViewHolder(HandyHolder holder, int position) {
        if (holder.isInflated()) return;//wait for async inflater

        holder.bindItem(get(position), position);

        super.onBindViewHolder(holder, position);
    }

    private void updateHandyHolderSettings(AlreadyBox set) {
        if (!set.isAlreadySetAsyncInflating()) {
            set.asyncInflating(isAsyncInflating());
        }

        if (!set.isAlreadySetRippleEffect()) {
            set.rippleEffect(isRippleEffect());
        }

        //click listeners
        if (mOnItemClickListener != null) {
            set.clickListener(mOnItemClickListener);
        }

        if (mOnItemViewsClickListener != null) {
            if (mClickableViewsArray != null)
                set.setViewsClickListener(mClickableViewsArray, mOnItemViewsClickListener);
            if (mClickableViewsList != null)
                set.setViewsClickListener(mClickableViewsList, mOnItemViewsClickListener);
        }
    }
}

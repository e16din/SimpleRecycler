package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e16din.handyholder.AlreadyBox;
import com.e16din.handyholder.holder.HandyHolder;
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

        if (handy.set().holder() instanceof HandyHolder) {
            HandyHolder h = (HandyHolder) handy.set().holder();
            if (!h.set().isAlreadyInited()) {
                updateHandyHolderSettings(h.set());
            }

            return h;
        }// else

        updateHandyHolderSettings(handy.set());
        return handy.set().init();
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

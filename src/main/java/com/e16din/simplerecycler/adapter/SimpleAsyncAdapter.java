package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e16din.handyholder.holder.HandyHolder;
import com.e16din.handyholder.wrapper.SimpleHandy;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleAsyncAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleClickAdapter<HOLDER, MODEL> {

    private boolean mAsyncInflating;

    public SimpleAsyncAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleAsyncAdapter(@NonNull Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder instanceof HandyHolder) {
            HandyHolder h = (HandyHolder) holder;
            if (h.isInflated()) {//already inited
                return holder;
            }
        }

        return new SimpleHandy<MODEL>(this, parent) {
            @Override
            public RecyclerView.ViewHolder newHolder(ViewGroup viewGroup) {
                return holder;
            }
        }.asyncInflating(mAsyncInflating).init();
    }

    public boolean isAsyncInflating() {
        return mAsyncInflating;
    }

    public void setAsyncInflating(boolean asyncInflating) {
        mAsyncInflating = asyncInflating;
    }

}

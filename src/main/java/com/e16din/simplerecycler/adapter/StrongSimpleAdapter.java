package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class StrongSimpleAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimplePagingAdapter<HOLDER, MODEL> {

    //SimpleBaseAdapter - base logic
    //SimpleListAdapter - List interface
    //SimpleClickAdapter - click listeners
    //SimpleAsyncAdapter - async inflating
    //SimpleBindListenerAdapter - bind listeners
    //SimpleRippleAdapter - ripple effect
    //SimpleInsertsAdapter - headers, footers, insertions
    //SimplePagingAdapter - paging logic

    private boolean mNeedShowBottomProgress = false;


    public StrongSimpleAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public StrongSimpleAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public void showBottomProgress() {
        if (mNeedShowBottomProgress) {
            super.showBottomProgress();
        }
    }

    public void setNeedShowBottomProgress(boolean needShowBottomProgress) {
        mNeedShowBottomProgress = needShowBottomProgress;
    }

    public boolean needShowBottomProgress() {
        return mNeedShowBottomProgress;
    }

    @Override
    protected void onBindItemViewHolder(HOLDER holder, int position) {
        super.onBindItemViewHolder(holder, position);
    }
}

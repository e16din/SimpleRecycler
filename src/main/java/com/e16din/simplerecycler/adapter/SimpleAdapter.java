package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;
import com.e16din.simplerecycler.adapter.listeners.OnItemClickListener;

import java.util.List;

public abstract class SimpleAdapter<MODEL, HOLDER extends SimpleViewHolder> extends SimplePagingAdapter<MODEL, HOLDER> {
    //SimpleBaseAdapter - base logic
    //SimpleListAdapter - List interface
    //SimpleAsyncAdapter - async inflating
    //SimpleClickAdapter - click listeners
    //SimpleRippleAdapter - ripple effect
    //SimpleInsertsAdapter - headers, footers, insertions
    //SimplePagingAdapter - paging logic

    private boolean mNeedShowBottomProgress = false;


    public SimpleAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId,
                         OnItemClickListener<MODEL> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public SimpleAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId) {
        super(context, items, resId);
    }

    public SimpleAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleAdapter(@NonNull Context context) {
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
}

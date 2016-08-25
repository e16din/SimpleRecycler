package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;

import java.util.List;

public abstract class SimpleAdapter<MODEL, HOLDER extends SimpleViewHolder> extends SimplePagingAdapter<MODEL, HOLDER> {

    private boolean mNeedShowBottomProgress = false;


    public SimpleAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                         OnItemClickListener<MODEL> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public SimpleAdapter(@NonNull Context context, @NonNull List<Object> items, int resId) {
        super(context, items, resId);
    }

    public SimpleAdapter(@NonNull Context context, @NonNull List<Object> items) {
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

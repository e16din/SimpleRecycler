package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

public abstract class SimpleAdapter<MODEL> extends SimplePagingAdapter<MODEL> {

    //SimpleBaseAdapter - base logic
    //SimpleListAdapter - List interface
    //SimpleInsertsAdapter - headers, footers, insertions
    //SimplePagingAdapter - paging logic

    private boolean mNeedShowBottomProgress = false;


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

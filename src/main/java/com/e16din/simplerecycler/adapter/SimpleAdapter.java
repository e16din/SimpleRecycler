package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.e16din.handyholder.wrapper.Handy;

import java.util.List;

public abstract class SimpleAdapter<MODEL> extends SimplePagingAdapter<MODEL> {

    //SimpleBaseAdapter - base logic
    //SimpleListAdapter - List interface
    //SimpleClickAdapter - click listeners
    //SimpleAsyncAdapter - async inflating
    //SimpleBindListenerAdapter - bind listeners
    //SimpleRippleAdapter - ripple effect
    //SimpleInsertsAdapter - headers, footers, insertions
    //SimplePagingAdapter - paging logic

    public static void init(Context context) {
        Handy.init(context);
    }


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

package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.e16din.handyholder.holder.HandyHolder;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleAsyncAdapter<MODEL> extends SimpleClickAdapter<MODEL> {

    private boolean mAsyncInflating;

    public SimpleAsyncAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleAsyncAdapter(@NonNull Context context) {
        super(context);
    }


    @Override
    public HandyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType)
                .asyncInflating(mAsyncInflating);
    }

    public boolean isAsyncInflating() {
        return mAsyncInflating;
    }

    public void setAsyncInflating(boolean asyncInflating) {
        mAsyncInflating = asyncInflating;
    }

}

package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.e16din.handyholder.holder.HandyHolder;
import com.e16din.handyholder.listeners.holder.BindListener;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleBindListenerAdapter<MODEL> extends SimpleAsyncAdapter<MODEL> {

    private List<BindListener<SimpleAdapter<MODEL>, HandyHolder, MODEL>> mListeners;

    public SimpleBindListenerAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleBindListenerAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public HandyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final HandyHolder holder = super.onCreateViewHolder(parent, viewType);

        for (BindListener<SimpleAdapter<MODEL>, HandyHolder, MODEL> listener : mListeners) {
            holder.holderListener(listener);
        }

        return holder;
    }

    public void addHolderListener(BindListener<SimpleAdapter<MODEL>, HandyHolder, MODEL> listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }

        mListeners.add(listener);
    }
}

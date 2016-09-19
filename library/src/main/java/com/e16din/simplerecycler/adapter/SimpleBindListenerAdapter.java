package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleBindListenerAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleClickAdapter<HOLDER, MODEL> {

    private List<OnBindListener<MODEL>> mListeners;

    public SimpleBindListenerAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleBindListenerAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onBindItemViewHolder(HOLDER holder, int position) {
        if (mListeners == null) return;

        for (OnBindListener<MODEL> listener : mListeners) {
            listener.onBind(get(position), position);
        }
    }

    public void addOnBindListener(OnBindListener<MODEL> listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }

        mListeners.add(listener);
    }
}

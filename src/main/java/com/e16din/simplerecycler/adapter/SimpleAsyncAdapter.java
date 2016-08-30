package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.AsyncLayoutInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;

import java.util.List;


@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleAsyncAdapter<MODEL, HOLDER extends SimpleViewHolder>
        extends SimpleListAdapter<MODEL, HOLDER> {

    public static final int NO_WAITING_LAYOUT = 0;
    @LayoutRes
    private int mWaitingLayoutId = NO_WAITING_LAYOUT;

    public SimpleAsyncAdapter(@NonNull Context context, @NonNull List<MODEL> items, @LayoutRes int resId) {
        super(context, items, resId);
    }

    public SimpleAsyncAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleAsyncAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        FrameLayout vContainer = (FrameLayout) inflater.inflate(getContainerLayoutId(), parent, false);

        HOLDER holder;

        if (mWaitingLayoutId == NO_WAITING_LAYOUT) {
            View v = inflater.inflate(getItemLayoutId(), vContainer, false);
            vContainer.addView(v);
            holder = newViewHolder(vContainer);
            holder.setInflated(true);
        } else {
            final View vWaiting = inflater.inflate(mWaitingLayoutId, vContainer, false);
            vContainer.addView(vWaiting);

            holder = newViewHolder(vContainer);
            final HOLDER finalHolder = holder;
            new AsyncLayoutInflater(getContext()).inflate(getItemLayoutId(), vContainer,
                    new AsyncLayoutInflater.OnInflateFinishedListener() {
                        @Override
                        public void onInflateFinished(View view, int resId, ViewGroup vContainer) {
                            vContainer.addView(view);
                            vContainer.removeView(vWaiting);
                            finalHolder.setInflated(true);
                            finalHolder.bind();
                            onViewHolderAsyncInflated(finalHolder);
                        }
                    });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        if (holder.isInflated()) {
            super.onBindViewHolder(holder, position);
        }//else onViewHolderAsyncInflated
    }

    protected void onViewHolderAsyncInflated(HOLDER holder) {
        final int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition >= 0) {
            super.onBindViewHolder(holder, adapterPosition);
        }
    }

    public void setWaitingLayoutId(@LayoutRes int layoutId) {
        mWaitingLayoutId = layoutId;
    }
}


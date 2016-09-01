package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.AsyncLayoutInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.Utils;
import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;

import java.util.List;


@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleAsyncAdapter<MODEL> extends SimpleListAdapter<MODEL> {

    public static final int NO_STUB_LAYOUT = 0;


    @LayoutRes
    private int mStubId = NO_STUB_LAYOUT;

    private Point mEmptyStubSize = new Point(0, 64);//dp, 0 is MATCH_PARENT


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

        SimpleViewHolder holder;

        if (!needAsyncInflating()) {
            View v = inflater.inflate(getItemLayoutId(), vContainer, false);
            vContainer.addView(v);
            holder = newViewHolder(vContainer);
            holder.setInflated(true);
        } else {
            final boolean hasStubId = mStubId != NO_STUB_LAYOUT;

            final View vStub = inflater.inflate(hasStubId ? mStubId : R.layout.layout_item_empty_stub,
                    vContainer, false);

            if (!hasStubId) {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        Utils.dpToPx(getContext(), mEmptyStubSize.x),
                        Utils.dpToPx(getContext(), mEmptyStubSize.y));

                vStub.setLayoutParams(params);
            }

            vContainer.addView(vStub);

            holder = newViewHolder(vContainer);
            final SimpleViewHolder finalHolder = holder;
            new AsyncLayoutInflater(getContext()).inflate(getItemLayoutId(), vContainer,
                    new AsyncLayoutInflater.OnInflateFinishedListener() {
                        @Override
                        public void onInflateFinished(View view, int resId, ViewGroup vContainer) {
                            vContainer.addView(view);
                            vContainer.removeView(vStub);
                            finalHolder.setInflated(true);
                            finalHolder.findViews();
                            finalHolder.init();
                            onViewHolderAsyncInflated(finalHolder);
                        }
                    });
        }

        return holder;
    }

    protected abstract boolean needAsyncInflating();

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        if (holder.isInflated()) {
            super.onBindViewHolder(holder, position);
        }//else onViewHolderAsyncInflated
    }

    protected void onViewHolderAsyncInflated(SimpleViewHolder holder) {
        final int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition >= 0) {
            super.onBindViewHolder(holder, adapterPosition);
        }
    }

    public void setEmptyStubSize(int width, int height) {
        mEmptyStubSize = new Point(width, height);
    }

    public void setStubIdForAsyncInflating(@LayoutRes int layoutId) {
        mStubId = layoutId;
    }

    /**
     * @deprecated Use setStubIdForAsyncInflating method
     */
    @Deprecated
    public void setWaitingLayoutId(@LayoutRes int layoutId) {
        mStubId = layoutId;
    }

}


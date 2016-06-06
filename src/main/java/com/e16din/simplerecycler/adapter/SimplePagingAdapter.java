package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.model.Insertion;

import java.util.List;

public abstract class SimplePagingAdapter<T> extends SimpleInsertsAdapter<T> {

    @LayoutRes
    private int mBottomProgressLayoutId = R.layout.layout_bottom_progress;


    public SimplePagingAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                               OnItemClickListener<T> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public SimplePagingAdapter(@NonNull Context context, @NonNull List<Object> items, int resId) {
        super(context, items, resId);
    }

    public SimplePagingAdapter(@NonNull Context context, @NonNull List<Object> items) {
        super(context, items);
    }

    public SimplePagingAdapter(@NonNull Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder newInsertionViewHolder(View v) {
        return new PagingViewHolder(v);
    }

    @Override
    public void onLastItem() {
        showBottomProgress();
        super.onLastItem();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (getItemCount() == 0) {
            showBottomProgress();
        }
    }

    @Override
    public void add(Object item) {
        hideBottomProgress();
        super.add(item);
    }

    @Override
    public void addAll(List items) {
        hideBottomProgress();
        super.addAll(items);
    }

    @Override
    public void addFooter(@LayoutRes int layoutId) {
        hideBottomProgress();
        super.addFooter(layoutId);
    }

    @Override
    public void addFooter(@LayoutRes int layoutId, Object data) {
        hideBottomProgress();
        super.addFooter(layoutId, data);
    }

    @Override
    public void addHeader(@LayoutRes int layoutId) {
        hideBottomProgress();
        super.addHeader(layoutId);
    }

    @Override
    public void addHeader(@LayoutRes int layoutId, Object data) {
        hideBottomProgress();
        super.addHeader(layoutId, data);
    }

    public void showBottomProgress() {
        try {
            if (!hasBottomProgress()) {
                Insertion insertion =
                        new Insertion(mBottomProgressLayoutId, null, Insertion.TYPE_ABSOLUTE_FOOTER);

                getItems().add(insertion);
                notifyItemInserted(getLastPosition());
            }
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    public void hideBottomProgress() {
        if (hasBottomProgress()) removeLast();
    }

    public boolean hasBottomProgress() {
        if (getItemCount() == 0) return false;

        int lastPosition = getLastPosition();

        if (isInsertion(lastPosition)) {
            Insertion insert = getInsertion(lastPosition);
            if (insert.getType() == Insertion.TYPE_ABSOLUTE_FOOTER) {
                return true;
            }
        }

        return false;
    }

    public void setBottomProgressLayoutId(int bottomProgressLayoutId) {
        mBottomProgressLayoutId = bottomProgressLayoutId;
    }

    //for newInsertionViewHolder
    static class PagingViewHolder extends RecyclerView.ViewHolder {
        public PagingViewHolder(View view) {
            super(view);
        }
    }
}

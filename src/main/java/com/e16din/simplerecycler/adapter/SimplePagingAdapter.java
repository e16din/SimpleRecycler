package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.e16din.simplerecycler.R;

import java.util.List;

public abstract class SimplePagingAdapter<T> extends SimpleInsertsAdapter<T> {

    @LayoutRes
    private int mBottomProgressLayoutId = R.layout.layout_bottom_progress;

    private int mPageSize = 10;

    private boolean mAllItemsLoaded;

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

    /**
     * Show bottom progress bar if mAllItemsLoaded is false
     */
    @Override
    public void onLastItem() {
        if (!isAllItemsLoaded()) showBottomProgress();
        super.onLastItem();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (getItemCount() == 0) {
            showBottomProgress();
        }
    }

    /**
     * Compute mAllItemsLoaded,
     * if size < mPageSize then hide bottom progress and no more show it.
     *
     * @param size Size of items list
     */
    public void onNewPageAdded(int size) {
        if (size < mPageSize) {
            setAllItemsLoaded(true);
            hideBottomProgress();
        }
    }

    @Override
    public void add(Object item) {
        hideBottomProgress();
        super.add(item);
    }

    /**
     * Add items to list.
     * In paging adapter also call method onNewPageAdded which check size of array and compute mAllItemsLoaded,
     * if items.size() < mPageSize then hide bottom progress and no more show it.
     *
     * @param items Items
     */
    @Override
    public void addAll(List items) {
        hideBottomProgress();
        super.addAll(items);
        onNewPageAdded(items == null ? 0 : items.size());
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
            addAbsoluteFooter(mBottomProgressLayoutId);
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    public void hideBottomProgress() {
        int position = getAbsoluteFooterPosition();

        if (position >= 0) {
            remove(position);
        }
    }

    public boolean hasBottomProgress() {
        return hasAbsoluteFooter();
    }

    public void setBottomProgressLayoutId(int bottomProgressLayoutId) {
        mBottomProgressLayoutId = bottomProgressLayoutId;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    public boolean isAllItemsLoaded() {
        return mAllItemsLoaded;
    }

    public void setAllItemsLoaded(boolean allItemsLoaded) {
        mAllItemsLoaded = allItemsLoaded;
    }

    //for newInsertionViewHolder
    static class PagingViewHolder extends RecyclerView.ViewHolder {
        public PagingViewHolder(View view) {
            super(view);
        }
    }
}

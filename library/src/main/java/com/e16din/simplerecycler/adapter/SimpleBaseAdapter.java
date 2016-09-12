package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleBaseAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final String TAG = "SimpleAdapter";


    private final Context mContext;

    protected List<MODEL> mItems;

    private Runnable mOnLastItemListener;

    private RecyclerView.ViewHolder mLastHolder;

    private boolean mHasNewItems;


    public SimpleBaseAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        mContext = context;
        mItems = items;
    }

    public SimpleBaseAdapter(@NonNull Context context) {
        this(context, new ArrayList<MODEL>());
    }

    public void onLastItem() {
        setHasNewItems(false);

        if (mOnLastItemListener != null) {
            mOnLastItemListener.run();
        }
    }

    public void setHasNewItems(boolean hasNewItems) {
        mHasNewItems = hasNewItems;
    }


    public List<MODEL> getItems() {
        return mItems;
    }

    public MODEL getItem(int position) {
        return mItems.get(position);
    }

    protected int calcInsertPosition(int position) {
        return position;
    }

    public void removeLast() {
        mItems.remove(getLastItemPosition());
    }

    public void removeFirst() {
        mItems.remove(0);
    }

    protected void onBindItemViewHolder(HOLDER holder, int position) {
    }

    protected Context getContext() {
        return mContext;
    }

    //use with SimpleRecyclerView
    public void setOnLastItemListener(Runnable onLastItemListener) {
        this.mOnLastItemListener = onLastItemListener;
    }

    public Runnable getOnLastItemListener() {
        return mOnLastItemListener;
    }

    /**
     * Last position of items and insertions
     *
     * @return size of all items and insertions - 1
     */
    public int getLastItemPosition() {
        return getItemCount() == 0 ? 0 : getItemCount() - 1;
    }


    public RecyclerView.ViewHolder getLastHolder() {
        return mLastHolder;
    }

    protected void setLastHolder(RecyclerView.ViewHolder lastHolder) {
        mLastHolder = lastHolder;
    }

    /**
     * Check new items after onLastItem
     *
     * @return true if after call onLastItem we add new items to this adapter
     */
    public boolean hasNewItems() {
        return mHasNewItems;
    }


    public Resources getResources() {
        return getContext().getResources();
    }

    public String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public int getColor(int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    //- RecyclerView.Adapter

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected abstract RecyclerView.ViewHolder newViewHolder(ViewGroup parent, int viewType);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return newViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindItemViewHolder((HOLDER) holder, position);

        setLastHolder(position == getItemCount() - 1 ? holder : null);
    }
}


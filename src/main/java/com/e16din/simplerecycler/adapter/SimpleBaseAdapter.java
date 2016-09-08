package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e16din.handyholder.holder.HandyHolder;
import com.e16din.handyholder.listeners.holder.HolderListener;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleBaseAdapter<MODEL> extends RecyclerView.Adapter<HandyHolder> {

    protected static final String TAG = "SimpleAdapter";


    private final Context mContext;

    protected List<MODEL> mItems;

    private Runnable mOnLastItemListener;

    private HandyHolder mLastHolder;

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

    protected void onBindItemViewHolder(HandyHolder holder, int position) {
    }

    protected void onBindItemViewHolder(HandyHolder holder,
                                        List<HolderListener<SimpleBaseAdapter, HandyHolder, MODEL>> listeners,
                                        int position) {
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


    public HandyHolder getLastHolder() {
        return mLastHolder;
    }

    protected void setLastHolder(HandyHolder lastHolder) {
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

    protected abstract HandyHolder newViewHolder(ViewGroup parent, int viewType);

    @Override
    public HandyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return newViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(HandyHolder holder, int position) {
        if (!holder.isInflated()) return;//wait for async inflater

        onBindItemViewHolder(holder, position);
        onBindItemViewHolder(holder, holder.getListeners(), position);
        holder.bindItem(getItem(position), position);

        setLastHolder(position == getItemCount() - 1 ? holder : null);
    }
}


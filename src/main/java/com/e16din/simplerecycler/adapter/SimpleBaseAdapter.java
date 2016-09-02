package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.adapter.holders.ItemViewHolder;
import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleBaseAdapter<MODEL> extends RecyclerView.Adapter<SimpleViewHolder> {

    private final Context mContext;

    protected List<MODEL> mItems;

    private Runnable mOnLastItemListener;

    private SimpleViewHolder mLastHolder;

    private boolean mHasNewItems;

    @LayoutRes private int mItemLayoutId;

    @LayoutRes private int mContainerLayoutId = R.layout.layout_container;

    @DrawableRes private int mItemSelectorId = R.drawable.selector_list_item_default;


    public SimpleBaseAdapter(@NonNull Context context, @NonNull List<MODEL> items, @LayoutRes int resId) {
        mContext = context;
        mItems = items;
        mItemLayoutId = resId;
        onInit();
    }

    public SimpleBaseAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        this(context, items, 0);
    }

    public SimpleBaseAdapter(@NonNull Context context) {
        this(context, new ArrayList<MODEL>());
    }

    protected void onInit() {
        //override it
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

    protected abstract ItemViewHolder<MODEL> newViewHolder(View v, int viewType);

    protected abstract void onBindItemViewHolder(SimpleViewHolder holder, int position);

    protected Context getContext() {
        return mContext;
    }


    public void setItemLayoutId(@LayoutRes int layoutId) {
        mItemLayoutId = layoutId;
    }

    @LayoutRes
    public int getItemLayoutId() {
        return mItemLayoutId;
    }

    public void setContainerLayoutId(int containerLayoutId) {
        mContainerLayoutId = containerLayoutId;
    }

    @LayoutRes
    public int getContainerLayoutId() {
        return mContainerLayoutId;
    }

    @DrawableRes
    protected int getItemSelectorId() {
        return mItemSelectorId;
    }

    public void setItemSelectorId(@DrawableRes int itemSelectorId) {
        mItemSelectorId = itemSelectorId;
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


    public SimpleViewHolder getLastHolder() {
        return mLastHolder;
    }

    protected void setLastHolder(SimpleViewHolder lastHolder) {
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
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        FrameLayout vContainer = (FrameLayout) inflater.inflate(getContainerLayoutId(), parent, false);

        View v = inflater.inflate(mItemLayoutId, parent, false);

        vContainer.addView(v);

        final ItemViewHolder<MODEL> holder = newViewHolder(vContainer, viewType);
        holder.setInflated(true);

        return holder;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        updateViewHolderSelector(holder);
        holder.resetBackgrounds();
        onBindItemViewHolder(holder, position);

        ((ItemViewHolder<MODEL>) holder).bindItem(getItem(position), position);

        setLastHolder(position == getItemCount() - 1 ? holder : null);
    }

    protected void updateViewHolderSelector(@NonNull SimpleViewHolder holder) {
        holder.mSelectorResId = getItemSelectorId();
    }

    @Override
    public void onViewDetachedFromWindow(SimpleViewHolder holder) {
        holder.onDetached();
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        //todo: check clear holders
        getItems().clear();
        notifyDataSetChanged();

        super.onDetachedFromRecyclerView(recyclerView);
    }
}


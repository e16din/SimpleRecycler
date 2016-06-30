package com.e16din.simplerecycler.adapter.holders;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.adapter.SimpleViewHolder;

import java.util.ArrayList;
import java.util.List;


public abstract class SimpleRecyclerAdapter<M> extends RecyclerView.Adapter<SimpleViewHolder> {

    public static final int TYPE_DEFAULT = 0;

    private final Context mContext;

    private List<Object> mItems;

    private int mItemLayoutId;

    private OnItemClickListener<M> mOnItemClickListener;
    private Runnable mOnLastItemListener;

    private boolean mRippleEffectEnabled = true;
    private SimpleViewHolder mLastHolder;

    private boolean mHasNewItems;


    public void setItemLayoutId(@LayoutRes int layoutId) {
        mItemLayoutId = layoutId;
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                                 OnItemClickListener<M> onItemClickListener) {
        mContext = context;
        mItems = items;
        mItemLayoutId = resId;
        mOnItemClickListener = onItemClickListener;
        onInit();
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items, int resId) {
        this(context, items, resId, null);
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items) {
        this(context, items, 0, null);
    }

    public SimpleRecyclerAdapter(@NonNull Context context) {
        this(context, new ArrayList<>());
    }

    protected void onInit() {
        //for override
    }

    protected int calcInsertPosition(int position) {
        return position;
    }

    /**
     * Add an item to adapter
     *
     * @param position insert position
     * @param item     the item
     */
    public void add(int position, Object item) {
        setHasNewItems(true);

        try {
            int insertPosition = calcInsertPosition(position);
            mItems.add(insertPosition, item);
            notifyItemInserted(insertPosition);

        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    /**
     * Add an item to end of adapter
     *
     * @param item the item
     */
    public void add(Object item) {
        add(getLastItemPosition(), item);
    }

    /**
     * Remove an item by position
     *
     * @param position item position
     */
    public void remove(int position) {
        try {
            mItems.remove(position);

            notifyItemRemoved(position);
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    protected void removeLast() {
        if (getItemCount() > 0) {
            remove(getLastItemPosition());
        }
    }

    protected void removeFirst() {
        if (getItemCount() > 0) {
            remove(0);
        }
    }

    /**
     * Add items to adapter
     *
     * @param position insert position
     * @param items    the items
     */
    public void addAll(int position, List items) {
        setHasNewItems(items != null && items.size() > 0);

        try {
            int insertPosition = calcInsertPosition(position);
            mItems.addAll(insertPosition, items);
            notifyItemRangeInserted(insertPosition, items.size());

        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    /**
     * Add items to end of adapter
     *
     * @param items the items
     */
    public void addAll(List items) {
        addAll(getLastItemPosition(), items);
    }

    /**
     * Remove all items from this adapter
     */
    public void clearAll() {
        try {
            mItems.clear();
            notifyDataSetChanged();
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout vContainer = (FrameLayout)
                LayoutInflater.from(getContext()).inflate(R.layout.layout_container, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        vContainer.addView(v);
        return rippledViewHolder(vContainer);
    }

    protected abstract SimpleViewHolder newViewHolder(View v);

    protected SimpleViewHolder rippledViewHolder(View v) {
        SimpleViewHolder holder = newViewHolder(v);

        addRippleEffectToHolder((ViewGroup) v, holder);

        return holder;
    }

    protected void addRippleEffectToHolder(ViewGroup vRoot, SimpleViewHolder holder) {
        if (!mRippleEffectEnabled) return;

        holder.vFirstChild = vRoot.getChildAt(0);
        if (holder.vFirstChild != null) {
            holder.mBackgroundDrawable = holder.vFirstChild.getBackground();
        }

        //update ripple effect (or selector for old androids)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            holder.mSelectorResId = getItemSelectorId();
        } else {
            TypedValue outValue = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
            holder.mSelectorResId = outValue.resourceId;
        }

        holder.reset();
    }


    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        onBindItemViewHolder(holder, position);

        setLastHolder(position == getItemCount() - 1 ? holder : null);
    }

    protected void onBindItemViewHolder(SimpleViewHolder holder, int position) {
        updateItemClickListener(position, holder.vFirstChild != null ? holder.vFirstChild : holder.itemView);
    }


    protected void updateItemClickListener(final int position, View vItem) {
        if (mOnItemClickListener != null) {
            final M item = getItem(position);

            vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(item, position);
                }
            });
        }
    }

    @DrawableRes
    protected int getItemSelectorId() {
        return R.drawable.selector_list_item_default;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public M getItem(int position) {
        return (M) mItems.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    protected Context getContext() {
        return mContext;
    }

    public void onLastItem() {
        setHasNewItems(false);

        if (mOnLastItemListener != null) {
            mOnLastItemListener.run();
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public List getItems() {
        return mItems;
    }

    public Runnable getOnLastItemListener() {
        return mOnLastItemListener;
    }

    public OnItemClickListener<M> getOnItemClickListener() {
        return mOnItemClickListener;
    }

    //use with SimpleRecyclerView
    public void setOnLastItemListener(Runnable onLastItemListener) {
        this.mOnLastItemListener = onLastItemListener;
    }

    public boolean isRippleEffectEnabled() {
        return mRippleEffectEnabled;
    }

    public void setRippleEffectEnabled(boolean rippleEffectEnabled) {
        mRippleEffectEnabled = rippleEffectEnabled;
    }

    protected void setLastHolder(SimpleViewHolder lastHolder) {
        mLastHolder = lastHolder;
    }

    /**
     * Last position of items and insertions
     *
     * @return size of all items and insertions - 1
     */
    protected int getLastItemPosition() {
        return getItemCount() == 0 ? 0 : getItemCount() - 1;
    }

    public SimpleViewHolder getLastHolder() {
        return mLastHolder;
    }

    /**
     * Check new items after onLastItem
     *
     * @return true if after call onLastItem we add new items to this adapter
     */
    public boolean hasNewItems() {
        return mHasNewItems;
    }

    public void setHasNewItems(boolean hasNewItems) {
        mHasNewItems = hasNewItems;
    }

    public interface OnItemClickListener<M> {
        void onClick(M item, int position);
    }
}
package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.lightutils.LightUtils;
import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;
import com.e16din.simplerecycler.view.OnClickTouchListener;

import java.util.ArrayList;
import java.util.List;


public abstract class SimpleRecyclerAdapter<M> extends RecyclerView.Adapter<SimpleViewHolder> {

    public static final int TYPE_DEFAULT = 0;
    public static final int INVALID_VALUE = -1;

    private final Context mContext;

    private List<Object> mItems;

    private int mItemLayoutId;

    private OnItemClickListener<M> mOnItemClickListener;
    private OnItemViewsClickListener<M> mOnItemViewsClickListener;

    private Runnable mOnLastItemListener;

    private boolean mRippleEffectEnabled = true;
    private SimpleViewHolder mLastHolder;

    private boolean mHasNewItems;
    private List<Integer> mClickableViewsList;
    private int[] mClickableViewsArray;


    public void setItemLayoutId(@LayoutRes int layoutId) {
        mItemLayoutId = layoutId;
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                                 OnItemClickListener<M> onItemClickListener) {
        LightUtils.init(context);

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

    public void set(int position, String s) {
        mItems.set(position, s);
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
        add(getItemCount(), item);
    }

    /**
     * Remove an item by position
     *
     * @param position item position
     */
    public void remove(int position) {
        if (getItemCount() >= position) {
            try {
                mItems.remove(position);

                notifyItemRemoved(position);
            } catch (IllegalStateException e) {
                //todo: update this way
                e.printStackTrace();
            }
        }
    }

    public void removeAll(List items) {
        getItems().removeAll(items);
        notifyDataSetChanged();
    }

    protected void removeLast() {
        remove(getLastItemPosition());
    }

    protected void removeFirst() {
        remove(0);
    }

    /**
     * Add items to adapter
     *
     * @param position insert position
     * @param items    the items
     */
    public void addAll(int position, List items) {
        if (items == null || items.size() == 0) {
            return;
        }

        setHasNewItems(items.size() > 0);

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
        addAll(getItemCount(), items);
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

    /**
     * Add ripple effect to view holder
     *
     * @param vRoot  root view
     * @param holder simple view holder
     */
    protected void addRippleEffectToHolder(ViewGroup vRoot, SimpleViewHolder holder) {
        if (!mRippleEffectEnabled) return;

        holder.vContainer = vRoot.getChildAt(0);
        if (holder.vContainer != null) {
            holder.mBackgroundDrawable = holder.vContainer.getBackground();
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
        updateItemClickListener(position, holder.itemView);
    }


    protected void updateItemClickListener(final int position, View vRoot) {
        final M item = getItem(position);

        vRoot.setOnTouchListener(new OnClickTouchListener() {
            @Override
            public void onClickTouch(View view, MotionEvent e) {
                int absPosition = calcAbsolutePosition(position);

                if (mOnItemViewsClickListener != null) {
                    if (view instanceof ViewGroup) {
                        ViewGroup vContainer = (ViewGroup) view;
                        int childViewId = getClickedViewId(vContainer, e);

                        if (childViewId != INVALID_VALUE) {
                            mOnItemViewsClickListener.onClick(childViewId, item, position, absPosition);
                        } else if (mOnItemClickListener != null) {
                            mOnItemClickListener.onClick(item, position, absPosition);
                        }
                    }
                } else if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(item, position, absPosition);
                }
            }
        });
    }

    /**
     * Override it if you need different logic
     *
     * @param position item position
     * @return absolute position
     */
    protected int calcAbsolutePosition(int position) {
        return position;
    }

    private int getClickedViewId(ViewGroup view, MotionEvent e) {
        if (mClickableViewsList != null) {
            for (int viewId : mClickableViewsList) {
                if (isViewClicked(viewId, view, e)) {
                    return viewId;
                }
            }
        }

        if (mClickableViewsArray != null) {
            for (int viewId : mClickableViewsArray) {
                if (isViewClicked(viewId, view, e)) {
                    return viewId;
                }
            }
        }

        return INVALID_VALUE;
    }

    @Nullable
    private boolean isViewClicked(int viewId, ViewGroup vParent, MotionEvent e) {
        Rect rect = new Rect();
        int x = (int) e.getRawX();
        int y = (int) e.getRawY();

        vParent.findViewById(viewId).getGlobalVisibleRect(rect);

        return rect.contains(x, y);
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
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemViewsClickListener(List<Integer> clickableViews,
                                            OnItemViewsClickListener<M> onItemViewsClickListener) {
        mClickableViewsList = clickableViews;
        mOnItemViewsClickListener = onItemViewsClickListener;
    }

    public void setOnItemViewsClickListener(int[] clickableViews,
                                            OnItemViewsClickListener<M> onItemViewsClickListener) {
        mClickableViewsArray = clickableViews;
        mOnItemViewsClickListener = onItemViewsClickListener;
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


    public Resources getResources() {
        return getContext().getResources();
    }

    public String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public int getColor(int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    public View findViewById(SimpleViewHolder holder, int resId) {
        return holder.itemView.findViewById(resId);
    }


    public interface OnItemClickListener<M> {
        void onClick(M item, int itemPosition, int absolutePosition);
    }

    public interface OnItemViewsClickListener<M> {
        void onClick(@IdRes int childViewId, M item, int itemPosition, int absolutePosition);
    }
}
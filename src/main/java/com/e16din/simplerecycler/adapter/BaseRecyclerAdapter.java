package com.e16din.simplerecycler.adapter;

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

import com.e16din.simplerecycler.R;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<H extends SimpleViewHolder, M>
        extends RecyclerView.Adapter<H> {

    private final Context mContext;

    private List<Object> mItems;

    private int mItemLayoutId;

    private OnItemClickListener<M> mOnItemClickListener;
    private Runnable onLastItemListener;


    public void setItemLayoutId(@LayoutRes int layoutId) {
        mItemLayoutId = layoutId;
    }

    public BaseRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                               OnItemClickListener<M> onItemClickListener) {
        mContext = context;
        mItems = items;
        mItemLayoutId = resId;
        mOnItemClickListener = onItemClickListener;
    }

    public BaseRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items, int resId) {
        this(context, items, resId, null);
    }

    public BaseRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items) {
        this(context, items, 0, null);
    }

    public BaseRecyclerAdapter(@NonNull Context context) {
        this(context, new ArrayList<>());
    }


    protected int calcInsertPosition(int position) {
        return position;
    }

    public void add(int position, Object item) {
        try {
            int insertPosition = calcInsertPosition(position);

            mItems.add(insertPosition, item);

            notifyItemInserted(insertPosition);
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    public void add(Object item) {
        add(mItems.size() == 0 ? 0 : mItems.size() - 1, item);
    }

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
        remove(getItemCount() - 1);
    }

    protected void removeFirst() {
        remove(0);
    }

    public void addItem(int position, M item) {
        add(position, item);
    }

    public void addItem(M item) {
        add(item);
    }

    public void addAll(int position, List items) {
        try {
            int insertPosition = calcInsertPosition(position);

            mItems.addAll(insertPosition, items);

            notifyItemRangeInserted(insertPosition, items.size());
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    public void addAll(List items) {
        addAll(mItems.size() == 0 ? 0 : mItems.size() - 1, items);
    }

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
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return newViewHolder(v);
    }

    protected abstract H newViewHolder(View v);

    @Override
    public void onBindViewHolder(H holder, final int position) {
        onBindItemViewHolder(holder, position);
    }

    protected void onBindItemViewHolder(H holder, final int position) {
        if (mOnItemClickListener != null) {
            final M item = getItem(position);

            holder.vContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(item, position);
                }
            });
        }

        onBindCommon(holder.vContainer);
    }

    protected void onBindCommon(View vContainer) {
        //update ripple effect (or selector for old androids)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            vContainer.setBackgroundResource(getItemSelectorId());
        } else {
            TypedValue outValue = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
            vContainer.setBackgroundResource(outValue.resourceId);
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
        if (onLastItemListener != null) {
            onLastItemListener.run();
        }
    }

    public List getItems() {
        return mItems;
    }

    public Runnable getOnLastItemListener() {
        return onLastItemListener;
    }

    public OnItemClickListener<M> getOnItemClickListener() {
        return mOnItemClickListener;
    }

    //use with SimpleRecyclerView
    public void setOnLastItemListener(Runnable onLastItemListener) {
        this.onLastItemListener = onLastItemListener;
    }

    public interface OnItemClickListener<M> {
        void onClick(M item, int position);
    }
}
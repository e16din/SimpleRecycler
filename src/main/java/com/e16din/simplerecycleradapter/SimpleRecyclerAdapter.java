package com.e16din.simplerecycleradapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class SimpleRecyclerAdapter<H extends SimpleViewHolder, M>
        extends RecyclerView.Adapter<H> {

    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;

    private final Context context;

    private List<M> items;
    private int resIdDefault;

    private OnNoMoreItemsListener onNoMoreItemsListener;
    private OnItemClickListener<M> onItemClickListener;

    private int resIdHeader = 0;
    private int resIdFooter = 0;


    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<M> items, int resId,
                                 OnItemClickListener<M> onItemClickListener,
                                 OnNoMoreItemsListener onNoMoreItemsListener) {
        this.context = context;
        this.items = items;
        this.resIdDefault = resId;
        this.onNoMoreItemsListener = onNoMoreItemsListener;
        this.onItemClickListener = onItemClickListener;
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<M> items, int resId,
                                 OnItemClickListener<M> onItemClickListener) {
        this(context, items, resId, onItemClickListener, null);
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<M> items, int resId) {
        this(context, items, resId, null, null);
    }


    public void update(int position, M item) {
        items.set(position, item);
        notifyItemChanged(position);
    }

    public void add(M item) {
        if (hasFooter()) {
            this.items.add(items.size() - 1, item);
            notifyItemInserted(items.size() - 2);
        } else {
            this.items.add(item);
            notifyItemInserted(items.size() - 1);
        }

    }

    public void addAll(int position, List<M> items) {
        if (hasFooter()) {
            this.items.addAll(items.size() - 1, items);
            notifyItemRangeInserted(position, items.size() - 2);
        } else {
            this.items.addAll(items);
            notifyItemRangeInserted(position, items.size());
        }
    }

    public void clear() {
        items.clear();

        if (hasHeader()) {
            items.add(0, null);
        }

        if (hasFooter()) {
            items.add(null);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && hasHeader()) {
            return TYPE_HEADER;
        }

        if (position == getItemCount() - 1 && hasFooter()) {
            return TYPE_FOOTER;
        }

        return TYPE_DEFAULT;
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;

        switch (viewType) {
            case TYPE_DEFAULT:
                v = LayoutInflater.from(parent.getContext()).inflate(resIdDefault, parent, false);
                break;
            case TYPE_HEADER:
                v = LayoutInflater.from(parent.getContext()).inflate(resIdHeader, parent, false);
                break;
            case TYPE_FOOTER:
                v = LayoutInflater.from(parent.getContext()).inflate(resIdFooter, parent, false);
                break;
        }

        return newViewHolder(v);
    }

    protected abstract H newViewHolder(View v);

    @Override
    public void onBindViewHolder(H holder, final int position) {
        if (needBindHeader(position)) {
            onBindHeaderViewHolder(holder);
            return;
        }

        if (needBindFooter(position)) {
            onBindFooterViewHolder(holder);
            return;
        }

        onBindItemViewHolder(holder, position);
    }

    protected void onBindItemViewHolder(H holder, final int position) {
        if (onItemClickListener != null) {
            final M item = items.get(position);
            holder.vContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(item, position);
                }
            });

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                holder.vContainer.setBackgroundResource(getItemSelectorId());
            } else {
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
                holder.vContainer.setBackgroundResource(outValue.resourceId);
            }
        }

        if (onNoMoreItemsListener != null && position == items.size() - 1) {
            onNoMoreItemsListener.onNoMoreItems(items.size());
        }
    }

    protected void onBindHeaderViewHolder(H holder) {
        //do nothing
    }

    protected void onBindFooterViewHolder(H holder) {
        //do nothing
    }

    @DrawableRes
    protected int getItemSelectorId() {
        return R.drawable.selector_list_item_default;
    }

    protected boolean needBindHeader(int position) {
        return hasHeader() && position == 0;
    }

    protected boolean needBindFooter(int position) {
        return hasFooter() && position >= getItemCount() - 1;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    protected boolean hasHeader() {
        return resIdHeader != 0;
    }

    protected boolean hasFooter() {
        return resIdFooter != 0;
    }

    public List<M> getItems() {
        return items;
    }

    public void setOnNoMoreItemsListener(OnNoMoreItemsListener onNoMoreItemsListener) {
        this.onNoMoreItemsListener = onNoMoreItemsListener;
    }

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setHeaderResId(int resId) {
        items.add(0, null);
        this.resIdHeader = resId;
    }

    public void setFooterResId(int resId) {
        items.add(null);
        this.resIdFooter = resId;
    }

    public int getHeadersAndFootersCount() {
        return hasFooter() && hasHeader() ? 2 : (hasFooter() || hasHeader() ? 1 : 0);
    }

    protected Context getContext() {
        return context;
    }

    public interface OnItemClickListener<M> {
        void onClick(M item, int position);
    }

    public interface OnNoMoreItemsListener {
        void onNoMoreItems(int itemsCount);
    }
}
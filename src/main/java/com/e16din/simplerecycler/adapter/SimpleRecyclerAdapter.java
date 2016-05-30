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
import android.widget.FrameLayout;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.model.Insertion;

import java.util.ArrayList;
import java.util.List;


public abstract class SimpleRecyclerAdapter<H extends SimpleViewHolder, M>
        extends RecyclerView.Adapter<H> {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_INSERTION = 1;

    private final Context mContext;

    private List<Object> mItems;

    private int mItemLayoutId;

    private OnItemClickListener<M> mOnItemClickListener;
    private OnInsertionClickListener mOnInsertionClickListener;


    public void setItemLayoutId(@LayoutRes int layoutId) {
        mItemLayoutId = layoutId;
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                                 OnItemClickListener<M> onItemClickListener) {
        mContext = context;
        mItems = items;
        mItemLayoutId = resId;
        mOnItemClickListener = onItemClickListener;
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

    private int calcInsertPosition(int position) {
        int insertPosition = position;

        if (insertPosition == mItems.size() - 1) {
            insertPosition -= getFootersCount();
        } else {
            insertPosition += getHeadersCount();
        }
        return insertPosition;
    }

    public void addItem(int position, M item) {
        add(position, item);
    }

    public void addItem(M item) {
        add(item);
    }

    public void addInsertion(int position, Insertion insertion) {
        add(position, insertion);
    }

    public void addInsertion(Insertion insertion) {
        add(insertion);
    }

    public void addHeader(@LayoutRes int layoutId, Object data) {
        try {
            int insertPosition = getHeadersCount();

            mItems.add(insertPosition, new Insertion(layoutId, data, Insertion.TYPE_HEADER));
            notifyItemInserted(insertPosition);
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    public void addHeader(@LayoutRes int layoutId) {
        addHeader(layoutId, null);
    }

    public void addFooter(@LayoutRes int layoutId, Object data) {
        try {
            mItems.add(new Insertion(layoutId, data, Insertion.TYPE_FOOTER));
            notifyItemInserted(getItemCount() - 1);
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    public void addFooter(@LayoutRes int layoutId) {
        addFooter(layoutId, null);
    }

    public int getFootersCount() {
        int count = 0;
        for (int i = mItems.size() - 1; i > 0; i--) {
            if (isInsertion(i)) {
                Insertion insert = getInsertion(i);
                if (insert.getType() >= Insertion.TYPE_FOOTER) {
                    count += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return count - 1;
    }

    public int getHeadersCount() {
        int count = 0;
        for (int i = 0; i < mItems.size() - 1; i++) {
            if (isInsertion(i)) {
                Insertion insert = getInsertion(i);
                if (insert.getType() >= Insertion.TYPE_HEADER
                        && insert.getType() < Insertion.TYPE_FOOTER) {
                    count += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return count;
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
    public int getItemViewType(int position) {
        if (needInsertion(position)) {
            return TYPE_INSERTION;
        }

        return TYPE_DEFAULT;
    }

    private boolean needInsertion(int position) {
        return getInsertion(position) != null;
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;

        switch (viewType) {
            case TYPE_DEFAULT:
                v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
                break;
            case TYPE_INSERTION:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insertion,
                        parent, false);
                break;
        }

        return newViewHolder(v);
    }

    protected abstract H newViewHolder(View v);

    @Override
    public void onBindViewHolder(H holder, final int position) {
        if (needInsertion(position)) {
            onBindInsertionViewHolder(holder, position);
            return;
        }

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

    private void onBindCommon(View vContainer) {
        //update ripple effect (or selector for old androids)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            vContainer.setBackgroundResource(getItemSelectorId());
        } else {
            TypedValue outValue = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
            vContainer.setBackgroundResource(outValue.resourceId);
        }
    }

    protected void onBindInsertionViewHolder(H holder, final int position) {
        final Insertion insertion = getInsertion(position);
        View vInsertion = LayoutInflater.from(getContext()).inflate(
                insertion.getLayoutId(), null, false);
        FrameLayout vContainer = (FrameLayout) holder.vContainer;
        vContainer.removeAllViews();
        vContainer.addView(vInsertion);
        vInsertion.setClickable(true);

        if (mOnInsertionClickListener != null) {
            vInsertion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInsertionClickListener.onClick(insertion, position);
                }
            });
        }

        onBindCommon(vInsertion);
        onBindInsertion(holder, position);
    }

    protected void onBindInsertion(H holder, int position) {
        //override it for set data
    }

    @DrawableRes
    protected int getItemSelectorId() {
        return R.drawable.selector_list_item_default;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public int getOnlyItemsCount() {
        int itemsCount = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (!isInsertion(i)) {
                itemsCount += 1;
            }
        }

        return itemsCount;
    }

    //return null if it is insertion object
    public M getItem(int position) {
        return !isInsertion(position) ? (M) mItems.get(position) : null;
    }

    //return null if it is item object
    public Insertion getInsertion(int position) {
        return isInsertion(position) ? (Insertion) mItems.get(position) : null;
    }

    protected boolean isInsertion(int position) {
        return mItems.get(position) instanceof Insertion;
    }

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public OnInsertionClickListener getOnInsertionClickListener() {
        return mOnInsertionClickListener;
    }

    public void setOnInsertionClickListener(OnInsertionClickListener onInsertionClickListener) {
        mOnInsertionClickListener = onInsertionClickListener;
    }

    protected Context getContext() {
        return mContext;
    }

    public void onLastItem(){
        //override it
    }

    public interface OnItemClickListener<M> {
        void onClick(M item, int position);
    }

    public interface OnInsertionClickListener {
        void onClick(Insertion insertion, int position);
    }
}
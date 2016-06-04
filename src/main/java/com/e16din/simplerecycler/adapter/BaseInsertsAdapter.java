package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.model.Insertion;

import java.util.List;


public abstract class BaseInsertsAdapter<H extends SimpleViewHolder, M>
        extends BaseRecyclerAdapter<H, M> {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_INSERTION = 1;

    private OnInsertionClickListener mOnInsertionClickListener;

    public BaseInsertsAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                              BaseRecyclerAdapter.OnItemClickListener<M> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public BaseInsertsAdapter(@NonNull Context context, @NonNull List<Object> items, int resId) {
        super(context, items, resId);
    }

    public BaseInsertsAdapter(@NonNull Context context, @NonNull List<Object> items) {
        super(context, items);
    }

    public BaseInsertsAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int calcInsertPosition(int position) {
        int insertPosition = position;

        if (insertPosition == getItemCount() - 1) {
            insertPosition -= getFootersCount();
        } else {
            insertPosition += getHeadersCount();
        }
        return insertPosition;
    }

    public void addInsertion(int position, Insertion insertion) {
        add(position, insertion);
    }

    public void addInsertion(Insertion insertion) {
        add(insertion);
    }

    public void addHeader(@LayoutRes int layoutId, Object data) {
        int insertPosition = getHeadersCount();

        add(insertPosition, new Insertion(layoutId, data, Insertion.TYPE_HEADER));
    }

    public void addHeader(@LayoutRes int layoutId) {
        addHeader(layoutId, null);
    }

    public void addFooter(@LayoutRes int layoutId, Object data) {
        add(new Insertion(layoutId, data, Insertion.TYPE_FOOTER));
    }

    public void addFooter(@LayoutRes int layoutId) {
        addFooter(layoutId, null);
    }

    public int getFootersCount() {
        int count = 0;
        for (int i = getItemCount() - 1; i > 0; i--) {
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
        for (int i = 0; i < getItemCount() - 1; i++) {
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
                return super.onCreateViewHolder(parent, viewType);
            case TYPE_INSERTION:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insertion,
                        parent, false);
                break;
        }

        return newViewHolder(v);
    }

    @Override
    public void onBindViewHolder(H holder, final int position) {
        if (needInsertion(position)) {
            onBindInsertionViewHolder(holder, position);
            return;
        }

        super.onBindViewHolder(holder, position);
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
        return !isInsertion(position) ? super.getItem(position) : null;
    }

    //return null if it is item object
    public Insertion getInsertion(int position) {
        return isInsertion(position) ? (Insertion) getItems().get(position) : null;
    }

    protected boolean isInsertion(int position) {
        return getItems().get(position) instanceof Insertion;
    }


    public OnInsertionClickListener getOnInsertionClickListener() {
        return mOnInsertionClickListener;
    }

    public void setOnInsertionClickListener(OnInsertionClickListener onInsertionClickListener) {
        mOnInsertionClickListener = onInsertionClickListener;
    }


    public interface OnInsertionClickListener {
        void onClick(Insertion insertion, int position);
    }
}
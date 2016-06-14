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


public abstract class SimpleInsertsAdapter<M> extends SimpleRecyclerAdapter<M> {

    public static final String LOG_INSERTS = "log_inserts";

    public static final int TYPE_INSERTION = 1;

    private OnInsertionClickListener mOnInsertionClickListener;

    public SimpleInsertsAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                                SimpleRecyclerAdapter.OnItemClickListener<M> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public SimpleInsertsAdapter(@NonNull Context context, @NonNull List<Object> items, int resId) {
        super(context, items, resId);
    }

    public SimpleInsertsAdapter(@NonNull Context context, @NonNull List<Object> items) {
        super(context, items);
    }

    public SimpleInsertsAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int calcInsertPosition(int position) {
        int insertPosition = position;

        if (insertPosition == getLastPosition()) {
            insertPosition -= getFootersCount();
        } else {
            insertPosition += getHeadersCount();
        }
        return insertPosition;
    }

    public boolean hasAbsoluteFooter(int position) {
        Insertion insertion = getInsertion(position);
        return insertion != null && insertion.getType() == Insertion.TYPE_ABSOLUTE_FOOTER;
    }

    public boolean hasAbsoluteHeader(int position) {
        Insertion insertion = getInsertion(position);
        return insertion != null && insertion.getType() == Insertion.TYPE_ABSOLUTE_HEADER;
    }

    /**
     * Add custom view insertion
     */
    public void addInsertion(int position, Insertion insertion) {
        add(position, insertion);
    }

    /**
     * Add custom view insertion
     */
    public void addInsertion(Insertion insertion) {
        add(insertion);
    }

    /**
     * Add header before all items and after TYPE_ABSOLUTE_HEADER
     *
     * @param layoutId header layout
     * @param data     data to binding
     */
    public void addHeader(@LayoutRes int layoutId, Object data) {
        int insertPosition = getHeadersCount() == 0 ? 0 : getHeadersCount() - 1;

        add(insertPosition, new Insertion(layoutId, data, Insertion.TYPE_HEADER));
    }

    /**
     * Add header before all items and after TYPE_ABSOLUTE_HEADER
     *
     * @param layoutId header layout
     */
    public void addHeader(@LayoutRes int layoutId) {
        addHeader(layoutId, null);
    }

    /**
     * Add footer after all items and before TYPE_ABSOLUTE_FOOTER
     *
     * @param layoutId footer layout
     * @param data     data to binding
     */
    public void addFooter(@LayoutRes int layoutId, Object data) {
        try {
            if (hasAbsoluteFooter(getLastPosition())) {
                getItems().add(getLastPosition() - 1,
                        new Insertion(layoutId, data, Insertion.TYPE_FOOTER));
                notifyItemInserted(getLastPosition() - 1);
            } else {
                getItems().add(new Insertion(layoutId, data, Insertion.TYPE_FOOTER));
                notifyItemInserted(getLastPosition());
            }

        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    /**
     * Add footer after all items and before TYPE_ABSOLUTE_FOOTER
     *
     * @param layoutId footer layout
     */
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
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DEFAULT:
                return super.onCreateViewHolder(parent, viewType);
            case TYPE_INSERTION:
                View v = LayoutInflater.from(getContext()).inflate(R.layout.container, parent, false);
                return newInsertionViewHolder(v);
        }

        return null;
    }

    public abstract SimpleViewHolder newInsertionViewHolder(View v);

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        if (needInsertion(position)) {
            onPreBindInsertionViewHolder(holder, position);

            setLastHolder(position == getItemCount() - 1 ? holder : null);
            return;
        }

        super.onBindViewHolder(holder, position);
    }

    private void onPreBindInsertionViewHolder(SimpleViewHolder holder, final int position) {
        final Insertion insertion = getInsertion(position);

        ViewGroup vInsertion = (ViewGroup) LayoutInflater.from(getContext()).inflate(
                insertion.getLayoutId(), null, false);
        FrameLayout vContainer = (FrameLayout) holder.itemView;
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

        holder = rippledInsertionViewHolder(vContainer);

        onBindInsertionViewHolder(holder, position);
    }

    protected SimpleViewHolder rippledInsertionViewHolder(View v) {
        SimpleViewHolder holder = newInsertionViewHolder(v);

        addRippleEffectToHolder((ViewGroup) v, holder);

        return holder;
    }

    /**
     * Override it to set data on your insertions
     *
     * @param holder   view holder
     * @param position insertion position
     */
    protected void onBindInsertionViewHolder(SimpleViewHolder holder, int position) {
    }

    @DrawableRes
    protected int getItemSelectorId() {
        return R.drawable.selector_list_item_default;
    }

    public boolean isEmpty() {
        return getOnlyItemsCount() == 0;
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

    protected boolean hasAbsoluteFooter() {
        return getAbsoluteFooterPosition() >= 0;
    }

    protected int getAbsoluteFooterPosition() {
        int result = -1;
        if (getItemCount() == 0) return result;

        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (isInsertion(i) && getInsertion(i).getType() == Insertion.TYPE_ABSOLUTE_FOOTER) {
                result = i;
                break;
            }
        }

        return result;
    }

    /**
     * Add only one absolute footer. All another inserts before it.
     */
    protected void addAbsoluteFooter(@LayoutRes int layoutId) {
        if (!hasAbsoluteFooter()) {
            Insertion insertion =
                    new Insertion(layoutId, null, Insertion.TYPE_ABSOLUTE_FOOTER);

            getItems().add(insertion);
            notifyItemInserted(getLastPosition());
        } //else: The absolute footer once inserted, remove it if you need, and try again
    }

    /**
     * Get item by position
     *
     * @param position position of item
     * @return item or null if it is insertion object
     */
    public M getItem(int position) {
        return !isInsertion(position) ? super.getItem(position) : null;
    }

    /**
     * Get insertion by position
     *
     * @param position
     * @return insertion or null if it is item object
     */
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
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
import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;
import com.e16din.simplerecycler.model.Insertion;

import java.util.ArrayList;
import java.util.List;


public abstract class SimpleInsertsAdapter<M> extends SimpleRecyclerAdapter<M> {

    public static final String LOG_INSERTS = "log_inserts";

    public static final int TYPE_INSERTION = 1;

    private OnInsertionClickListener mOnInsertionClickListener;

    private int mOnlyItemsCount;//count of items exclude all insertions
    private int mOnlyInsertsCount;//count of inserts exclude headers and footers
    private int mHeadersCount;
    private int mFootersCount;

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
    public void remove(int position) {
        if (isHeader(position)) {
            mHeadersCount -= 1;
        } else if (isFooter(position)) {
            mFootersCount -= 1;
        } else if (isInsertion(position)) {
            mOnlyInsertsCount -= 1;
        } else {
            mOnlyItemsCount -= 1;
        }

        super.remove(position);
    }

    @Override
    public void clearAll() {
        mOnlyItemsCount = 0;
        mOnlyInsertsCount = 0;
        mHeadersCount = 0;
        mFootersCount = 0;

        super.clearAll();
    }

    public void clearAllBetweenHeadersAndFooters() {
        List headersAndFooters = new ArrayList();

        headersAndFooters.addAll(getHeaders());
        headersAndFooters.addAll(getFooters());

        try {
            getItems().clear();
            mOnlyInsertsCount = 0;
            mOnlyItemsCount = 0;
            notifyDataSetChanged();
            reAddAll(headersAndFooters);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void clearOnlyItems() {
        List insertions = new ArrayList();

        insertions.addAll(getHeaders());
        insertions.addAll(getOnlyInsertions());
        insertions.addAll(getFooters());

        try {
            getItems().clear();
            mOnlyItemsCount = 0;
            notifyDataSetChanged();
            reAddAll(insertions);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void clearHeaders() {
        int startHeadersCount = getHeadersCount();
        if (startHeadersCount == 0) {
            return;
        }

        try {
            List items = getItems();

            while (isHeader(0)) {
                items.remove(0);
            }

            mHeadersCount = 0;
            notifyItemRangeRemoved(0, startHeadersCount);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void clearFooters() {
        int startItemCount = getItemCount();
        int startFootersCount = getFootersCount();

        if (startFootersCount == 0) {
            return;
        }

        try {
            List items = getItems();

            while (isHeader(getItemCount() - 1)) {
                items.remove(getItemCount() - 1);
            }

            mFootersCount = 0;

            notifyItemRangeRemoved(startItemCount - startFootersCount, startFootersCount);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear all insertions with headers and footers
     */
    public void clearAllInsertions() {
        List items = new ArrayList();

        items.addAll(getOnlyItems());

        try {
            getItems().clear();
            mOnlyInsertsCount = 0;
            mHeadersCount = 0;
            mFootersCount = 0;
            notifyDataSetChanged();
            reAddAll(items);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public List getOnlyItems() {
        ArrayList result = new ArrayList();

        for (int i = getHeadersCount(); i < getItemCount() - getFootersCount(); i++) {
            if (!isInsertion(i)) {
                result.add(getItems().get(i));
            }
        }

        return result;
    }

    /**
     * Get only insertions
     *
     * @return Insertions between headers and footers
     */
    @NonNull
    public List getOnlyInsertions() {
        ArrayList result = new ArrayList();

        for (int i = getHeadersCount(); i < getItemCount() - getFootersCount(); i++) {
            if (isInsertion(i)) {
                result.add(getItems().get(i));
            }
        }

        return result;
    }

    @NonNull
    public List getHeaders() {
        if (getHeadersCount() > 0) {
            return getItems().subList(0, getHeadersCount());
        }

        return new ArrayList();
    }

    @NonNull
    public List getFooters() {
        if (getFootersCount() > 0) {
            int itemCount = getItemCount();
            return getItems().subList(itemCount - getFootersCount(), itemCount);
        }

        return new ArrayList();
    }

    protected int getAbsoluteFootersCount() {
        int result = 0;
        if (getItemCount() == 0) return result;

        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (isInsertion(i) && getInsertion(i).getType() >= Insertion.TYPE_ABSOLUTE_FOOTER) {
                result += 1;
            }
        }

        return result;
    }

    public boolean hasAbsoluteHeader(int position) {
        Insertion insertion = getInsertion(position);
        return insertion != null && insertion.getType() == Insertion.TYPE_ABSOLUTE_HEADER;
    }

    @Override
    protected int calcInsertPosition(int insertPosition) {
        if (getItemCount() == 0) return 0;

        if (isHeader(insertPosition)) {
            for (int i = 0; i <= insertPosition; i++) {
                if (isHeader(i)) {
                    insertPosition += 1;
                } else {
                    break;
                }
            }
        } else if (isFooter(insertPosition)) {
            for (int i = insertPosition; i > 0; i--) {
                if (isFooter(i)) {
                    insertPosition -= 1;
                } else {
                    break;
                }
            }
        }

        return insertPosition;
    }

    @Override
    protected int getLastItemPosition() {
        final int onlyItemsCount = getOnlyItemsCount();
        return onlyItemsCount == 0 ? 0 : onlyItemsCount - 1;
    }

    public boolean isFooter(int i) {
        return isInsertion(i) && getInsertion(i).getType() >= Insertion.TYPE_FOOTER;
    }

    public boolean isHeader(int i) {
        return isInsertion(i)
                && getInsertion(i).getType() >= Insertion.TYPE_HEADER
                && getInsertion(i).getType() <= Insertion.TYPE_ABSOLUTE_HEADER;
    }

    /**
     * Add custom view insertion to adapter
     */
    public void addInsertion(int position, Insertion insertion) {
        add(position, insertion);
    }

    /**
     * Add custom view insertion to adapter
     */
    public void addInsertion(Insertion insertion) {
        add(insertion);
    }

    protected void updateCounter(Object item) {
        if (item instanceof Insertion) {
            int type = ((Insertion) item).getType();

            if (type >= Insertion.TYPE_HEADER && type <= Insertion.TYPE_ABSOLUTE_HEADER) {
                mHeadersCount += 1;
            } else if (type >= Insertion.TYPE_ABSOLUTE_FOOTER) {
                mFootersCount += 1;
            } else {
                mOnlyInsertsCount += 1;
            }
        } else {
            mOnlyItemsCount += 1;
        }
    }

    @Override
    public void add(int position, Object item) {
        setHasNewItems(true);

        try {
            int insertPosition = calcInsertPosition(position);
            getItems().add(insertPosition, item);

            updateCounter(item);

            notifyItemInserted(insertPosition);

        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    @Override
    public void add(Object item) {
        setHasNewItems(true);

        try {
            int insertPosition = getItemCount() - getFootersCount();

            if (getFootersCount() == 0) {
                getItems().add(item);
            } else {
                getItems().add(insertPosition, item);
            }

            updateCounter(item);

            notifyItemInserted(insertPosition);

        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }


    public void reAddAll(List items) {
        if (items == null || items.size() == 0) {
            return;
        }

        try {
            getItems().addAll(items);
            //no update counters, because we save their old values
            notifyItemRangeInserted(0, items.size());
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    @Override
    public void addAll(List items) {
        if (items == null || items.size() == 0) {
            return;
        }

        int size = items.size();

        setHasNewItems(size > 0);

        try {
            int insertPosition = getItemCount() - getFootersCount();

            if (getFootersCount() == 0) {
                getItems().addAll(items);
            } else {
                getItems().addAll(insertPosition, items);
            }

            for (int i = 0; i < items.size(); i++) {
                Object item = items.get(i);
                updateCounter(item);
            }

            notifyItemRangeInserted(insertPosition, size);

        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
    }

    /**
     * Add header before all items and after TYPE_ABSOLUTE_HEADER
     *
     * @param layoutId header layout
     * @param data     data to binding
     */
    public void addHeader(@LayoutRes int layoutId, Object data) {
        try {
            int insertPosition = calcInsertPosition(0);
            getItems().add(insertPosition, new Insertion(layoutId, data, Insertion.TYPE_HEADER));
            mHeadersCount += 1;
            notifyItemInserted(insertPosition);

        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
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
            int absoluteFootersCount = getAbsoluteFootersCount();
            if (absoluteFootersCount > 0) {
                int position = getItemCount() - absoluteFootersCount;
                getItems().add(position,
                        new Insertion(layoutId, data, Insertion.TYPE_FOOTER));
                mFootersCount += 1;
                notifyItemInserted(position);
            } else {
                getItems().add(new Insertion(layoutId, data, Insertion.TYPE_FOOTER));
                mFootersCount += 1;
                notifyItemInserted(getLastItemPosition());
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
        return mFootersCount;
    }

    public int getHeadersCount() {
        return mHeadersCount;
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
                View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_container, parent, false);
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

    /**
     * Get only items count
     *
     * @return count of items exclude all insertions
     */
    public int getOnlyItemsCount() {
        return mOnlyItemsCount;
    }

    /**
     * Get only inserts count
     *
     * @return count of inserts exclude headers and footers
     */
    public int getOnlyInsertsCount() {
        return mOnlyInsertsCount;
    }


    /**
     * Add absolute footer. All another inserts before it.
     */
    protected void addAbsoluteFooter(@LayoutRes int layoutId) {
        addAbsoluteFooter(layoutId, Insertion.TYPE_ABSOLUTE_FOOTER);
    }

    /**
     * Add absolute footer. All another inserts before it.
     */
    protected void addAbsoluteFooter(@LayoutRes int layoutId, int type) {
        try {
            Insertion insertion = new Insertion(layoutId, null, type);

            getItems().add(insertion);
            mFootersCount += 1;
            notifyItemInserted(getItemCount() - 1);
        } catch (IllegalStateException e) {
            //todo: update this way
            e.printStackTrace();
        }
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
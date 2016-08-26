package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;
import com.e16din.simplerecycler.model.Insertion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class SimpleInsertsAdapter<MODEL, HOLDER extends SimpleViewHolder>
        extends SimpleRecyclerAdapter<MODEL, HOLDER> {

    public static final int TYPE_INSERTION = 1;


    private OnInsertionClickListener mOnInsertionClickListener;

    private int mOnlyItemsCount;//count of items exclude all insertions

    private SparseArray<Insertion> mHeaders = new SparseArray<>();
    private SparseArray<Insertion> mInsertions = new SparseArray<>();
    private SparseArray<Insertion> mFooters = new SparseArray<>();

    public SimpleInsertsAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId,
                                SimpleRecyclerAdapter.OnItemClickListener<MODEL> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public SimpleInsertsAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId) {
        super(context, items, resId);
    }

    public SimpleInsertsAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleInsertsAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public MODEL remove(int position) {
        if (isHeader(position)) {
            mHeaders.remove(position);
        } else if (isFooter(position)) {
            mFooters.remove(position);
        } else if (isInsertion(position)) {
            mInsertions.remove(position);
        } else {
            mOnlyItemsCount -= 1;
        }

        return super.remove(position);
    }

    @Override
    public void clear() {
        mOnlyItemsCount = 0;

        mHeaders.clear();
        mInsertions.clear();
        mFooters.clear();

        super.clear();
    }

    //todo: check this
    public void clearAllBetweenHeadersAndFooters() {
        getItems().clear();
        mInsertions.clear();
        mOnlyItemsCount = 0;

        final List<MODEL> emptyItems = new ArrayList<>();

        forEach(mHeaders, new ForEach<Insertion>() {
            @Override
            public void onItem(Insertion item) {
                emptyItems.add(null);
            }
        });

        forEach(mFooters, new ForEach<Insertion>() {
            @Override
            public void onItem(Insertion item) {
                emptyItems.add(null);
            }
        });

        getItems().addAll(emptyItems);
        //no update counters, because we save their old values
        notifyItemRangeInserted(0, emptyItems.size());
    }

    private <T> void forEach(SparseArray<T> array, ForEach<T> forEach) {
        for (int i = 0; i < array.size(); i++) {
            forEach.onItem(array.get(i));
        }
    }

    interface ForEach<T> {
        void onItem(T item);
    }

    //todo: update and check this
    public void clearOnlyItems() {
        List<Insertion> insertions = new ArrayList<>();

        insertions.addAll(getHeaders());
        insertions.addAll(getOnlyInsertions());
        insertions.addAll(getFooters());

        getItems().clear();
        mOnlyItemsCount = 0;
        notifyDataSetChanged();
        reAddAll(insertions);
    }

    //todo: update and check this
    public void clearHeaders() {
        int startHeadersCount = getHeadersCount();
        if (startHeadersCount == 0) {
            return;
        }

        List items = getItems();

        while (isHeader(0)) {
            items.remove(0);
        }

        mHeadersCount = 0;
        notifyDataSetChanged();
    }

    //todo: update and check this
    public void clearFooters() {
        int startItemCount = getItemCount();
        int startFootersCount = getFootersCount();

        if (startFootersCount == 0) {
            return;
        }

        List items = getItems();

        while (isHeader(getItemCount() - 1)) {
            items.remove(getItemCount() - 1);
        }

        mFootersCount = 0;

        notifyItemRangeRemoved(startItemCount - startFootersCount, startFootersCount);
    }

    /**
     * Clear all insertions with headers and footers
     */
    //todo: update and check this
    public void clearAllInsertions() {
        List<Object> items = new ArrayList<>();

        items.addAll(getOnlyItems());

        getItems().clear();
        mOnlyInsertsCount = 0;
        mHeadersCount = 0;
        mFootersCount = 0;
        notifyDataSetChanged();
        reAddAll(items);
    }


    //todo: update and check this
    @NonNull
    public List<MODEL> getOnlyItems() {
        ArrayList<MODEL> result = new ArrayList<>();

        for (int i = getHeadersCount(); i < getItemCount() - getFootersCount(); i++) {
            result.add(get(i));
        }

        return result;
    }

    /**
     * Get only insertions
     *
     * @return Insertions between headers and footers
     */
    //todo: update and check this
    @NonNull
    public List<Insertion> getOnlyInsertions() {
        ArrayList<Insertion> result = new ArrayList<>();

        for (int i = getHeadersCount(); i < getItemCount() - getFootersCount(); i++) {
            if (isInsertion(i)) {
                result.add((Insertion) getItems().get(i));
            }
        }

        return result;
    }

    //todo: check this
    @NonNull
    public final List<Insertion> getHeaders() {
        List<Insertion> result = new ArrayList<>();

        if (getHeadersCount() > 0) {
            for (int i = 0; i < mHeaders.size(); i++) {
                result.add(mHeaders.get(i));
            }

            return result;
        }

        return result;
    }

    //todo: check this
    @NonNull
    public List<Insertion> getFooters() {
        List<Insertion> result = new ArrayList<>();

        if (getFootersCount() > 0) {
            for (int i = 0; i < mFooters.size(); i++) {
                result.add(mFooters.get(i));
            }

            return result;
        }

        return result;
    }

    //todo: update and check this
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

    //todo: update and check this
    public boolean hasAbsoluteHeader(int position) {
        Insertion insertion = getInsertion(position);
        return insertion != null && insertion.getType() == Insertion.TYPE_ABSOLUTE_HEADER;
    }

    //todo: update and check this
    @Override
    protected int calcInsertPosition(int insertPosition) {
        if (getItemCount() == 0) return 0;

        if (isHeader(insertPosition)) {
            for (int i = 0; i <= insertPosition; i++) {
                if (i < getItemCount() && isHeader(i)) {
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

    //todo: update and check this
    @Override
    public int getLastItemPosition() {
        final int onlyItemsCount = getOnlyItemsCount();
        return onlyItemsCount == 0 ? 0 : onlyItemsCount - 1;
    }

    //todo: update and check this
    public boolean isFooter(int i) {
        return isInsertion(i) && getInsertion(i).getType() >= Insertion.TYPE_FOOTER;
    }

    //todo: update and check this
    public boolean isHeader(int i) {
        return isInsertion(i)
                && getInsertion(i).getType() >= Insertion.TYPE_HEADER
                && getInsertion(i).getType() <= Insertion.TYPE_ABSOLUTE_HEADER;
    }

    //todo: check this
    /**
     * Add custom view insertion to adapter
     */
    public void addInsertion(int position, Insertion insertion) {
        setHasNewItems(true);

        int insertPosition = calcInsertPosition(position);

        if (insertPosition == getItemCount()) {
            getItems().add(null);
            mInsertions.put(size() - 1, insertion);
        } else {
            getItems().add(insertPosition, null);
            mInsertions.put(insertPosition, insertion);
        }

        updateCounter(insertion);

        notifyDataSetChanged();
    }

    //todo: check this
    /**
     * Add custom view insertion to adapter
     */
    public boolean addInsertion(Insertion insertion) {
        setHasNewItems(true);

        int insertPosition = getItemCount() - getFootersCount();

        boolean result;

        if (getFootersCount() == 0) {
            result = getItems().add(null);
            mInsertions.put(size() - 1, insertion);
        } else {
            getItems().add(insertPosition, null);
            mInsertions.put(insertPosition, insertion);
            result = true;
        }

        updateCounter(insertion);

        notifyDataSetChanged();

        return result;
    }

    //todo: update and check this
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
    public void add(int position, MODEL item) {
        setHasNewItems(true);

        int insertPosition = calcInsertPosition(position);

        if (insertPosition == getItemCount()) {
            getItems().add(item);
        } else {
            getItems().add(insertPosition, item);
        }

        updateCounter(item);

        notifyDataSetChanged();
    }

    @Override
    public boolean add(MODEL item) {
        setHasNewItems(true);

        int insertPosition = getItemCount() - getFootersCount();

        boolean result;

        if (getFootersCount() == 0) {
            result = getItems().add(item);
        } else {
            getItems().add(insertPosition, item);
            result = true;
        }

        updateCounter(item);

        notifyDataSetChanged();

        return result;
    }

    public void fireAdd(int position, MODEL item) {
        super.add(position, item);
    }

    public void fireAdd(MODEL item) {
        super.add(item);
    }


    @Override
    public boolean addAll(@NonNull Collection<? extends MODEL> collection) {
        if (collection.size() == 0) {
            return false;
        }

        setHasNewItems(collection.size() > 0);

        int insertPosition = getItemCount() - getFootersCount();

        boolean result;

        if (getFootersCount() == 0) {
            result = getItems().addAll(collection);
        } else {
            result = getItems().addAll(insertPosition, collection);
        }

        for (MODEL item : collection) {
            updateCounter(item);
        }

        notifyDataSetChanged();

        return result;
    }

    @Override
    public boolean addAll(int location, @NonNull Collection<? extends MODEL> collection) {
        if (collection.size() == 0) {
            return false;
        }

        int size = collection.size();

        setHasNewItems(size > 0);

        int insertPosition = calcInsertPosition(location);

        boolean result;

        if (insertPosition == getItemCount()) {
            result = getItems().addAll(collection);
        } else {
            result = getItems().addAll(insertPosition, collection);
        }

        for (MODEL item : collection) {
            updateCounter(item);
        }

        notifyDataSetChanged();

        return result;
    }

    public void fireAddAll(int position, List<MODEL> items) {
        super.addAll(position, items);
    }

    public void fireAddAll(List<MODEL> items) {
        super.addAll(items);
    }

    /**
     * Add header before all items and after TYPE_ABSOLUTE_HEADER
     *
     * @param layoutId header layout
     * @param data     data to binding
     */
    public void addHeader(@LayoutRes int layoutId, Object data) {
        int insertPosition = calcInsertPosition(0);
        getItems().add(insertPosition, null);
        mHeaders.put(insertPosition, new Insertion(layoutId, data, Insertion.TYPE_HEADER));
        mHeadersCount += 1;
        notifyDataSetChanged();
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
        final int absoluteFootersCount = getAbsoluteFootersCount();
        final Insertion insertion = new Insertion(layoutId, data, Insertion.TYPE_FOOTER);

        if (absoluteFootersCount > 0) {
            int position = getItemCount() - absoluteFootersCount;
            getItems().add(position, null);
            mFooters.put(position, insertion);
            mFootersCount += 1;
            notifyDataSetChanged();
        } else {
            getItems().add(null);
            mFooters.put(getItems().size() - 1, insertion);
            mFootersCount += 1;
            notifyItemInserted(getLastItemPosition());
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

    /**
     * Override this method if you need to bind view holder for insertions
     */
    public SimpleViewHolder newInsertionViewHolder(View v) {
        return new SimpleViewHolder(v);//empty view holder
    }

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

        ViewGroup vInsertion = (ViewGroup)
                LayoutInflater.from(getContext()).inflate(insertion.getLayoutId(), null, false);

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

        switch (insertion.getType()) {
            case Insertion.TYPE_HEADER:
                onBindHeaderViewHolder(holder, position);
                break;
            case Insertion.TYPE_FOOTER:
                onBindFooterViewHolder(holder, position);
                break;
        }

    }

    @Nullable
    protected SimpleViewHolder rippledInsertionViewHolder(View v) {
        SimpleViewHolder holder = newInsertionViewHolder(v);

        addRippleEffectToHolder((ViewGroup) v, holder);

        return holder;
    }

    /**
     * Override this method to update your insertions
     *
     * @param holder   view holder
     * @param position insertion position
     */
    protected void onBindInsertionViewHolder(SimpleViewHolder holder, int position) {
    }

    /**
     * Override this method to update your footers
     *
     * @param holder   view holder
     * @param position footer position
     */
    protected void onBindFooterViewHolder(SimpleViewHolder holder, int position) {

    }

    /**
     * Override this method to update your headers
     *
     * @param holder   view holder
     * @param position header position
     */
    protected void onBindHeaderViewHolder(SimpleViewHolder holder, int position) {
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
        getItems().add(null);

        final int position = size() - 1;

        mFooters.put(position, new Insertion(layoutId, null, type));

        mFootersCount += 1;

        notifyItemInserted(position);
    }

    /**
     * Returns the element at the specified location in this List.
     * <p/>
     *
     * @param location the index of the element to return.
     *                 <p/>
     * @return the element at the specified location or null if it is insertion object.
     * <p/>
     * @throws IndexOutOfBoundsException if location < 0 || location >= size()
     */
    @Nullable
    @Override
    public MODEL get(int location) {
        return !isInsertion(location) ? super.get(location) : null;
    }

    /**
     * Get insertion by position
     *
     * @param position position of insertion
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
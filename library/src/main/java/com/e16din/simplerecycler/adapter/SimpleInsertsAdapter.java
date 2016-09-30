package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.handyholder.HandyHolder;
import com.e16din.handyholder.listeners.click.OnClickListener;
import com.e16din.handyholder.listeners.click.OnViewsClickListener;
import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.model.Insertion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleInsertsAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleListAdapter<HOLDER, MODEL> {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_INSERTION = 100500;

    private List<Insertion> mInserts;

    private OnClickListener<Insertion> mOnInsertClickListener;
    private OnViewsClickListener<Insertion> mOnInsertViewsClickListener;

    private List<Integer> mClickableInsertViewsList;
    private int[] mClickableInsertViewsArray;

    private int mOnlyItemsCount;//count of items exclude all insertions
    private int mOnlyInsertsCount;//count of inserts exclude headers and footers
    private int mHeadersCount;
    private int mFootersCount;


    public SimpleInsertsAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
        mInserts = createEmptyInsertsList(items.size());
    }

    public SimpleInsertsAdapter(@NonNull Context context) {
        super(context);
        mInserts = new ArrayList<>();
    }

    @Override
    public MODEL remove(int position) {
        if (getItemCount() > position) {
            updateCountersAfterRemove(position);

            mInserts.remove(position);
        }
        return super.remove(position);
    }

    private void updateCountersAfterRemove(int position) {
        if (isHeader(position)) {
            mHeadersCount -= 1;
        } else if (isFooter(position)) {
            mFootersCount -= 1;
        } else if (isInsertion(position)) {
            mOnlyInsertsCount -= 1;
        } else {
            mOnlyItemsCount -= 1;
        }
    }

    @Override
    public void clear() {
        clearInserts();
        super.clear();
    }

    private void clearInserts() {
        mOnlyItemsCount = 0;
        mOnlyInsertsCount = 0;
        mHeadersCount = 0;
        mFootersCount = 0;

        mInserts.clear();
    }

    //todo: check it with paging adapter

    /**
     * Clear items and inserts between headers and footers
     */
    public void clearAllBetweenHeadersAndFooters() {
        List<Insertion> headersAndFooters = new ArrayList<>();

        headersAndFooters.addAll(getHeaders());
        headersAndFooters.addAll(getFooters());

        mInserts.clear();
        getItems().clear();
        mOnlyInsertsCount = 0;
        mOnlyItemsCount = 0;

        reAddAllInserts(headersAndFooters);

        notifyIfNeed();
    }

    /**
     * Clear only items (exclude all insertions)
     */
    public void clearOnlyItems() {
        List<Insertion> insertions = new ArrayList<>();

        insertions.addAll(getHeaders());
        insertions.addAll(getOnlyInsertions());
        insertions.addAll(getFooters());

        mInserts.clear();
        getItems().clear();
        mOnlyItemsCount = 0;

        reAddAllInserts(insertions);

        notifyIfNeed();
    }

    public void clearHeaders() {
        int startHeadersCount = getHeadersCount();
        if (startHeadersCount == 0) {
            return;
        }

        List items = getItems();

        while (isHeader(0)) {
            mInserts.remove(0);
            items.remove(0);
        }

        mHeadersCount = 0;

        notifyIfNeed();
    }

    public void clearFooters() {
        int startItemCount = getItemCount();
        int startFootersCount = getFootersCount();

        if (startFootersCount == 0 || getItemCount() == 0) {
            return;
        }

        List items = getItems();

        while (isHeader(getItemCount() - 1)) {
            final int position = getItemCount() - 1;
            mInserts.remove(position);
            items.remove(position);
        }

        mFootersCount = 0;

        notifyIfNeed();
    }

    /**
     * Clear all insertions with headers and footers
     */
    public void clearAllInsertions() {
        List<MODEL> items = new ArrayList<>();

        items.addAll(getOnlyItems());

        mInserts.clear();
        getItems().clear();
        mOnlyInsertsCount = 0;
        mHeadersCount = 0;
        mFootersCount = 0;

        reAddAllItems(items);

        notifyIfNeed();
    }

    /**
     * @return items exclude all insertions
     */
    @NonNull
    public List<MODEL> getOnlyItems() {
        ArrayList<MODEL> result = new ArrayList<>();

        for (int i = getHeadersCount(); i < getItemCount() - getFootersCount(); i++) {
            if (isItem(i)) {
                result.add(get(i));
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
    public List<Insertion> getOnlyInsertions() {
        ArrayList<Insertion> result = new ArrayList<>();

        for (int i = getHeadersCount(); i < getItemCount() - getFootersCount(); i++) {
            if (isInsertion(i)) {
                result.add(mInserts.get(i));
            }
        }

        return result;
    }

    @NonNull
    public List<Insertion> getHeaders() {
        if (getHeadersCount() > 0) {
            return mInserts.subList(0, getHeadersCount());
        }

        return new ArrayList<>();
    }

    @NonNull
    public List<Insertion> getFooters() {
        if (getFootersCount() > 0) {
            int itemCount = getItemCount();
            return mInserts.subList(itemCount - getFootersCount(), itemCount);
        }

        return new ArrayList<>();
    }

    protected int getAbsoluteFootersCount() {
        int result = 0;
        if (getItemCount() == 0) return result;

        for (int i = getItemCount() - 1; i >= 0; i--) {
            final Insertion insertion = getInsertion(i);
            if (insertion != null && insertion.getType() >= Insertion.TYPE_ABSOLUTE_FOOTER) {
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


    public List<Insertion> getInserts() {
        return mInserts;
    }

    /**
     * @return Get last of items position (without insertions and footers)
     */
    @Override
    public int getLastItemPosition() {
        final int onlyItemsCount = getOnlyItemsCount();
        return onlyItemsCount == 0 ? 0 : onlyItemsCount - 1;
    }

    public boolean isFooter(int i) {
        final Insertion insertion = getInsertion(i);
        return insertion != null && insertion.getType() >= Insertion.TYPE_FOOTER;
    }

    public boolean isHeader(int i) {
        final Insertion insertion = getInsertion(i);
        return insertion != null
                && insertion.getType() >= Insertion.TYPE_HEADER
                && insertion.getType() <= Insertion.TYPE_ABSOLUTE_HEADER;
    }

    /**
     * Add custom view insertion to adapter
     */
    public void addInsertion(int position, Insertion insert) {
        setHasNewItems(true);

        int insertPosition = calcInsertPosition(position);

        if (insertPosition == getItemCount()) {
            mInserts.add(insert);
            getItems().add(null);
        } else {
            mInserts.add(insertPosition, insert);
            getItems().add(insertPosition, null);
        }

        updateCounter(insert);

        notifyIfNeed();
    }

    /**
     * Add custom view insertion to adapter
     */
    public boolean addInsertion(Insertion insert) {
        setHasNewItems(true);

        int insertPosition = getItemCount() <= getFootersCount() ? 0 : getItemCount() - getFootersCount();

        boolean result;

        if (getFootersCount() == 0) {
            mInserts.add(insert);
            result = getItems().add(null);
        } else {
            mInserts.add(insertPosition, insert);
            getItems().add(insertPosition, null);
            result = true;
        }

        updateCounter(insert);

        notifyIfNeed();
        return result;
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
    public void add(int position, MODEL item) {
        setHasNewItems(true);

        int insertPosition = calcInsertPosition(position);

        if (insertPosition == getItemCount()) {
            mInserts.add(null);
            getItems().add(item);
        } else {
            mInserts.add(insertPosition, null);
            getItems().add(insertPosition, item);
        }

        updateCounter(item);

        notifyIfNeed();
    }

    @Override
    public boolean add(MODEL item) {
        setHasNewItems(true);

        int insertPosition = getItemCount() <= getFootersCount() ? 0 : getItemCount() - getFootersCount();

        boolean result;

        if (getFootersCount() == 0) {
            mInserts.add(null);
            result = getItems().add(item);
        } else {
            mInserts.add(insertPosition, null);
            getItems().add(insertPosition, item);
            result = true;
        }

        updateCounter(item);

        notifyIfNeed();
        return result;
    }

    /**
     * Add all items without update counters
     */
    public void reAddAllItems(@NonNull Collection<? extends MODEL> collection) {
        if (collection.size() == 0) {
            return;
        }

        mInserts.addAll(createEmptyInsertsList(collection.size()));
        getItems().addAll(collection);
        //no update counters, because we save their old values
    }

    private ArrayList<Insertion> createEmptyInsertsList(int size) {
        final ArrayList<Insertion> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        return list;
    }

    private ArrayList<MODEL> createEmptyItemsList(int size) {
        final ArrayList<MODEL> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        return list;
    }

    /**
     * Add all inserts without update counters
     */
    public void reAddAllInserts(@NonNull Collection<? extends Insertion> collection) {
        if (collection.size() == 0) {
            return;
        }

        mInserts.addAll(collection);
        getItems().addAll(createEmptyItemsList(collection.size()));
        //no update counters, because we save their old values
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        final int size = collection.size();

        final List<MODEL> onlyItems = getOnlyItems();

        if (onlyItems.size() >= size) {
            boolean lastAutoNotifyDataSetChanged = isAutoNotifyDataSetChanged();
            setAutoNotifyDataSetChanged(false);

            clearOnlyItems();
            onlyItems.removeAll(collection);
            addAll(onlyItems);

            setAutoNotifyDataSetChanged(lastAutoNotifyDataSetChanged);
        }

        notifyIfNeed();
        return true;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends MODEL> collection) {
        if (collection.size() == 0) {
            return false;
        }

        int size = collection.size();

        setHasNewItems(size > 0);

        int insertPosition = getItemCount() <= getFootersCount() ? 0 : getItemCount() - getFootersCount();

        boolean result;

        if (getFootersCount() == 0) {
            mInserts.addAll(createEmptyInsertsList(collection.size()));
            result = getItems().addAll(collection);
        } else {
            mInserts.addAll(insertPosition, createEmptyInsertsList(collection.size()));
            result = getItems().addAll(insertPosition, collection);
        }

        for (MODEL item : collection) {
            updateCounter(item);
        }

        notifyIfNeed();
        return result;
    }

    @Override
    public boolean addAll(int position, @NonNull Collection<? extends MODEL> collection) {
        if (collection.size() == 0) {
            return false;
        }

        int size = collection.size();

        setHasNewItems(size > 0);

        int insertPosition = calcInsertPosition(position);

        boolean result;

        if (insertPosition == getItemCount()) {
            mInserts.addAll(createEmptyInsertsList(collection.size()));
            result = getItems().addAll(collection);
        } else {
            mInserts.addAll(insertPosition, createEmptyInsertsList(collection.size()));
            result = getItems().addAll(insertPosition, collection);
        }

        for (MODEL item : collection) {
            updateCounter(item);
        }

        notifyIfNeed();
        return result;
    }

    /**
     * Add header before all items and after TYPE_ABSOLUTE_HEADER
     *
     * @param layoutId header layout
     * @param data     data to binding
     */
    public void addHeader(@LayoutRes int layoutId, Object data) {
        int insertPosition = calcInsertPosition(0);
        mInserts.add(insertPosition, new Insertion(layoutId, data, Insertion.TYPE_HEADER));
        getItems().add(insertPosition, null);
        mHeadersCount += 1;

        notifyIfNeed();
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
        int absoluteFootersCount = getAbsoluteFootersCount();
        if (absoluteFootersCount > 0) {
            int position = getItemCount() <= absoluteFootersCount ? 0 : getItemCount() - absoluteFootersCount;
            mInserts.add(position, new Insertion(layoutId, data, Insertion.TYPE_FOOTER));
            getItems().add(position, null);
            mFootersCount += 1;

        } else {
            mInserts.add(new Insertion(layoutId, data, Insertion.TYPE_FOOTER));
            getItems().add(null);
            mFootersCount += 1;
        }

        notifyIfNeed();
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
        if (isInsertion(position)) {
            return TYPE_INSERTION;
        }

        return TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup vParent, int viewType) {
        switch (viewType) {
            case TYPE_INSERTION:
                return newInsertionViewHolder(vParent);
            default:
                return super.onCreateViewHolder(vParent, viewType);
        }
    }

    /**
     * Override this method if you need to bind view getHolder for insertions
     * <p/>
     * viewType == 100500
     */
    public RecyclerView.ViewHolder newInsertionViewHolder(ViewGroup vParent) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.layout_root, vParent, false);

        return new InsertViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Insertion insertion = getInsertion(position);
        if (insertion == null) {
            super.onBindViewHolder(holder, position);
            return;
        }

        final LayoutInflater inflater = LayoutInflater.from(getContext());

        final FrameLayout itemView = (FrameLayout) holder.itemView;

        final ViewGroup vContainer = (ViewGroup) inflater.inflate(insertion.getLayoutId(), itemView, false);

        if (holder instanceof HandyHolder) {
            ((HandyHolder) holder).setContainerView(vContainer);
        }

        itemView.removeAllViews();
        itemView.addView(vContainer);
        vContainer.setClickable(true);

        if (mOnInsertClickListener != null) {
            final RecyclerView.ViewHolder finalHolder = holder;
            vContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInsertClickListener.onClick(insertion, finalHolder.getAdapterPosition());
                }
            });
        }

        if (holder instanceof HandyHolder) {
            ((HandyHolder) holder).onInit(itemView);
        }

        onBindInsertionViewHolder(holder, position);

        switch (insertion.getType()) {
            case Insertion.TYPE_HEADER:
                onBindHeaderViewHolder(holder, position);
                break;
            case Insertion.TYPE_FOOTER:
                onBindFooterViewHolder(holder, position);
                break;
        }

        setLastHolder(position == getItemCount() - 1 ? holder : null);
    }

    protected void updateInsertClickListener(final int position, @NonNull View view) {
        view.setOnClickListener(null);

        if (mOnInsertClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnInsertClickListener.onClick(getInsertion(position), position);
                }
            });
        }

        if (mOnInsertViewsClickListener != null) {
            if (mClickableInsertViewsList != null) {
                for (final int viewId : mClickableInsertViewsList) {
                    updateInsertChildViewClickListener(position, view, viewId);
                }
            }

            if (mClickableInsertViewsArray != null) {
                for (final int viewId : mClickableInsertViewsArray) {
                    updateInsertChildViewClickListener(position, view, viewId);
                }
            }
        }
    }

    private void updateInsertChildViewClickListener(final int position, @NonNull View vRoot, final int viewId) {
        vRoot.findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnInsertViewsClickListener.onClick(viewId, getInsertion(position), position);
            }
        });
    }

    /**
     * Override this method to update your insertions
     *
     * @param holder   view getHolder
     * @param position insertion position
     */
    protected void onBindInsertionViewHolder(RecyclerView.ViewHolder holder, int position) {
        updateInsertClickListener(position, holder.itemView);
    }

    /**
     * Override this method to update your footers
     *
     * @param holder   view getHolder
     * @param position footer position
     */
    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    /**
     * Override this method to update your headers
     *
     * @param holder   view getHolder
     * @param position header position
     */
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
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
        Insertion insertion = new Insertion(layoutId, null, type);

        mInserts.add(insertion);
        getItems().add(null);
        mFootersCount += 1;

        notifyChangedIfNeed(getItemCount() - 1);
    }


    /**
     * @return item or null if it is insertion object
     */
    @Nullable
    public MODEL getItem(int location) {
        return get(location);
    }

    /**
     * @return insertion or null if it is item object
     */
    @Nullable
    public Insertion getInsertion(int position) {
        return mInserts.size() == 0 ? null : mInserts.get(position);
    }

    public boolean isInsertion(int position) {
        return mInserts.size() != 0 && mInserts.get(position) != null;//and getItems().get(position) == null
    }

    private boolean isItem(int position) {
        return get(position) != null;
    }


    public OnClickListener<Insertion> getOnInsertionClickListener() {
        return mOnInsertClickListener;
    }

    public void setOnInsertClickListener(OnClickListener<Insertion> onInsertClickListener) {
        mOnInsertClickListener = onInsertClickListener;
    }


    public void setOnInsertViewsClickListener(int[] clickableViews,
                                              OnViewsClickListener<Insertion> onInsertViewsClickListener) {
        mClickableInsertViewsArray = clickableViews;
        mOnInsertViewsClickListener = onInsertViewsClickListener;
    }

    public void setOnInsertViewsClickListener(List<Integer> clickableViews,
                                              OnViewsClickListener<Insertion> onInsertViewsClickListener) {
        mClickableInsertViewsList = clickableViews;
        mOnInsertViewsClickListener = onInsertViewsClickListener;
    }

    public static class InsertViewHolder extends HandyHolder {

        public InsertViewHolder(ViewGroup vParent) {
            super(vParent);
            set().isRecyclable(false);
        }
    }

}
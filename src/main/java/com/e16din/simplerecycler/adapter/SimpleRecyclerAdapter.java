package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Descriptions for List-methods taken from the documentation:
 * https://developer.android.com/reference/java/util/List.html
 */
public abstract class SimpleRecyclerAdapter<MODEL, HOLDER extends SimpleViewHolder>
        extends RecyclerView.Adapter<SimpleViewHolder> implements List<MODEL> {

    public interface OnItemClickListener<MODEL> {
        void onClick(MODEL item, int position);
    }

    public interface OnItemViewsClickListener<MODEL> {
        void onClick(@IdRes int childViewId, MODEL item, int position);
    }


    public static final int TYPE_DEFAULT = 0;
    public static final int INVALID_VALUE = -1;


    private final Context mContext;

    private List<MODEL> mItems;

    private int mItemLayoutId;

    private OnItemClickListener<MODEL> mOnItemClickListener;
    private OnItemViewsClickListener<MODEL> mOnItemViewsClickListener;

    private Runnable mOnLastItemListener;

    private boolean mRippleEffectEnabled = true;
    private SimpleViewHolder mLastHolder;

    private boolean mHasNewItems;
    private List<Integer> mClickableViewsList;
    private int[] mClickableViewsArray;


    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId,
                                 OnItemClickListener<MODEL> onItemClickListener) {
        mContext = context;
        mItems = items;
        mItemLayoutId = resId;
        mOnItemClickListener = onItemClickListener;
        onInit();
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId) {
        this(context, items, resId, null);
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        this(context, items, 0, null);
    }

    public SimpleRecyclerAdapter(@NonNull Context context) {
        this(context, new ArrayList<MODEL>());
    }

    protected void onInit() {
        //for override
    }

    public void onLastItem() {
        setHasNewItems(false);

        if (mOnLastItemListener != null) {
            mOnLastItemListener.run();
        }
    }

    protected int calcInsertPosition(int position) {
        return position;
    }

    protected void removeLast() {
        remove(getLastItemPosition());
    }

    protected void removeFirst() {
        remove(0);
    }

    protected abstract HOLDER newViewHolder(View v);

    protected HOLDER rippledViewHolder(View v) {
        HOLDER holder = newViewHolder(v);

        addRippleEffectToHolder((ViewGroup) v, holder);

        return holder;
    }

    /**
     * Add ripple effect to view holder
     *
     * @param vRoot  root view
     * @param holder simple view holder
     */
    protected void addRippleEffectToHolder(@NonNull ViewGroup vRoot, @NonNull SimpleViewHolder holder) {
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

    protected void onBindItemViewHolder(HOLDER holder, int position) {
        updateItemClickListener(position, holder.itemView);
    }


    protected void updateItemClickListener(final int position, @NonNull View vRoot) {
        vRoot.setOnClickListener(null);


        if (mOnItemClickListener == null) return;


        final MODEL item = get(position);

        vRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onClick(item, position);
            }
        });


        if (mOnItemViewsClickListener == null) return;


        if (mClickableViewsList != null) {
            for (final int viewId : mClickableViewsList) {
                updateChildViewClickListener(position, vRoot, item, viewId);
            }
        }

        if (mClickableViewsArray != null) {
            for (final int viewId : mClickableViewsArray) {
                updateChildViewClickListener(position, vRoot, item, viewId);
            }
        }
    }

    private void updateChildViewClickListener(final int position, @NonNull View vRoot, final MODEL item, final int viewId) {
        vRoot.findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemViewsClickListener.onClick(viewId, item, position);
            }
        });
    }

    protected Context getContext() {
        return mContext;
    }

    protected int getClickedViewId(ViewGroup view, MotionEvent e) {
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

    public void setItemLayoutId(@LayoutRes int layoutId) {
        mItemLayoutId = layoutId;
    }

    public void setOnItemClickListener(OnItemClickListener<MODEL> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemViewsClickListener(List<Integer> clickableViews,
                                            OnItemViewsClickListener<MODEL> onItemViewsClickListener) {
        mClickableViewsList = clickableViews;
        mOnItemViewsClickListener = onItemViewsClickListener;
    }

    public void setOnItemViewsClickListener(int[] clickableViews,
                                            OnItemViewsClickListener<MODEL> onItemViewsClickListener) {
        mClickableViewsArray = clickableViews;
        mOnItemViewsClickListener = onItemViewsClickListener;
    }

    public List<MODEL> getItems() {
        return mItems;
    }

    public Runnable getOnLastItemListener() {
        return mOnLastItemListener;
    }

    public OnItemClickListener<MODEL> getOnItemClickListener() {
        return mOnItemClickListener;
    }

    //use with SimpleRecyclerView
    public void setOnLastItemListener(Runnable onLastItemListener) {
        this.mOnLastItemListener = onLastItemListener;
    }


    public OnItemViewsClickListener<MODEL> getOnItemViewsClickListener() {
        return mOnItemViewsClickListener;
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
    public int getLastItemPosition() {
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

    public List<Integer> getClickableViewsList() {
        return mClickableViewsList;
    }


    //- RecyclerView.Adapter


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout vContainer = (FrameLayout)
                LayoutInflater.from(getContext()).inflate(R.layout.layout_container, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        vContainer.addView(v);
        return rippledViewHolder(vContainer);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        onBindItemViewHolder((HOLDER) holder, position);

        setLastHolder(position == getItemCount() - 1 ? holder : null);
    }


    //- List
    //todo: Update descriptions


    /**
     * Replaces the element at the specified location in this List with the specified object. This operation does not change the size of the List.
     * <p/>
     *
     * @param location the index at which to put the specified object.
     * @param object   the object to insert.
     *                 <p/>
     * @return the previous element at the index.
     * <p/>
     * @throws UnsupportedOperationException if replacing elements in this List is not supported.
     * @throws ClassCastException            if the class of an object is inappropriate for this List.
     * @throws IllegalArgumentException      if an object cannot be added to this List.
     * @throws IndexOutOfBoundsException     if location < 0 || location >= size()
     */
    @Override
    public MODEL set(int location, MODEL object) {
        return mItems.set(location, object);
    }

    /**
     * Inserts the specified object into this List at the specified location.
     * The object is inserted before the current element at the specified location.
     * If the location is equal to the size of this List, the object is added at the end.
     * If the location is smaller than the size of this List, then all elements beyond the specified location are
     * moved by one position towards the end of the List.
     * <p/>
     *
     * @param location the index at which to insert.
     * @param object   the object to add.
     *                 <p/>
     * @throws UnsupportedOperationException if adding to this List is not supported.
     * @throws ClassCastException            if the class of the object is inappropriate for this List.
     * @throws IllegalArgumentException      if the object cannot be added to this List.
     * @throws IndexOutOfBoundsException     if location < 0 || location > size()
     */
    @Override
    public void add(int location, MODEL object) {
        setHasNewItems(true);

        int insertPosition = calcInsertPosition(location);
        if (insertPosition == getItemCount()) {
            mItems.add(object);
        } else {
            mItems.add(insertPosition, object);
        }
        notifyDataSetChanged();
    }


    /**
     * Adds the specified object at the end of this List.
     * <p/>
     *
     * @param object the object to add.
     *               <p/>
     * @return always true.
     * <p/>
     * @throws UnsupportedOperationException if adding to this List is not supported.
     * @throws ClassCastException            if the class of the object is inappropriate for this List.
     * @throws IllegalArgumentException      if the object cannot be added to this List.
     */
    @Override
    public boolean add(MODEL object) {
        add(getItemCount(), object);
        return true;
    }

    @Override
    public MODEL remove(int position) {
        MODEL result = null;
        if (getItemCount() >= position) {
            result = mItems.remove(position);

            notifyDataSetChanged();
        }
        return result;
    }

    /**
     * Searches this List for the specified object and returns the index of the first occurrence.
     * <br/>
     *
     * @param object the object to search for.
     *               <br/>
     * @return the index of the first occurrence of the object or -1 if the object was not found.
     */
    @Override
    public int indexOf(Object object) {
        return mItems.indexOf(object);
    }

    /**
     * Searches this List for the specified object and returns the index of the last occurrence.
     * <p/>
     *
     * @param object the object to search for.
     *               <p/>
     * @return the index of the last occurrence of the object, or -1 if the object was not found.
     */
    @Override
    public int lastIndexOf(Object object) {
        return mItems.lastIndexOf(object);
    }

    @Override
    public ListIterator<MODEL> listIterator() {
        return mItems.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<MODEL> listIterator(int i) {
        return mItems.listIterator(i);
    }

    @NonNull
    @Override
    public List<MODEL> subList(int i, int i1) {
        return mItems.subList(i, i1);
    }

    @Override
    public int size() {
        return mItems.size();
    }

    @Override
    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mItems.contains(o);
    }

    @NonNull
    @Override
    public Iterator<MODEL> iterator() {
        return mItems.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return mItems.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        return mItems.toArray(ts);
    }

    @Override
    public boolean remove(Object o) {
        return mItems.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return mItems.containsAll(collection);
    }


    /**
     * Adds the objects in the specified collection to the end of this List.
     * The objects are added in the order in which they are returned from the collection's iterator.
     * <p/>
     *
     * @param collection the collection of objects.
     *                   <p/>
     * @return true if this List is modified, false otherwise (i.e. if the passed collection was empty).
     * <p/>
     * @throws UnsupportedOperationException if adding to this List is not supported.
     * @throws ClassCastException            if the class of an object is inappropriate for this List.
     * @throws IllegalArgumentException      if an object cannot be added to this List.
     * @throws NullPointerException          if collection is null.
     */
    @Override
    public boolean addAll(@NonNull Collection<? extends MODEL> collection) {
        return addAll(getItemCount(), collection);
    }

    /**
     * Inserts the objects in the specified collection at the specified location in this List.
     * The objects are added in the order they are returned from the collection's iterator.
     * <p/>
     *
     * @param location   the index at which to insert.
     * @param collection the collection of objects to be inserted.
     *                   <p/>
     * @return true if this List has been modified through the insertion, false otherwise (i.e. if the passed
     * collection was
     * empty).
     * <p/>
     * @throws UnsupportedOperationException if adding to this List is not supported.
     * @throws ClassCastException            if the class of an object is inappropriate for this List.
     * @throws IllegalArgumentException      if an object cannot be added to this List.
     * @throws IndexOutOfBoundsException     if location < 0 || location > size().
     * @throws NullPointerException          if collection is null.
     */
    @Override
    public boolean addAll(int location, @NonNull Collection<? extends MODEL> collection) {
        if (collection.size() == 0) {
            return false;
        }

        setHasNewItems(collection.size() > 0);

        int insertPosition = calcInsertPosition(location);

        boolean result;

        if (insertPosition == getItemCount()) {
            result = mItems.addAll(collection);
        } else {
            result = mItems.addAll(insertPosition, collection);
        }
        notifyItemRangeInserted(insertPosition, collection.size());

        return result;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        boolean result = mItems.removeAll(collection);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return mItems.retainAll(collection);
    }

    /**
     * Removes all elements from this List, leaving it empty.
     *
     * @throws UnsupportedOperationException if removing from this List is not supported.
     */
    @Override
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    /**
     * Returns the element at the specified location in this List.
     * <p/>
     *
     * @param location the index of the element to return.
     *                 <p/>
     * @return the element at the specified location.
     * <p/>
     * @throws IndexOutOfBoundsException if location < 0 || location >= size()
     */
    @Override
    public MODEL get(int location) {
        return mItems.get(location);
    }


    //- Deprecated


    /**
     * @deprecated use method clear() instead this
     */
    @Deprecated
    public void clearAll() {
        clear();
    }

    /**
     * @deprecated use method get() instead this
     */
    @Deprecated
    public MODEL getItem(int position) {
        return get(position);
    }
}


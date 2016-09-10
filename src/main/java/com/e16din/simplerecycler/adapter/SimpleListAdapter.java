package com.e16din.simplerecycler.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.e16din.handyholder.holder.HandyHolder;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;


/**
 * Descriptions for List-methods taken from the documentation:
 * https://developer.android.com/reference/java/util/List.html
 */
@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleListAdapter<HOLDER extends HandyHolder, MODEL>
        extends SimpleBaseAdapter<HOLDER, MODEL> implements List<MODEL> {

    private boolean mAutoNotifyDataSetChanged = true;


    public SimpleListAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleListAdapter(@NonNull Context context) {
        super(context);
    }

    public void setAutoNotifyDataSetChanged(boolean autoNotifyDataSetChanged) {
        mAutoNotifyDataSetChanged = autoNotifyDataSetChanged;
    }

    public boolean isAutoNotifyDataSetChanged() {
        return mAutoNotifyDataSetChanged;
    }

    protected void notifyIfNeed() {
        if (isAutoNotifyDataSetChanged()) {
            notifyDataSetChanged();
        }
    }

    protected void notifyChangedIfNeed(int position) {
        if (isAutoNotifyDataSetChanged()) {
            notifyItemChanged(position);
        }
    }

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
        final MODEL result = mItems.set(location, object);
        notifyChangedIfNeed(location);
        return result;
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
        int insertPosition = calcInsertPosition(location);
        if (insertPosition == getItemCount()) {
            mItems.add(object);
        } else {
            mItems.add(insertPosition, object);
        }

        notifyIfNeed();
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
        if (getItemCount() > position) {
            result = mItems.remove(position);

            notifyIfNeed();
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
    public <T> T[] toArray(@NonNull T[] ts) {
        return mItems.toArray(ts);
    }

    @Override
    public boolean remove(Object o) {
        final boolean result = mItems.remove(o);
        notifyIfNeed();
        return result;
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

        int insertPosition = calcInsertPosition(location);

        boolean result;

        if (insertPosition == getItemCount()) {
            result = mItems.addAll(collection);
        } else {
            result = mItems.addAll(insertPosition, collection);
        }

        notifyIfNeed();

        return result;
    }


    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        boolean result = mItems.removeAll(collection);
        notifyIfNeed();
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
        notifyIfNeed();
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

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void forEach(Consumer<? super MODEL> action) {
        mItems.forEach(action);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public Spliterator<MODEL> spliterator() {
        return mItems.spliterator();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public Stream<MODEL> stream() {
        return mItems.stream();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public Stream<MODEL> parallelStream() {
        return mItems.parallelStream();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void replaceAll(UnaryOperator<MODEL> operator) {
        mItems.replaceAll(operator);
        notifyIfNeed();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void sort(Comparator<? super MODEL> c) {
        mItems.sort(c);
        notifyIfNeed();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean removeIf(Predicate<? super MODEL> filter) {
        final boolean result = mItems.removeIf(filter);
        notifyIfNeed();
        return result;
    }
}


package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e16din.handyholder.HandyCreator;
import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.model.Insertion;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimplePagingAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleBindListenerAdapter<HOLDER, MODEL> {

    public static final int TYPE_BOTTOM_PROGRESS = Insertion.TYPE_ABSOLUTE_FOOTER + 1;

    @LayoutRes
    private int mBottomProgressLayoutId = R.layout.layout_bottom_progress;

    private int mPageSize = 10;

    private boolean mAllItemsLoaded;
    private boolean mNeedShowProgressFromStart = true;


    public SimplePagingAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimplePagingAdapter(@NonNull Context context) {
        super(context);
    }

    /**
     * Override this method if you need to bind view getHolder for insertions
     * <p/>
     * viewType == 100500
     */
    public RecyclerView.ViewHolder newInsertionViewHolder(ViewGroup vParent) {
        return new HandyCreator<>().create(this, vParent, mBottomProgressLayoutId)
                .set()
                .isRecyclable(false)
                .asyncInflating(false)
                .rippleEffect(false)
                .init();
    }

    @Override
    public void onLastItem() {
        if (getOnlyItemsCount() >= mPageSize) {
            if (!mAllItemsLoaded) {
                fireOnLastItem();
            } else {
                setHasNewItems(false);
            }
        }
    }

    public void fireOnLastItem() {
        super.onLastItem();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mNeedShowProgressFromStart) {
            showBottomProgress();
        }
    }

    /**
     * Compute mAllItemsLoaded,
     * if size < mPageSize then hide bottom progress and no more show it.
     *
     * @param size Size of items list
     */
    public void onNewItemsAdded(int size) {
        if (size < mPageSize) {
            setAllItemsLoaded(true);
            hideBottomProgress();
        }
    }

    /**
     * Add new page to adapter.
     * Call method onNewItemsAdded which check size of array and compute mAllItemsLoaded,
     * if items.size() < mPageSize then hide bottom progress and no more show it.
     *
     * @param items Items
     */
    public void addPage(@NonNull List<MODEL> items) {
        setAutoNotifyDataSetChanged(false);
        addAll(items);
        setAutoNotifyDataSetChanged(true);
        onNewItemsAdded(items.size());

        notifyItemRangeInsertedIfNeed(size() - (items.size() + getHeadersCount()), items.size());
    }

    /**
     * Add new page to adapter.
     * Call method onNewItemsAdded which check size of array and compute mAllItemsLoaded,
     * if items.size() < mPageSize then hide bottom progress and no more show it.
     *
     * @param position Insert position
     * @param items    Items
     */
    public void addPage(int position, @NonNull List<MODEL> items) {
        setAutoNotifyDataSetChanged(false);
        addAll(position, items);
        setAutoNotifyDataSetChanged(true);
        onNewItemsAdded(items.size());

        notifyItemRangeInsertedIfNeed(size() - (items.size() + getHeadersCount()), items.size());
    }

    public void showBottomProgress() {
        if (!hasBottomProgress()) {
            fireShowBottomProgress();
        }
    }

    public void fireShowBottomProgress() {
        addAbsoluteFooter(mBottomProgressLayoutId, TYPE_BOTTOM_PROGRESS);
    }

    public void hideBottomProgress() {
        int position = getBottomProgressPosition();

        if (position >= 0) {
            remove(position);
        }
    }

    /**
     * Bottom progress item position.
     *
     * @return bottom progress item position or -1 if array is empty
     */
    protected int getBottomProgressPosition() {
        int result = -1;
        if (getItemCount() == 0) return result;

        for (int i = getItemCount() - 1; i >= 0; i--) {
            final Insertion insertion = getInsertion(i);
            if (insertion != null && insertion.getType() == TYPE_BOTTOM_PROGRESS) {
                result = i;
                break;
            }
        }

        return result;
    }

    @Override
    public void clearAllInsertions() {
        boolean hasBottomProgress = hasBottomProgress();

        fireClearAllInsertions();

        if (hasBottomProgress) {
            showBottomProgress();
        }
    }

    @Override
    public void clearFooters() {
        boolean hasBottomProgress = hasBottomProgress();

        fireClearFooters();

        if (hasBottomProgress) {
            showBottomProgress();
        }
    }

    public void fireClearFooters() {
        super.clearFooters();
    }

    public void fireClearAllInsertions() {
        super.clearAllInsertions();
    }

    public boolean hasBottomProgress() {
        return getBottomProgressPosition() >= 0;
    }

    public void setBottomProgressLayoutId(int bottomProgressLayoutId) {
        mBottomProgressLayoutId = bottomProgressLayoutId;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    public boolean isAllItemsLoaded() {
        return mAllItemsLoaded;
    }

    public void setAllItemsLoaded(boolean allItemsLoaded) {
        mAllItemsLoaded = allItemsLoaded;
    }

    public void setNeedShowProgressFromStart(boolean needShowProgressFromStart) {
        mNeedShowProgressFromStart = needShowProgressFromStart;
    }
}

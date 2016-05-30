package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.model.BottomProgress;
import com.e16din.simplerecycler.model.Insertion;

import java.util.List;

public abstract class SimplePagingAdapter<H extends SimpleViewHolder, T>
        extends SimpleRecyclerAdapter<H, T> {

    @LayoutRes
    private int mBottomProgressLayoutId = R.layout.layout_bottom_progress;


    public SimplePagingAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                               OnItemClickListener<T> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public SimplePagingAdapter(@NonNull Context context, @NonNull List<Object> items, int resId) {
        super(context, items, resId);
    }

    public SimplePagingAdapter(@NonNull Context context, @NonNull List<Object> items) {
        super(context, items);
    }

    public SimplePagingAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onLastItem() {
        showBottomProgress();
    }

    public void showBottomProgress() {
        if (!hasBottomProgress()) addInsertion(new BottomProgress(mBottomProgressLayoutId));
    }

    public void hideBottomProgress() {
        if (hasBottomProgress()) removeLast();
    }

    public boolean hasBottomProgress() {
        for (int i = getItemCount() - 1; i > 0; i--) {
            if (isInsertion(i)) {
                Insertion insert = getInsertion(i);
                if (insert.getType() == BottomProgress.TYPE_BOTTOM_PROGRESS) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    public void setBottomProgressLayoutId(int bottomProgressLayoutId) {
        mBottomProgressLayoutId = bottomProgressLayoutId;
    }
}

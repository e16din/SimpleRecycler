package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

public abstract class SimpleRecyclerAdapter<H extends SimpleViewHolder, T> extends BaseInsertsAdapter<H, T> {

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items, int resId,
                                 OnItemClickListener<T> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items, int resId) {
        super(context, items, resId);
    }

    public SimpleRecyclerAdapter(@NonNull Context context, @NonNull List<Object> items) {
        super(context, items);
    }

    public SimpleRecyclerAdapter(@NonNull Context context) {
        super(context);
    }
}

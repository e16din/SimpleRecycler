package com.e16din.simplerecycler.adapter.holders;

import android.support.annotation.LayoutRes;
import android.view.View;

public abstract class ItemViewHolder<MODEL> extends SimpleViewHolder {

    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public ItemViewHolder<MODEL> layoutId(@LayoutRes int layoutId) {
        return (ItemViewHolder<MODEL>) super.layoutId(layoutId);
    }

    public void bindItem(MODEL item, int position) {
    }
}
package com.e16din.simplerecycler.adapter.holders;

import android.view.View;

public abstract class ItemViewHolder<MODEL> extends SimpleViewHolder {

    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bindItem(MODEL item, int position) {
    }
}
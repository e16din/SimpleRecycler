package com.e16din.simplerecycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public abstract class SimpleViewHolder extends RecyclerView.ViewHolder {
    protected View vContainer;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        vContainer = itemView;
    }
}
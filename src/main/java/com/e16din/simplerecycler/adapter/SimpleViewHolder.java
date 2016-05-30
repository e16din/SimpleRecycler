package com.e16din.simplerecycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SimpleViewHolder extends RecyclerView.ViewHolder {
    protected View vContainer;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        vContainer = itemView;
    }
}
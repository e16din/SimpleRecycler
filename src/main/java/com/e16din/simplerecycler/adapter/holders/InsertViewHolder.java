package com.e16din.simplerecycler.adapter.holders;

import android.view.View;

import com.e16din.simplerecycler.model.Insertion;

public abstract class InsertViewHolder extends SimpleViewHolder {

    public InsertViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindInsert(Insertion item, int position);
}
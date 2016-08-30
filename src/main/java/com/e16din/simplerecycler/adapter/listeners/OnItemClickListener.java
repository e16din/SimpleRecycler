package com.e16din.simplerecycler.adapter.listeners;

public interface OnItemClickListener<MODEL> {
    void onClick(MODEL item, int position);
}
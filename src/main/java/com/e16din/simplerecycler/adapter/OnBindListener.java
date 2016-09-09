package com.e16din.simplerecycler.adapter;

public interface OnBindListener<MODEL> {
    void onBind(MODEL item, int position);
}

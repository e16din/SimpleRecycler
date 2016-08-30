package com.e16din.simplerecycler.adapter.listeners;

import android.support.annotation.IdRes;

public interface OnItemViewsClickListener<MODEL> {
    void onClick(@IdRes int childViewId, MODEL item, int position);
}
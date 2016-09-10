package com.e16din.simplerecycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e16din.handyholder.holder.HandyHolder;
import com.e16din.handyholder.wrapper.StrongHandy;

public abstract class HandyHolderWrapper<MODEL> extends StrongHandy<RecyclerView.Adapter, HandyHolder, MODEL> {
    public HandyHolderWrapper(RecyclerView.Adapter adapter, ViewGroup vParent) {
        super(adapter, vParent);
    }

    public HandyHolderWrapper(RecyclerView.Adapter adapter, ViewGroup vParent, int layoutId) {
        super(adapter, vParent, layoutId);
    }
}
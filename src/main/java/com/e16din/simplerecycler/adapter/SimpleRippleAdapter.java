package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.e16din.handyholder.HandyHolder;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleRippleAdapter<MODEL> extends SimpleBindListenerAdapter<MODEL> {

    private boolean mRippleEffect = true;

    public SimpleRippleAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleRippleAdapter(@NonNull Context context) {
        super(context);
    }


    @Override
    public HandyHolder<MODEL> onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType)
                .rippleEffect(mRippleEffect);
    }

    public boolean isRippleEffect() {
        return mRippleEffect;
    }

    public void setRippleEffect(boolean rippleEffect) {
        mRippleEffect = rippleEffect;
    }
}

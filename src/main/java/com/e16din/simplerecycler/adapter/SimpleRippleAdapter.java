package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.e16din.handyholder.holder.HandyHolder;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleRippleAdapter<HOLDER extends HandyHolder, MODEL>
        extends SimpleInsertsAdapter<HOLDER, MODEL> {

    private boolean mRippleEffect = true;

    public SimpleRippleAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleRippleAdapter(@NonNull Context context) {
        super(context);
    }

    public boolean isRippleEffect() {
        return mRippleEffect;
    }

    public void setRippleEffect(boolean rippleEffect) {
        mRippleEffect = rippleEffect;
    }
}

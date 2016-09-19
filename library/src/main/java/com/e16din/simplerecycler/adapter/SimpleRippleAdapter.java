package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.e16din.handyholder.AlreadyBox;
import com.e16din.simplerecycler.R;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleRippleAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleHandyHolderAdapter<HOLDER, MODEL> {

    public static final int WRONG_VALUE = -1;

    private boolean mRippleEffect = true;

    @ColorInt
    private int mRippleColor = WRONG_VALUE;

    public SimpleRippleAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleRippleAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onUpdateHandyHolderSettings(AlreadyBox set) {
        if (!set.isAlreadySetRippleEffect()) {
            set.rippleEffect(isRippleEffect());
            //todo: check if(set.mRippleColor == WRONG_VALUE)
            set.rippleColor(mRippleColor != WRONG_VALUE
                    ? mRippleColor
                    : ContextCompat.getColor(getContext(), R.color.simplePressedColor));
        }
    }

    public boolean isRippleEffect() {
        return mRippleEffect;
    }

    public void setRippleEffect(boolean rippleEffect) {
        mRippleEffect = rippleEffect;
    }

    public int getRippleColor() {
        return mRippleColor;
    }

    public void setRippleColor(int rippleColor) {
        mRippleColor = rippleColor;
    }
}

package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.e16din.simplerecycler.R;
import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;
import com.e16din.simplerecycler.adapter.listeners.OnItemClickListener;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleRippleAdapter<MODEL> extends SimpleClickAdapter<MODEL> {

    private boolean mRippleEffectEnabled = true;


    public SimpleRippleAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId,
                               OnItemClickListener<MODEL> onItemClickListener) {
        super(context, items, resId, onItemClickListener);
    }

    public SimpleRippleAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId) {
        super(context, items, resId);
    }

    public SimpleRippleAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleRippleAdapter(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void updateViewHolderSelector(@NonNull SimpleViewHolder holder) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || !mRippleEffectEnabled) {
            super.updateViewHolderSelector(holder);
        } else {// Add ripple effect to view holder
            addRippleEffect(holder);
        }
    }

    protected void addRippleEffect(@NonNull SimpleViewHolder holder) {
        holder.vContainer = (ViewGroup) ((ViewGroup) holder.findViewById(R.id.vSimpleContainer)).getChildAt(0);
        if (holder.vContainer != null) {
            holder.mBackgroundDrawable = holder.vContainer.getBackground();
        }

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
        holder.mSelectorResId = outValue.resourceId;
    }

    public void refreshBackgrounds() {
        //todo: reset all ViewHolders
    }

    public void setRippleEffectEnabled(boolean rippleEffectEnabled) {
        mRippleEffectEnabled = rippleEffectEnabled;
    }

    public boolean isRippleEffectEnabled() {
        return mRippleEffectEnabled;
    }
}


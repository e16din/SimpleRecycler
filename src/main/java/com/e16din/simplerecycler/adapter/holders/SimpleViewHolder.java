package com.e16din.simplerecycler.adapter.holders;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class SimpleViewHolder extends RecyclerView.ViewHolder {

    public ViewGroup vContainer;

    public int mLayoutId;

    public Drawable mBackgroundDrawable;
    public int mSelectorResId;
    public boolean mIsSelectorEnabled = true;

    public boolean mInflated;

    public SimpleViewHolder(View itemView) {
        super(itemView);

        findViews();
        init(itemView);
        resetBackgrounds();
    }

    public SimpleViewHolder layoutId(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
        return this;
    }

    /**
     * Initialize all your views here.
     * <p/>
     * It will be used again after async inflating will be completed.
     */
    public abstract void init(View v);

    public void reInit(View v) {
        init(v);
    }

    public void reInit() {
        reInit(itemView);
    }

    /**
     * @deprecated Use init()
     */
    @Deprecated
    public void findViews() {
    }

    public final View findViewById(int id) {
        return itemView.findViewById(id);
    }

    public void resetBackgrounds() {
        if (itemView.getBackground() != null) {
            updateSelectorIfNeed();
            return;
        }

        if (itemView instanceof ViewGroup) {
            if (vContainer != null) {
                mBackgroundDrawable = vContainer.getBackground();

                updateSelectorIfNeed();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemView.setBackground(mBackgroundDrawable);
            } else {
                itemView.setBackgroundDrawable(mBackgroundDrawable);
            }
        }
    }

    private void updateSelectorIfNeed() {
        if (mIsSelectorEnabled && vContainer != null) {
            vContainer.setBackgroundResource(mSelectorResId);
        }
    }

    public boolean isInflated() {
        return mInflated;
    }

    public SimpleViewHolder setInflated(boolean inflated) {
        mInflated = inflated;
        return this;
    }

    public void onDetached() {
    }
}
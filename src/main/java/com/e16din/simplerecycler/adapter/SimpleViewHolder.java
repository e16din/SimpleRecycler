package com.e16din.simplerecycler.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class SimpleViewHolder extends RecyclerView.ViewHolder {
    View vFirstChild;
    Drawable mBackgroundDrawable;
    int mSelectorResId;
    boolean mIsSelectorEnabled = true;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        reset();
    }

    public void reset() {
        if (itemView instanceof ViewGroup) {
            if (mIsSelectorEnabled && vFirstChild != null) {
                vFirstChild.setBackgroundResource(mSelectorResId);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemView.setBackground(mBackgroundDrawable);
            } else {
                itemView.setBackgroundDrawable(mBackgroundDrawable);
            }
        }
    }
}
package com.e16din.simplerecycler.adapter.holders;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class SimpleViewHolder extends RecyclerView.ViewHolder {

    public View vContainer;

    public Drawable mBackgroundDrawable;
    public int mSelectorResId;
    public boolean mIsSelectorEnabled = true;


    public SimpleViewHolder(View itemView) {
        super(itemView);
        reset();
    }

    public final View findViewById(int id) {
        return itemView.findViewById(id);
    }

    public void reset() {
        if (itemView instanceof ViewGroup) {
            if (mIsSelectorEnabled && vContainer != null) {
                vContainer.setBackgroundResource(mSelectorResId);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemView.setBackground(mBackgroundDrawable);
            } else {
                itemView.setBackgroundDrawable(mBackgroundDrawable);
            }
        }
    }
}
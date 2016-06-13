package com.e16din.simplerecycler.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.e16din.simplerecycler.adapter.SimpleRecyclerAdapter;

public class OnScrollSimpleListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView vRecycler, int dx, int dy) {
        super.onScrolled(vRecycler, dx, dy);

        Rect scrollBounds = new Rect();
        vRecycler.getHitRect(scrollBounds);

        SimpleRecyclerAdapter adapter = (SimpleRecyclerAdapter) vRecycler.getAdapter();

        if (getCondition(adapter)) {
            View vLast = adapter.getLastHolder().itemView;
            if (vLast.getLocalVisibleRect(scrollBounds)) {
                // Any portion of the vLast, even a single pixel, is within the visible window
                adapter.onLastItem();
            }
        }
    }

    public boolean getCondition(SimpleRecyclerAdapter adapter) {
        return adapter.getLastHolder() != null && adapter.hasNewItems();
    }
}

package com.e16din.simplerecycler.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.e16din.simplerecycler.adapter.SimpleRecyclerAdapter;

public class OnScrollSimpleListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLinearLayoutManager;
    private int lastTotalItemCount;


    public OnScrollSimpleListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView vRecycler, int dx, int dy) {
        super.onScrolled(vRecycler, dx, dy);

        int visibleItemCount = vRecycler.getChildCount();
        int currentTotalItemCount = vRecycler.getAdapter().getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        boolean isLastItem = firstVisibleItem + visibleItemCount >= currentTotalItemCount;

        if (isLastItem && dy >= 0) {
            if (currentTotalItemCount != lastTotalItemCount) {
                SimpleRecyclerAdapter adapter = (SimpleRecyclerAdapter) vRecycler.getAdapter();
                adapter.onLastItem();
            }
        }

        lastTotalItemCount = currentTotalItemCount;
    }
}

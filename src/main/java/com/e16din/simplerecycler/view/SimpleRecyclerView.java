package com.e16din.simplerecycler.view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class SimpleRecyclerView extends RecyclerView {
    public SimpleRecyclerView(Context context) {
        super(context);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        init();
    }

    private void init() {
        clearOnScrollListeners();
        addOnScrollListener(new OnScrollSimpleListener((LinearLayoutManager) getLayoutManager()));
    }
}

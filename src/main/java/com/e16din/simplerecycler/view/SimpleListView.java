package com.e16din.simplerecycler.view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;


public class SimpleListView extends SimpleRecyclerView {

    public SimpleListView(Context context) {
        super(context);
        init(context);
    }

    public SimpleListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void scrollToPosition(int position) {
        getLayoutManager().scrollToPosition(position);
    }

    private void init(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) super.getLayoutManager();
    }
}

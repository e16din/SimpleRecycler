package com.e16din.simplerecycler.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

        clearOnScrollListeners();
        addOnScrollListener(newListener());
    }

    @NonNull
    protected OnScrollSimpleListener newListener() {
        return new OnScrollSimpleListener();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        try {//workaround for NPE on findMinMaxChildLayoutPositions
            //todo: catch this bug
            super.onMeasure(widthSpec, heightSpec);
        } catch (NullPointerException ignore) {
            ignore.printStackTrace();
        }
    }
}

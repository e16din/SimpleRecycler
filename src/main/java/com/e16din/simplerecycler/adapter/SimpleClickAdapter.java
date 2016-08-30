package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.e16din.simplerecycler.adapter.holders.SimpleViewHolder;
import com.e16din.simplerecycler.adapter.listeners.OnItemClickListener;
import com.e16din.simplerecycler.adapter.listeners.OnItemViewsClickListener;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleClickAdapter<MODEL, HOLDER extends SimpleViewHolder>
        extends SimpleAsyncAdapter<MODEL, HOLDER> implements List<MODEL> {

    private OnItemClickListener<MODEL> mOnItemClickListener;
    private OnItemViewsClickListener<MODEL> mOnItemViewsClickListener;

    private List<Integer> mClickableViewsList;
    private int[] mClickableViewsArray;


    public SimpleClickAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId,
                              OnItemClickListener<MODEL> onItemClickListener) {
        super(context, items, resId);
        mOnItemClickListener = onItemClickListener;
        onInit();
    }

    public SimpleClickAdapter(@NonNull Context context, @NonNull List<MODEL> items, int resId) {
        this(context, items, resId, null);
    }

    public SimpleClickAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        this(context, items, 0, null);
    }

    public SimpleClickAdapter(@NonNull Context context) {
        this(context, new ArrayList<MODEL>());
    }

    @Override
    protected void onBindItemViewHolder(HOLDER holder, int position) {
        updateItemClickListener(holder, position);
    }

    protected void updateItemClickListener(@NonNull HOLDER holder, final int position) {
        final View vRoot = holder.itemView;

        vRoot.setOnClickListener(null);

        if (mOnItemClickListener != null) {
            vRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(get(position), position);
                }
            });
        }

        if (mOnItemViewsClickListener != null) {
            if (mClickableViewsList != null) {
                for (final int viewId : mClickableViewsList) {
                    updateItemChildViewClickListener(position, vRoot, viewId);
                }
            }

            if (mClickableViewsArray != null) {
                for (final int viewId : mClickableViewsArray) {
                    updateItemChildViewClickListener(position, vRoot, viewId);
                }
            }
        }
    }

    private void updateItemChildViewClickListener(final int position, @NonNull View vRoot, final int viewId) {
        vRoot.findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemViewsClickListener.onClick(viewId, get(position), position);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener<MODEL> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public void setOnItemViewsClickListener(List<Integer> clickableViews,
                                            OnItemViewsClickListener<MODEL> onItemViewsClickListener) {
        mClickableViewsList = clickableViews;
        mOnItemViewsClickListener = onItemViewsClickListener;
    }

    public void setOnItemViewsClickListener(int[] clickableViews,
                                            OnItemViewsClickListener<MODEL> onItemViewsClickListener) {
        mClickableViewsArray = clickableViews;
        mOnItemViewsClickListener = onItemViewsClickListener;
    }


    public OnItemClickListener<MODEL> getOnItemClickListener() {
        return mOnItemClickListener;
    }


    public OnItemViewsClickListener<MODEL> getOnItemViewsClickListener() {
        return mOnItemViewsClickListener;
    }


    public List<Integer> getClickableViewsList() {
        return mClickableViewsList;
    }
}


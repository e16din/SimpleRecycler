package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e16din.handyholder.AlreadyBox;
import com.e16din.handyholder.holder.StrongHandyHolder;
import com.e16din.handyholder.wrapper.StrongHandy;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleHandyHolderAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleInsertsAdapter<HOLDER, MODEL> {

    public SimpleHandyHolderAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleHandyHolderAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        final RecyclerView.ViewHolder[] viewHolder = new RecyclerView.ViewHolder[1];

        final StrongHandy<SimpleHandyHolderAdapter, RecyclerView.ViewHolder, MODEL> handy =
                new StrongHandy<SimpleHandyHolderAdapter, RecyclerView.ViewHolder, MODEL>(this, parent) {
                    @Override
                    public RecyclerView.ViewHolder newHolder(ViewGroup vRoot) {
                        if (viewType == TYPE_INSERTION) {
                            viewHolder[0] = newInsertionViewHolder(vRoot);
                            return viewHolder[0];
                        }// else

                        viewHolder[0] = newViewHolder(vRoot, viewType);
                        return viewHolder[0];
                    }
                };

        if (handy.set().isAlreadyInited()) {
            StrongHandyHolder h = (StrongHandyHolder) viewHolder[0];
            h.itemView.setTag(handy);
            return h;
        }

        onUpdateHandyHolderSettings(handy.set());
        final RecyclerView.ViewHolder holder = handy.set().init();
        holder.itemView.setTag(handy);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StrongHandy handy = (StrongHandy) holder.itemView.getTag();

        if (!handy.set().mInflated) return;//wait for async inflater

        handy.set().bindItem(holder, get(position), position);

        super.onBindViewHolder(holder, position);
    }

    protected abstract void onUpdateHandyHolderSettings(AlreadyBox set);
}

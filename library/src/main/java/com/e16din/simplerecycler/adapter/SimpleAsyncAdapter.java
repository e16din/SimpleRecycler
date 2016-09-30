package com.e16din.simplerecycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.e16din.handyholder.settings.AlreadyBox;

import java.util.List;

@SuppressWarnings("unused")//remove it to see unused warnings
public abstract class SimpleAsyncAdapter<HOLDER extends RecyclerView.ViewHolder, MODEL>
        extends SimpleRippleAdapter<HOLDER, MODEL> {

    private boolean mAsyncInflating;

    public SimpleAsyncAdapter(@NonNull Context context, @NonNull List<MODEL> items) {
        super(context, items);
    }

    public SimpleAsyncAdapter(@NonNull Context context) {
        super(context);
    }

    public boolean isAsyncInflating() {
        return mAsyncInflating;
    }

    public void setAsyncInflating(boolean asyncInflating) {
        mAsyncInflating = asyncInflating;
    }

    @Override
    protected void onUpdateHandyHolderSettings(AlreadyBox set) {
        super.onUpdateHandyHolderSettings(set);

        if (!set.isAlreadySetAsyncInflating()) {
            set.asyncInflating(isAsyncInflating());
        }
    }
}

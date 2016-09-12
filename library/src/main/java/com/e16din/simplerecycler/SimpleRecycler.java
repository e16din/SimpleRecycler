package com.e16din.simplerecycler;

import android.content.Context;

import com.e16din.handyholder.holder.HandyHolder;

public final class SimpleRecycler {

    private SimpleRecycler() {
    }

    public static void init(Context context) {
        HandyHolder.init(context);
    }

}

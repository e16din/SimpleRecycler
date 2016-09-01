package com.e16din.simplerecycler;

import android.content.Context;
import android.util.TypedValue;

public class Utils {
    public static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}

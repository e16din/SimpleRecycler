package com.e16din.simplerecycler.model;


public class BottomProgress extends Insertion {

    public static final int TYPE_BOTTOM_PROGRESS = Insertion.TYPE_FOOTER + 1;

    public BottomProgress(int layoutId) {
        super(layoutId, null, TYPE_BOTTOM_PROGRESS);
    }
}

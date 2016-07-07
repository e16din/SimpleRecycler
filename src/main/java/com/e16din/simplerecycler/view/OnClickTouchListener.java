package com.e16din.simplerecycler.view;

import android.view.MotionEvent;
import android.view.View;

import com.e16din.lightutils.utils.U;

public abstract class OnClickTouchListener implements View.OnTouchListener {
    /**
     * Max allowed duration for a "click", in milliseconds.
     */
    private static final int MAX_CLICK_DURATION = 1000;

    /**
     * Max allowed distance to move during a "click", in DP.
     */
    private static final int MAX_CLICK_DISTANCE = 15;


    private long mPressStartTime;
    private float mPressedX;
    private float mPressedY;
    private boolean mStayedWithinClickDistance;


    @Override
    public boolean onTouch(View view, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPressStartTime = System.currentTimeMillis();
                mPressedX = e.getX();
                mPressedY = e.getY();
                mStayedWithinClickDistance = true;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mStayedWithinClickDistance && distance(mPressedX, mPressedY,
                        e.getX(), e.getY()) > MAX_CLICK_DISTANCE) {
                    mStayedWithinClickDistance = false;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                long pressDuration = System.currentTimeMillis() - mPressStartTime;
                if (pressDuration < MAX_CLICK_DURATION && mStayedWithinClickDistance) {

                    onClickTouch(view, e);
                }
            }
        }

        return false;
    }

    public abstract void onClickTouch(View view, MotionEvent e);

    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return U.pxToDpF(distanceInPx);
    }
}

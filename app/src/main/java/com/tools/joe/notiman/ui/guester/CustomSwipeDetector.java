package com.tools.joe.notiman.ui.guester;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by joe_wang on 2016/8/8.
 */
public class CustomSwipeDetector implements View.OnTouchListener {

    private float downX, downY, upX, upY;

    private GuestureChecker mChecker;

    public CustomSwipeDetector() {
    }

    public CustomSwipeDetector(GuestureChecker checker) {
        mChecker = checker;
    }

    public void setChecker(GuestureChecker checker) {
        mChecker = checker;
    }

    public boolean caughtSwipe() {
        if(mChecker != null
                && mChecker.isCaught(downX, downY, upX, upY)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();

                return false;
            case MotionEvent.ACTION_MOVE:
                upX = event.getX();
                upY = event.getY();

                if(mChecker != null
                        && mChecker.isCaught(downX, downY, upX, upY)) {
                    return true;
                }

                return false;

            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();

                return false;
        }
        return false;
    }
}

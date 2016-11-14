package com.tools.joe.notiman.ui.guester;

/**
 * Created by joe_wang on 2016/8/8.
 */
public interface GuestureChecker {
    boolean isCaught(final float initialX, final float initialY,
                     final float lastX, final float lastY);
}

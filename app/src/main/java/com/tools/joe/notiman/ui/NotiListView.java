package com.tools.joe.notiman.ui;

/**
 * Created by joe_wang on 2016/8/5.
 */
public interface NotiListView {
    void updateListView();
    void showWaiting();
    void dismissWaiting();
    void showSwipeTip(boolean show);
    void showNoNotiTip(boolean show);
    void go2Setting();
}

package com.tools.joe.notiman.ui;

import com.tools.joe.notiman.model.AppInfo;

import java.util.List;

/**
 * Created by joe_wang on 2016/8/1.
 */
public interface ConfigView {
    void refresh();
    void showProgress();
    void hideProgress();
    void selectAll();
    void selectNone();
    void selectReverse();
}

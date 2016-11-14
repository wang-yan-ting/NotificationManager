package com.tools.joe.notiman.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ArrayAdapter;

import com.tools.joe.notiman.R;

/**
 * Created by joe_wang on 2016/8/12.
 */
public class AboutPage extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.about_page;
    }

    @Override
    protected void actionBarConfig(ActionBar ab) {
        ab.setTitle(R.string.about);
        ab.setDisplayHomeAsUpEnabled(true);
    }
}

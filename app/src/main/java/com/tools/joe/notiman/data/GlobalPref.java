package com.tools.joe.notiman.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.tools.joe.notiman.di.qualifier.AppContext;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/11.
 */
public class GlobalPref {
    private SharedPreferences mPref = null;
    private final String KNOW_TIP_SWIPE = "know_tip_swipe";
    private final String FIRST_TIME_SET = "first_time_set";

    public GlobalPref(@AppContext Context context) {
        mPref = context.getSharedPreferences("app_global", 0);
    }

    public void setKnowTipSwipe(boolean known) {
        mPref.edit().putBoolean(KNOW_TIP_SWIPE, known).commit();
    }

    public boolean getKnowTipSwipe() {
        return mPref.getBoolean(KNOW_TIP_SWIPE, false);
    }

    public void setFirstTimeSet(boolean set) {
        mPref.edit().putBoolean(FIRST_TIME_SET, set).commit();
    }

    public boolean getFirstTimeSet() {
        return mPref.getBoolean(FIRST_TIME_SET, true);
    }
}

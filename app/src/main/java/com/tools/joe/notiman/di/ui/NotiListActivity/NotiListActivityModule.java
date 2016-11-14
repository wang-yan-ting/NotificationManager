package com.tools.joe.notiman.di.ui.NotiListActivity;

import android.content.Context;

import com.tools.joe.notiman.di.scope.ActivityScope;
import com.tools.joe.notiman.ui.guester.CustomSwipeDetector;

import dagger.Module;
import dagger.Provides;

/**
 * Created by joe_wang on 2016/8/5.
 */
@Module
public class NotiListActivityModule {
    private Context mContext;

    public NotiListActivityModule(Context context) {
        mContext = context;
    }

    @Provides
    @ActivityScope
    CustomSwipeDetector providesCustomSwipeDetector() {
        return new CustomSwipeDetector();
    }
}


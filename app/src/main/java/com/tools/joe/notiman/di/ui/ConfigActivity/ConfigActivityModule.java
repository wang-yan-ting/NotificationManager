package com.tools.joe.notiman.di.ui.ConfigActivity;

import android.content.Context;

import com.tools.joe.notiman.di.qualifier.ActivityContext;
import com.tools.joe.notiman.di.scope.ActivityScope;
import com.tools.joe.notiman.model.AppInfo;
import com.tools.joe.notiman.ui.ConfigView;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by joe_wang on 2016/8/1.
 */
@Module
public class ConfigActivityModule {
    private Context mContext;

    public ConfigActivityModule(Context ctx) {
        this.mContext = ctx;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    Context provideContext() {
        return mContext;
    }

    @Provides
    @ActivityScope
    ConfigView provideConfigView() {
        return ((ConfigView) mContext);
    }

    @Provides
    @ActivityScope
    List<AppInfo> provideAppInfoList() {
        return new ArrayList<AppInfo>();
    }
}
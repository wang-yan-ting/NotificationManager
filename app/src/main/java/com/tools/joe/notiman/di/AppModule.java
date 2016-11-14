package com.tools.joe.notiman.di;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.tools.joe.notiman.data.AppInMonitorPref;
import com.tools.joe.notiman.data.GlobalPref;
import com.tools.joe.notiman.db.NotiDbHelper;
import com.tools.joe.notiman.di.qualifier.AppContext;
import com.tools.joe.notiman.model.DrawableMap;
import com.tools.joe.notiman.model.InstalledPackageList;
import com.tools.joe.notiman.model.MonitorPackageList;
import com.tools.joe.notiman.model.NotiInfoList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by joe_wang on 2016/8/1.
 */
@Module
public class AppModule {
    Context mContext;

    public AppModule(Context ctx) {
        this.mContext = ctx;
    }

    @Provides
    @Singleton
    @AppContext
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    AppInMonitorPref provideMonitoAppPref() {
        return new AppInMonitorPref(mContext);
    }

    @Provides
    @Singleton
    MonitorPackageList provideMonitorPackageList() {
        return new MonitorPackageList(provideMonitoAppPref());
    }

    @Provides
    @Singleton
    LocalBroadcastManager provideLocalBroadcastManager() {
        return LocalBroadcastManager.getInstance(mContext);
    }

    @Provides
    @Singleton
    NotiInfoList provideNotiInfoList() {
        return new NotiInfoList();
    }

    @Provides
    @Singleton
    NotiDbHelper provideNotiDbHelper() {
        return new NotiDbHelper(mContext);
    }

    @Provides
    @Singleton
    InstalledPackageList provideInstalledPackageList() {
        return new InstalledPackageList();
    }

    @Provides
    @Singleton
    DrawableMap provideDrawableMap() {
        return new DrawableMap();
    }

    @Provides
    @Singleton
    GlobalPref provideGlobalPref() {
        return new GlobalPref(mContext);
    }
}

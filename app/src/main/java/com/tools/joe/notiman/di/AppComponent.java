package com.tools.joe.notiman.di;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.tools.joe.notiman.NotiManApp;
import com.tools.joe.notiman.data.AppInMonitorPref;
import com.tools.joe.notiman.data.GlobalPref;
import com.tools.joe.notiman.db.NotiDbHelper;
import com.tools.joe.notiman.di.qualifier.AppContext;
import com.tools.joe.notiman.model.DrawableMap;
import com.tools.joe.notiman.model.InstalledPackageList;
import com.tools.joe.notiman.model.MonitorPackageList;
import com.tools.joe.notiman.model.NotiInfoList;
import com.tools.joe.notiman.service.NotiManService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by joe_wang on 2016/8/1.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(NotiManApp app);
    void inject(NotiManService service);

    @AppContext
    Context getAppContext();
    AppInMonitorPref getAppInMonitorPref();
    NotiInfoList getNotiInfoList();
    LocalBroadcastManager getLocalBroadcastManager();
    NotiDbHelper getNotiDbHelper();
    MonitorPackageList getMonitorPackageList();
    InstalledPackageList getInstalledPackageList();
    DrawableMap getDrawableMap();
    GlobalPref getGlobalPref();
}

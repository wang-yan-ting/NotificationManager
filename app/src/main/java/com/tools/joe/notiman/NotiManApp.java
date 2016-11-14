package com.tools.joe.notiman;

import android.app.Application;

import com.tools.joe.notiman.di.*;
import com.tools.joe.notiman.model.DrawableMap;
import com.tools.joe.notiman.model.InstalledPackageList;
import com.tools.joe.notiman.model.MonitorPackageList;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/1.
 */
public class NotiManApp extends Application {
    AppComponent mAppComponent;

    @Inject
    MonitorPackageList monitorPackageList;

    @Inject
    DrawableMap mDrawableMap;

    @Inject
    InstalledPackageList mInstalledPackageList;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                        .appModule(new AppModule(this))
                        .build();

        mAppComponent.inject(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                monitorPackageList.retrieve();
                mInstalledPackageList.setDrawableMap(mDrawableMap);
                mInstalledPackageList.forceRetrieve(NotiManApp.this);
            }
        }).start();

    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}

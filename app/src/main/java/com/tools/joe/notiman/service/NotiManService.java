package com.tools.joe.notiman.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.tools.joe.notiman.NotiManApp;
import com.tools.joe.notiman.db.DbUtils;
import com.tools.joe.notiman.di.AppComponent;
import com.tools.joe.notiman.model.Constant;
import com.tools.joe.notiman.model.MonitorPackageList;
import com.tools.joe.notiman.model.NotiInfo;
import com.tools.joe.notiman.model.NotiInfoList;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/4.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotiManService extends NotificationListenerService {
    @Inject
    MonitorPackageList monitorPackageList;

    @Inject
    NotiInfoList mNotiInfoList;

    @Inject
    LocalBroadcastManager mLbm;

    @Inject
    DbUtils mDbUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        AppComponent appComponent = ((NotiManApp) getApplication()).getAppComponent();
        appComponent.inject(this);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        final String packageName = sbn.getPackageName();

        if(!monitorPackageList.includes(packageName)) {
            return;
        }

        if(sbn.isClearable()) {
            final Bundle extras = sbn.getNotification().extras;

            if(extras != null) {
                final String title = extras.getString("android.title");
                final long time = sbn.getPostTime();
                final String content;
                CharSequence contentChar = extras.getCharSequence("android.text");
                if (contentChar == null) {
                    content = "";
                } else {
                    content = contentChar.toString();
                }

                if(!TextUtils.isEmpty(title) || !TextUtils.isEmpty(content)) {

                    NotiInfo notiInfo = new NotiInfo(packageName, title, content, time);

                    // add to interception list
                    mNotiInfoList.add(notiInfo);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mDbUtils.add(packageName, title, content, time);
                        }
                    }).start();

                    // send notification
                    Intent intent = new Intent();
                    intent.setAction(Constant.ACTION_NOTI_INTERCEPT);
                    mLbm.sendBroadcast(intent);
                }
            }

            // cancel notification
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                cancelNotification(sbn.getKey());
            } else {
                cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
            }

        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }


}

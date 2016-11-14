package com.tools.joe.notiman.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * Created by joe_wang on 2016/8/4.
 */
public class NotiUtils {

    /*
     *  check if app is granted read notification permission
     */
    public static boolean hasReadPermission(Context context) {
        String notiStr = Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");
        if (notiStr != null && !TextUtils.isEmpty(notiStr)) {
            final String[] names = notiStr.split(":");
            for (int i = 0; i < names.length; i++) {
                ComponentName cn = ComponentName.unflattenFromString(names[i]);

                if (cn != null) {
                    if(context.getPackageName().equals(cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * go to enable read notification permission
     */
    public static void go2EnablePermission(Context context) {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        context.startActivity(intent);
    }
}

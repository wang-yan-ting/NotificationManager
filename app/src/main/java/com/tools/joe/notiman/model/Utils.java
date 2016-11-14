package com.tools.joe.notiman.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joe_wang on 2016/8/8.
 */
public class Utils {
    private static final long TWO_HOUR = 7200000;

    public static String ts2String(long time) {
        DateFormat format;

        if(System.currentTimeMillis() - time > TWO_HOUR) {
            format = new SimpleDateFormat("MM-dd HH:mm:ss");
        } else {
            format = new SimpleDateFormat("HH:mm:ss");
        }

        return format.format(new Date(time));
    }

    public static void launchAppByPackageName(Context context, String packageName) {
        if(context == null || TextUtils.isEmpty(packageName)) {
            return;
        }

        try {
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            if(intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

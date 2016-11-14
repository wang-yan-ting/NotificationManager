package com.tools.joe.notiman.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tools.joe.notiman.di.qualifier.AppContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/1.
 */
public class AppInMonitorPref {
    private SharedPreferences mPref = null;

    @Inject
    public AppInMonitorPref(@AppContext Context context) {
        mPref = context.getSharedPreferences("app_in_monitor", 0);
    }

    public void addApp(String appName) {
        mPref.edit().putString(appName, "0").commit();
    }

    public void removeApp(String appName) {
        mPref.edit().remove(appName).commit();
    }

    public List<String> getAppList() {
        List<String> list = new ArrayList<String>();

        String name;
        Map<String, ?> allEntries = mPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            name = entry.getKey();
            if(!TextUtils.isEmpty(name)) {
                list.add(name);
            }
        }

        return list;
    }
}

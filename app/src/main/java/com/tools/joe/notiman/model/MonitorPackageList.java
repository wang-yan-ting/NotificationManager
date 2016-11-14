package com.tools.joe.notiman.model;

import android.text.TextUtils;

import com.tools.joe.notiman.data.AppInMonitorPref;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/4.
 */
public class MonitorPackageList {
    private List<String> mList;
    private AppInMonitorPref mPref;

    public MonitorPackageList(AppInMonitorPref pref) {
        mPref = pref;
    }

    public synchronized List<String> retrieve() {
        if(mList != null) {
            return mList;
        } else {
            return forceRetrieve();
        }
    }

    public synchronized List<String> forceRetrieve() {
        mList = mPref.getAppList();
        return mList;
    }

    private synchronized boolean isEmpty() {
        if(mList == null || mList.size() <= 0) {
            return true;
        }

        return false;
    }

    public synchronized void remove(String packageName) {
        if(!isEmpty()) {
            mList.remove(packageName);
        }
    }

    public synchronized void add(String packageName) {
        mList.add(packageName);
    }

    public synchronized boolean includes(final String packageName) {
        if(isEmpty()) {
            return false;
        } else if(TextUtils.isEmpty(packageName)) {
            return false;
        } else {
            return mList.contains(packageName);
        }
    }

    public synchronized void dump() {
        System.out.println("MonitorPackageList:"+this.toString());
        if(isEmpty()) {
            System.out.println(" monitor package list NULL!");
            return;
        }

        int len = mList.size();
        System.out.println("size = " + len);
        for(int i = 0; i < len; i++) {
            System.out.println(mList.get(i));
        }
    }

}

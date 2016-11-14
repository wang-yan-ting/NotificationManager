package com.tools.joe.notiman.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/2.
 */
public class InstalledPackageList {

    private final List<AppInfo> mList = new ArrayList<>();
    private DrawableMap mDrawableMap;
    private ReentrantLock mLock = new ReentrantLock();
    private Condition mCondition = mLock.newCondition();
    private volatile boolean mInited = false;

    public InstalledPackageList() {
        mInited = false;
    }

    public void setDrawableMap(DrawableMap drawableMap) {
        mDrawableMap = drawableMap;
    }

    public final synchronized List<AppInfo> forceRetrieve(Context context) {
        mLock.lock();

        try {
            mInited = false;

            mList.clear();

            final PackageManager pm = context.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            for (ApplicationInfo pi : packages) {
                AppInfo ai = new AppInfo();
                ai.packageName = pi.packageName;
                ai.simpleName = pm.getApplicationLabel(pi).toString();
                if((pi.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    ai.system = true;
                } else {
                    ai.system = false;
                }
                mList.add(ai);

                Drawable icon;
                try {
                    icon = pm.getApplicationIcon(pi.packageName);
                } catch (PackageManager.NameNotFoundException e) {
                    icon = null;
                }
                mDrawableMap.add(pi.packageName, icon);
            }

            mInited = true;
            mCondition.signal();
        } finally {
            mLock.unlock();
        }

        return mList;
    }

    public final void waitInitDone() {
        mLock.lock();
        try {
            while(!mInited) {
                try {
                    mCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            mLock.unlock();
        }
    }

    public final synchronized List<AppInfo> retrieve(Context context) {
        if(isEmpty()) {
            return forceRetrieve(context);
        } else {
            return mList;
        }
    }

    private final synchronized boolean isEmpty() {
        if(mList == null || mList.size() <= 0) {
            return true;
        }

        return false;
    }


}

package com.tools.joe.notiman.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.tools.joe.notiman.data.AppInMonitorPref;
import com.tools.joe.notiman.model.AppInfo;
import com.tools.joe.notiman.model.Constant;
import com.tools.joe.notiman.model.MonitorPackageList;
import com.tools.joe.notiman.model.InstalledPackageList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/1.
 */
public class ConfigPresenter {

    Context mContext;
    AppInMonitorPref mPref;
    ConfigView mConfigView;
    List<AppInfo> mAppInfoList;
    MonitorPackageList mMonitorList;
    InstalledPackageList mInstalledPackageList;

    private boolean mNeedSave = false;


    @Inject
    public ConfigPresenter(AppInMonitorPref pref, List<AppInfo> infoList,
                           MonitorPackageList monitorList,
                           InstalledPackageList installedPackageList) {
        mPref = pref;
        mAppInfoList = infoList;
        mMonitorList = monitorList;
        mInstalledPackageList = installedPackageList;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setView(ConfigView configView) {
        mConfigView = configView;
    }

    public List<AppInfo> getAppInfoList() {
        return mAppInfoList;
    }

    public void addMonitorApp(final String packageName, final int pos) {
        mMonitorList.add(packageName);
        mAppInfoList.get(pos).select = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPref.addApp(packageName);

            }
        }).start();
    }

    public void deleteMonitorApp(final String packageName, final int pos) {
        mMonitorList.remove(packageName);
        mAppInfoList.get(pos).select = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPref.removeApp(packageName);
            }
        }).start();
    }

    public void getInstalledPackage(final int type) {

        AsyncTask<Void,Void,List<AppInfo>> task =
                new AsyncTask<Void,Void,List<AppInfo>>() {

            @Override
            protected List<AppInfo> doInBackground(Void... params) {
                List<AppInfo> appList = mInstalledPackageList.retrieve(mContext);
                List<AppInfo> list = new ArrayList<AppInfo>();

                if(type == Constant.SP_ITEM_ALL) {
                    list = appList;
                } else {
                    if (type == Constant.SP_ITEM_DOWN) {
                        for(AppInfo ai : appList) {
                            if(!ai.system) {
                                list.add(ai);
                            }
                        }
                    } else {
                        for(AppInfo ai : appList) {
                            if(ai.system) {
                                list.add(ai);
                            }
                        }
                    }
                }

                List<String> monitorList = mMonitorList.retrieve();
                if(monitorList != null && monitorList.size() > 0) {
                    for(AppInfo ai : list) {
                        if(!TextUtils.isEmpty(ai.packageName)) {
                            ai.select = monitorList.contains(ai.packageName);
                        }
                    }
                }

                return list;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(mConfigView != null) mConfigView.showProgress();
            }

            @Override
            protected void onPostExecute(List<AppInfo> appList) {
                super.onPostExecute(appList);

                if(appList != null && appList.size() > 0) {
                    mAppInfoList.clear();
                    if(mConfigView != null) {
                        mConfigView.refresh();
                    }

                    mAppInfoList.addAll(appList);

                    if(mConfigView != null) {
                        mConfigView.refresh();
                        mConfigView.hideProgress();
                    }
                }
            }
        };

        task.execute(null, null, null);
    }

    public void waitPackageListInitDone() {
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                mInstalledPackageList.waitInitDone();
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                if(mConfigView != null) {
                    mConfigView.refresh();
                }
            }
        }.execute(null, null, null);
    }

    public void selectAll() {
        for(AppInfo ai : mAppInfoList) {
            ai.select = true;
            if(!mMonitorList.includes(ai.packageName)) {
                mMonitorList.add(ai.packageName);
                mNeedSave = true;
            }
        }

        mConfigView.selectAll();
    }

    public void selectNone() {
        for(AppInfo ai : mAppInfoList) {
            ai.select = false;
            if(mMonitorList.includes(ai.packageName)) {
                mMonitorList.remove(ai.packageName);
                mNeedSave = true;
            }
        }

        mConfigView.selectNone();
    }

    public void selectReverse() {
        for(AppInfo ai : mAppInfoList) {
            ai.select = !ai.select;
            if(ai.select) {
                mMonitorList.add(ai.packageName);
            } else {
                mMonitorList.remove(ai.packageName);
            }
        }

        mNeedSave = true;
        mConfigView.selectReverse();
    }

    public void saveChanges() {
        if(!mNeedSave) {
            return;
        }
        mNeedSave = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                String pname;
                List<String> savedList = mPref.getAppList();
                for(AppInfo ai : mAppInfoList) {
                    pname = ai.packageName;
                    if(ai.select && !savedList.contains(pname)) {
                        mPref.addApp(pname);
                    } else if(!ai.select && savedList.contains(pname)) {
                        mPref.removeApp(pname);
                    }
                }
            }
        }).start();
    }
}

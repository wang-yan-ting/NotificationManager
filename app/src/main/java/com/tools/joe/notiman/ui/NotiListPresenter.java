package com.tools.joe.notiman.ui;

import android.content.Context;
import android.os.AsyncTask;

import com.tools.joe.notiman.data.GlobalPref;
import com.tools.joe.notiman.db.DbUtils;
import com.tools.joe.notiman.model.InstalledPackageList;
import com.tools.joe.notiman.model.NotiInfo;
import com.tools.joe.notiman.model.NotiInfoList;
import com.tools.joe.notiman.model.NotiUtils;
import com.tools.joe.notiman.model.Utils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/1.
 */
public class NotiListPresenter {
    NotiListView mNotiListView;
    Context mContext;
    List<NotiInfo> mList;
    NotiInfoList mNotiList;
    DbUtils mDbUtils;
    InstalledPackageList mInstalledPackageList;
    GlobalPref mGlobalPref;

    @Inject
    public NotiListPresenter(NotiInfoList list, DbUtils dbUtils,
                             InstalledPackageList packageList) {
        mNotiList = list;
        mDbUtils = dbUtils;
        mInstalledPackageList = packageList;
    }

    public void setView(NotiListView v) {
        mNotiListView = v;
    }

    public void setContext(Context ctx) {
        mContext = ctx;
    }

    public void setGlobalPref(GlobalPref pref) {
        mGlobalPref = pref;
    }

    public void markSwipeTipKnown() {
        mGlobalPref.setKnowTipSwipe(true);
        mNotiListView.showSwipeTip(false);
        mNotiListView.showNoNotiTip(mNotiList.isEmpty());
    }

    public void onNewNotiReceived() {
        getNotiList(true);

        if(mNotiListView != null) {
            mNotiListView.updateListView();
            mNotiListView.showNoNotiTip(mNotiList.isEmpty() && mGlobalPref.getKnowTipSwipe());
        }
    }

    public List<NotiInfo> getNotiList(boolean update) {
        if(update) {
            mList = mNotiList.retrieve();
        }

        return mList;
    }

    public void remove(final NotiInfo notiInfo) {
        mNotiList.delete(notiInfo);
        getNotiList(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDbUtils.delete(notiInfo.packageName, notiInfo.time);
            }
        }).start();

        if(mNotiListView != null) {
            mNotiListView.updateListView();
            mNotiListView.showNoNotiTip(mNotiList.isEmpty() && mGlobalPref.getKnowTipSwipe());
        }
    }

    public void removeAll() {
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                mDbUtils.deleteAll();
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mNotiListView.showWaiting();
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);

                mNotiList.deleteAll();
                getNotiList(false);

                if(mNotiListView != null) {
                    mNotiListView.updateListView();
                    mNotiListView.showNoNotiTip(mNotiList.isEmpty() && mGlobalPref.getKnowTipSwipe());
                }

                mNotiListView.dismissWaiting();
            }
        }.execute(null, null, null);
    }

    public void clickItem(final NotiInfo notiInfo) {
        Utils.launchAppByPackageName(mContext, notiInfo.packageName);
        remove(notiInfo);
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
                if(mNotiListView != null) {
                    mNotiListView.updateListView();
                    mNotiListView.showNoNotiTip(mNotiList.isEmpty() && mGlobalPref.getKnowTipSwipe());
                }
            }
        }.execute(null, null, null);
    }

    public void start() {

        mNotiListView.showSwipeTip(!mGlobalPref.getKnowTipSwipe());

        if(mGlobalPref.getFirstTimeSet()) {
            mGlobalPref.setFirstTimeSet(false);
            mNotiListView.go2Setting();
        } else {
            AsyncTask<Void, Void, List<NotiInfo>> task =
                    new AsyncTask<Void, Void, List<NotiInfo>>() {

                        @Override
                        protected List<NotiInfo> doInBackground(Void... params) {
                            mNotiListView.showWaiting();
                            return mDbUtils.getAll();
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected void onPostExecute(List<NotiInfo> notiList) {
                            super.onPostExecute(notiList);
                            mNotiList.allAll(notiList);
                            getNotiList(false);
                            if (mNotiListView != null) {
                                mNotiListView.updateListView();
                                mNotiListView.showNoNotiTip(mNotiList.isEmpty() && mGlobalPref.getKnowTipSwipe());
                            }
                            mNotiListView.dismissWaiting();
                        }
                    };
            task.execute(null, null, null);
        }
    }

    public void go2OpenNotiPerm() {
        NotiUtils.go2EnablePermission(mContext);
    }

    public boolean hasNotiPerm() {
        return NotiUtils.hasReadPermission(mContext);
    }
}

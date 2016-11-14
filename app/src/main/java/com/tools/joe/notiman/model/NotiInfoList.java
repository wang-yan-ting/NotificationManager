package com.tools.joe.notiman.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe_wang on 2016/8/5.
 */
public class NotiInfoList {
    private List<NotiInfo> mList = new ArrayList<>();

    public synchronized void add(NotiInfo notiInfo) {
        if(mList == null) {
            return;
        }

        mList.add(0, notiInfo);
    }

    public synchronized  void allAll(List<NotiInfo> list) {
        mList.clear();
        mList.addAll(list);
    }

    public synchronized void delete(NotiInfo notiInfo) {
        if(isEmpty()) {
            return;
        }
        mList.remove(notiInfo);
    }

    public synchronized void deleteAll() {
        if(isEmpty()) {
            return;
        }

        mList.clear();
    }

    public synchronized boolean isEmpty() {
        if(mList == null || mList.size() <= 0) {
            return true;
        }

        return false;
    }

    public synchronized List<NotiInfo> retrieve() {
        return mList;
    }

    public synchronized void dump() {
        System.out.println("NotiInfoList Dump:");

        if(isEmpty()) {
            System.out.println("empty list!");
        }

        int len = mList.size();
        for(int i = 0; i < len; i++) {
            System.out.println("["+i+"] -> " + mList.get(i).toString());
        }
    }
}

package com.tools.joe.notiman.ui.list;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by joe_wang on 2016/8/2.
 */
public abstract class ListAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    protected Context mContext;

    public ListAdapter(Context context, List<T> list) {
        mContext = context;
        mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

package com.tools.joe.notiman.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by joe_wang on 2016/8/9.
 */
public class DrawableMap {
    private Map<String, Drawable> mMap = new HashMap<String, Drawable>();

    public DrawableMap() {}

    public synchronized Drawable get(String packageName) {
        return mMap.get(packageName);
    }

    public synchronized void add(String packagName, Drawable drawable) {
        mMap.put(packagName, drawable);
    }
}

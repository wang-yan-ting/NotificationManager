package com.tools.joe.notiman.model;

import android.graphics.drawable.Drawable;

/**
 * Created by joe_wang on 2016/8/4.
 */
public class NotiInfo {
    public String packageName;
    public String title;
    public String content;
    public long time;

    public NotiInfo() {}

    public NotiInfo(String packageName,
                    String title, String content, long time) {
        this.packageName = packageName;
        this.title   = title;
        this.content = content;
        this.time    = time;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("package:").append(packageName).append("  ");
        sb.append("title:").append(title).append("  ");
        sb.append("content:").append(content).append("  ");
        sb.append("time:").append(time);

        return sb.toString();
    }
}

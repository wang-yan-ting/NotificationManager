package com.tools.joe.notiman.model;

/**
 * Created by joe_wang on 2016/8/2.
 */
public class AppInfo {
    public String packageName;
    public String simpleName;
    public boolean system;
    public boolean select;

    public AppInfo() {
        select = false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("package:").append(packageName).append("\n")
                .append("appName:").append(simpleName).append("\n")
                .append("system:").append(system).append("\n")
                .append("selected:").append(select);

        return sb.toString();
    }
}

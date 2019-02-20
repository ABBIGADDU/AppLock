package com.example.user1.applock.project.model;

import android.graphics.drawable.Drawable;

public class AppInfoModel {

    private String appName;
    private String pckgName;
    private boolean isLocked;
    private Drawable appIcon;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPckgName() {
        return pckgName;
    }

    public void setPckgName(String pckgName) {
        this.pckgName = pckgName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}

package com.example.user1.applock.project.helper;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class ProcessList {
    // process package name
    public static final String COLUMN_PROCESS_NAME = "process";

    // TODO: arbitrary property (can be user-fiendly name)
    public static final String COLUMN_PROCESS_PROP = "property";

    // number of times a process has been activated
    public static final String COLUMN_PROCESS_COUNT = "count";

    // number of seconds a process was in foreground
    public static final String COLUMN_PROCESS_TIME = "time";

    private ContextWrapper context;

    public ProcessList(ContextWrapper context) {
        this.context = context;
    }

    protected abstract boolean isFilteredByName(String pack);

    public void fillProcessList(ArrayList<HashMap<String, Object>> processList, ArrayList<String> packages) {
        String currentApp ="";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        Log.e("currentApp", currentApp + "check");

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfo = activityManager.getRunningAppProcesses();

        HashMap<String, Object> hm;
        final PackageManager pm = context.getApplicationContext().getPackageManager();

        for (int i = 0; i < procInfo.size(); i++) {
            String process = procInfo.get(i).processName;
            String packageList = Arrays.toString(procInfo.get(i).pkgList);
            if (!packageList.contains(process)) {
                process = procInfo.get(i).pkgList[0];
            }

            if (!packages.contains(process) && !isFilteredByName(process)) {
                ApplicationInfo ai;
                String applicationName = "";

                for (int k = 0; k < procInfo.get(i).pkgList.length; k++) {
                    String thisPackage = procInfo.get(i).pkgList[k];
                    try {
                        ai = pm.getApplicationInfo(thisPackage, 0);
                    } catch (final PackageManager.NameNotFoundException e) {
                        ai = null;
                    }
                    if (k > 0) applicationName += " / ";
                    applicationName += (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
                }

                packages.add(process);
                hm = new HashMap<String, Object>();
                hm.put(COLUMN_PROCESS_NAME, process);
                hm.put(COLUMN_PROCESS_PROP, applicationName);
                processList.add(hm);
            }
        }

        // optional sorting
        Comparator<HashMap<String, Object>> comparator = new Comparator<HashMap<String, Object>>() {
            public int compare(HashMap<String, Object> object1, HashMap<String, Object> object2) {
                return ((String) object1.get(COLUMN_PROCESS_NAME)).compareToIgnoreCase((String) object2.get(COLUMN_PROCESS_NAME));
            }
        };
        Collections.sort(processList, comparator);

        packages.clear();
        for (HashMap<String, Object> e : processList) {
            packages.add((String) e.get(COLUMN_PROCESS_NAME));
        }
    }

}

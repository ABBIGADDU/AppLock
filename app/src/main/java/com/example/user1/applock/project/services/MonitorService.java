package com.example.user1.applock.project.services;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import com.example.user1.applock.project.activities.FirstActivity;
import com.example.user1.applock.project.application.AppLockApplication;
import com.example.user1.applock.project.helper.ProcessList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static com.example.user1.applock.project.helper.ProcessList.COLUMN_PROCESS_COUNT;
import static com.example.user1.applock.project.helper.ProcessList.COLUMN_PROCESS_TIME;

public class MonitorService extends Service {
    private boolean initialized = false;
    private final IBinder mBinder = new LocalBinder();
    private ServiceCallback callback = null;
    private Timer timer = null;
    private final Handler mHandler = new Handler();
    private String foreground = null;
    private ArrayList<HashMap<String, Object>> processList;
    private ArrayList<String> packages;
    private Date split = null;

    public static int SERVICE_PERIOD = 5000; // TODO: customize (this is for scan every 5 seconds)

    private final ProcessList pl = new ProcessList(this) {
        @Override
        protected boolean isFilteredByName(String pack) {
            // TODO: filter processes by names, return true to skip the process
            // always return false (by default) to monitor all processes
            return false;
        }

    };

    public interface ServiceCallback {
        void sendResults(int resultCode, Bundle b);
    }

    public class LocalBinder extends Binder {
        public MonitorService getService() {
            // Return this instance of the service so clients can call public methods
            return MonitorService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialized = true;
        processList = ((AppLockApplication) getApplication()).getProcessList();
        packages = ((AppLockApplication) getApplication()).getPackages();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (initialized) {
            return mBinder;
        }
        return null;
    }

    public void setCallback(ServiceCallback callback) {
        this.callback = callback;
    }

    private boolean addToStatistics(String target) {
        boolean changed = false;
        Date now = new Date();
        if (!TextUtils.isEmpty(target)) {
            if (!target.equals(foreground)) {
                int i;
                if (foreground != null && split != null) {
                    // TODO: calculate time difference from current moment
                    // to the moment when previous foreground process was activated
                    i = packages.indexOf(foreground);
                    long delta = (now.getTime() - split.getTime()) / 1000;
                    Long time = (Long) processList.get(i).get(COLUMN_PROCESS_TIME);
                    if (time != null) {
                        // TODO: add the delta to statistics of 'foreground'
                        time += delta;
                    } else {
                        time = new Long(delta);
                    }
                    processList.get(i).put(COLUMN_PROCESS_TIME, time);
                }

                // update count of process activation for new 'target'
                i = packages.indexOf(target);
                if(i >0) {
                    Integer count = (Integer) processList.get(i).get(COLUMN_PROCESS_COUNT);
                    if (count != null) count++;
                    else {
                        count = new Integer(1);
                    }
                    processList.get(i).put(COLUMN_PROCESS_COUNT, count);

                    foreground = target;
                    split = now;
                    changed = true;
                }
            }
        }
        return changed;
    }


    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new MonitoringTimerTask(), 500, SERVICE_PERIOD);
        }

        // TODO: startForeground(srvcid, createNotification(null));
    }

    public void stop() {
        timer.cancel();
        timer.purge();
        timer = null;
    }

    private class MonitoringTimerTask extends TimerTask {
        @Override
        public void run() {
            if(currentRunningApp().equalsIgnoreCase("com.pyarinc.pyardev")){
                startActivity(new Intent(MonitorService.this, FirstActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    private String currentRunningApp(){
        String currentApp ="Null";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager)MonitorService.this.getSystemService(Context.USAGE_STATS_SERVICE);
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
            ActivityManager am = (ActivityManager)MonitorService.this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        return currentApp;
    }
    private void fillProcessList() {
        pl.fillProcessList(processList, packages);
    }

}

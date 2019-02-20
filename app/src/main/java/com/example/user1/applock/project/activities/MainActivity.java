package com.example.user1.applock.project.activities;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.user1.applock.AppInfoAdapter;
import com.example.user1.applock.R;
import com.example.user1.applock.patternlockView.PatternLockView;
import com.example.user1.applock.project.DbHelper.SessionManager;
import com.example.user1.applock.project.DbHelper.Singleton;
import com.example.user1.applock.project.model.AppInfoModel;
import com.example.user1.applock.project.services.MonitorService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MonitorService.ServiceCallback {

    private Context context;
    private RecyclerView rvAppsInfo;
    private MonitorService backgroundService;
    PackageManager packageManager;
    AppInfoAdapter appInfoAdapter;
    private List<AppInfoModel> lstAdvanced = new ArrayList<>();
    private List<PackageInfo> lstSwitchlock = new ArrayList<>();
    private List<PackageInfo> lstGenerallock = new ArrayList<>();
    private List<String> lstSelectedPckgs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    private void initializeViews() {
        context = this;
        rvAppsInfo = findViewById(R.id.rv_apps_list);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        packageManager = getPackageManager();
        List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        SessionManager sessionManager = new SessionManager(context);
//        sessionManager.setMemberLockPattern(Singleton.getInstance().getLstFirstSavingDots().toString());
        for (PackageInfo pi : packageList) {
            if (!isSystemPackage(pi)) {
                try {
                    AppInfoModel model = new AppInfoModel();
                    model.setPckgName(pi.packageName);
                    model.setAppIcon(context.getPackageManager().getApplicationIcon(model.getPckgName()));
                    model.setAppName(packageManager.getApplicationLabel(
                            pi.applicationInfo).toString());
                    model.setLocked(false);
                    lstAdvanced.add(model);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        rvAppsInfo.setLayoutManager(new LinearLayoutManager(context));
        appInfoAdapter = new AppInfoAdapter(context, lstAdvanced, new AppInfoAdapter.onItemClickListener() {
            @Override
            public void itemSelected(int position) {
                if (!isAccessGranted()) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);
                } else {
                    lstAdvanced.get(position).setLocked(true);
                    appInfoAdapter.notifyDataSetChanged();
                }
            }
        });
        rvAppsInfo.setAdapter(appInfoAdapter);

        bindService(
                new Intent(MainActivity.this, MonitorService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MonitorService.LocalBinder binder = (MonitorService.LocalBinder) service;
            backgroundService = binder.getService();
            backgroundService.setCallback(MainActivity.this);
            backgroundService.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            backgroundService = null;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (backgroundService != null) {
            backgroundService.setCallback(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
/*
        if (backgroundService != null) {
            backgroundService.setCallback(null);
        }
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundService != null)
            unbindService(serviceConnection);
    }

    @Override
    public void sendResults(int resultCode, Bundle b) {
        Log.e("bundle", b + "check");
    }
}

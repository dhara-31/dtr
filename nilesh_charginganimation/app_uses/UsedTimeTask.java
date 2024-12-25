package com.si_charginganimation.nilesh_charginganimation.app_uses;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.util.Log;


import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.model.AppUsageAU;
import com.si_charginganimation.nilesh_charginganimation.model.TimeUsedAU;
import com.si_charginganimation.nilesh_charginganimation.model.UtilsAU;

import java.util.ArrayList;
import java.util.List;

public class UsedTimeTask extends AsyncTask<Void, Void, ArrayList<TimeUsedAU>> {
    private static final String TAG = null;
    private Context context;
    private OnLoad onLoad;
    private UsageDaily usageDaily;


    public interface OnLoad {
        void onLoadFinish(ArrayList<TimeUsedAU> arrayList);
    }

    public UsedTimeTask(UsageDaily usageDaily, Context context, OnLoad onLoad) {
        this.usageDaily = usageDaily;
        this.context = context;
        this.onLoad = onLoad;
    }

    public ArrayList<TimeUsedAU> doInBackground(Void... voidArr) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        List<ResolveInfo> resolveInfoList = context.getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);


        ArrayList<TimeUsedAU> arrayList = new ArrayList<>();
        ArrayList<UsageStats> usageList = UsageDailyTask.getUsageList();
        for (int i = 0; i < usageList.size(); i++) {
            boolean logo = false;
            TimeUsedAU TimeUsedAU = new TimeUsedAU();
            AppUsageAU appUsage = this.usageDaily.getAppUsages().get(usageList.get(i).getPackageName());
            if (appUsage != null && appUsage.getTotalTime() > 0) {
                TimeUsedAU.setAppName(UtilsAU.getAppName(usageList.get(i).getPackageName()));
                TimeUsedAU.setPackageName(usageList.get(i).getPackageName());
                TimeUsedAU.setTotalTime(appUsage.getTotalTime() / 1000);

                for (ResolveInfo resolveInfo : resolveInfoList) {
                    ActivityInfo activityInfo = resolveInfo.activityInfo;
                    if(!isSystemPackage(resolveInfo)) {
                     }
                    if (activityInfo.applicationInfo.packageName.equals(usageList.get(i).getPackageName())) {
                        TimeUsedAU.setIconApp(activityInfo.loadIcon(context.getPackageManager()));
                        logo = true;
                        break;
                    }
                }
                if (!logo) {

                    try {
                        TimeUsedAU.setIconApp(this.context.getPackageManager().getApplicationIcon(usageList.get(i).getPackageName()));
                    } catch (PackageManager.NameNotFoundException e) {
                        TimeUsedAU.setIconApp(this.context.getResources().getDrawable(R.drawable.ic_launcher_background));
                        e.printStackTrace();
                    }
                }
                arrayList.add(TimeUsedAU);
            }
        }
         return arrayList;
    }

    public static   int lambda$doInBackground$0(TimeUsedAU TimeUsedAU, TimeUsedAU TimeUsedAU2) {
        return (int) (TimeUsedAU2.getTotalTime() - TimeUsedAU.getTotalTime());
    }

    public void onPostExecute(ArrayList<TimeUsedAU> arrayList) {
        super.onPostExecute(arrayList);
        OnLoad onLoad = this.onLoad;
        if (onLoad != null) {
            onLoad.onLoadFinish(arrayList);
        }
    }
    public boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}

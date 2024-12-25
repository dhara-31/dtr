package com.si_charginganimation.nilesh_charginganimation.service;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.magicfluids.FActivity;
import com.magicfluids.MainActivity;
import com.si_charginganimation.nilesh_charginganimation.R;

import com.si_charginganimation.nilesh_charginganimation.act.MainAct;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActFullWarning;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowAnim;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowClassicTheme;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowCustomAnim;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowGame;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowNewTheme;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowOwlTheme;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowPopularTheme;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowPremiumTheme;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActTempWarning;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


public class ChargingCAService extends Service {
    ShCAPreference shCAPreference;
    public static String level;
    boolean showLow = true;
    BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();

            if (action.equals(Intent.ACTION_POWER_CONNECTED)) {


                startScreen(intent);


            } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                ActShowAnim.closed();
                ActShowCustomAnim.closed();
                ActShowPremiumTheme.closed();
                ActShowClassicTheme.closed();
                ActShowPopularTheme.closed();
                ActShowOwlTheme.closed();
                ActShowGame.closed();
                ActFullWarning.closed();
                ActTempWarning.closed();
                ActShowNewTheme.closed();
                showLow = true;
            }
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                level = updateView(new BatteryInfo(intent));

                ActShowAnim.setPer(intent);
                ActShowCustomAnim.setPer(intent);
                ActShowPremiumTheme.setPer(intent);
                ActShowClassicTheme.setPer(intent);
                ActShowPopularTheme.setPer(intent);
                ActShowNewTheme.setPer(intent);
                ActShowOwlTheme.setPer(intent);

                ActShowGame.setPer(intent);


                openAlarm(new BatteryInfo(intent));

            }
        }
    };

    private void openAlarm(BatteryInfo batteryInfo) {
        int bl = batteryInfo.getLevel();
        float temp = (batteryInfo.getTemperature() / 10.0f);

        if (shCAPreference.getFullAlarm_ca()) {
            if (batteryInfo.getPlugged() != 0) {
                if (bl == shCAPreference.getFullAlarmLevel_ca()) {

                    Intent intent2 = new Intent(getApplicationContext(), ActFullWarning.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.putExtra("type", "full");
                    startActivity(intent2);

                }
            }
        }
        if (shCAPreference.getLowAlarm_ca()) {
            if (bl == shCAPreference.getLowAlarmLevel_ca()) {
                if (showLow) {
                    showLow = false;
                    Intent intent2 = new Intent(getApplicationContext(), ActFullWarning.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.putExtra("type", "low");
                    startActivity(intent2);
                }

            }
        }
        if (shCAPreference.getTempAlarm_ca()) {
            if (Float.compare(temp, shCAPreference.getTempAlarmLevel_ca()) == 0) {
                Intent intent2 = new Intent(getApplicationContext(), ActTempWarning.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
            }
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        shCAPreference = new ShCAPreference(getApplicationContext());

        startServiceWithNotification();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {


            startServiceWithNotification();

            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(Intent.ACTION_POWER_CONNECTED);
            filter1.addAction(Intent.ACTION_POWER_DISCONNECTED);
            filter1.addAction(Intent.ACTION_BATTERY_CHANGED);

            registerReceiver(myBroadcastReceiver, filter1);

        } else {
            stopMyService();
        }
        return START_STICKY;

    }


    @Override
    public void onDestroy() {

        shCAPreference.setServiceOnOff(false);
        try {
            this.unregisterReceiver(myBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void startServiceWithNotification() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            String NOTIFICATION_CHANNEL_ID = getPackageName();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            notificationBuilder.setAutoCancel(false)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(getResources().getString(R.string.app_name) + " Service Running");

            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = getPackageName();
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);


        Intent notificationIntent = new Intent(getApplicationContext(), MainAct.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(getResources().getString(R.string.app_name) + " Service Running")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(intent)
                .build();

        startForeground(2, notification);
    }

    void stopMyService() {

        stopForeground(true);
        stopSelf();


    }

    public static String updateView(BatteryInfo batteryInfo) {


        return getLevel(batteryInfo);


    }

    private static String getLevel(BatteryInfo batteryInfo) {
        return String.valueOf(batteryInfo.getLevel());
    }

    private void startScreen(Intent intent) {


        if (shCAPreference.getShowLockScreen()) {
            if (isPhoneIsLockedOrNot(getApplicationContext())) {

                openAct(intent);

            }
        } else {
            openAct(intent);
        }


    }

    private void openAct(Intent intent) {
        level = updateView(new BatteryInfo(intent));

        if (shCAPreference.getType_ca().equals("cs_anim")) {
            Intent intent2 = new Intent(getApplicationContext(), ActShowCustomAnim.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        } else if (shCAPreference.getType_ca().equals("ptTheme")) {
            Intent intent2 = new Intent(getApplicationContext(), ActShowPremiumTheme.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        } else if (shCAPreference.getType_ca().equals("ctTheme")) {
            Intent intent2 = new Intent(getApplicationContext(), ActShowClassicTheme.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        } else if (shCAPreference.getType_ca().equals("atTheme")) {
            Intent intent2 = new Intent(getApplicationContext(), ActShowPopularTheme.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        } else if (shCAPreference.getType_ca().equals("newTheme")) {
            Intent intent2 = new Intent(getApplicationContext(), ActShowNewTheme.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        } else if (shCAPreference.getType_ca().equals("owl_theme")) {
            Intent intent2 = new Intent(getApplicationContext(), ActShowOwlTheme.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        }
        else if (shCAPreference.getType_ca().equals("flud")) {
            Intent intent2 = new Intent(getApplicationContext(), FActivity.class);
            intent2.putExtra("v",shCAPreference.getfuid());
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        }

        else if (shCAPreference.getType_ca().equals("Game")) {
            Intent intent2 = new Intent(getApplicationContext(), ActShowGame.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        }


        else {
            Intent intent2 = new Intent(getApplicationContext(), ActShowAnim.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        }
        ActShowAnim.setPer(intent);

        ActShowCustomAnim.setPer(intent);
        ActShowPremiumTheme.setPer(intent);
        ActShowClassicTheme.setPer(intent);
        ActShowPopularTheme.setPer(intent);
        ActShowNewTheme.setPer(intent);
        ActShowOwlTheme.setPer(intent);

        ActShowGame.setPer(intent);
    }

    private boolean isPhoneIsLockedOrNot(Context context) {
        boolean isPhoneLock = false;
        if (context != null) {
            KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            if (myKM != null && myKM.isKeyguardLocked()) {
                isPhoneLock = true;
            }
        }
        return isPhoneLock;
    }


}

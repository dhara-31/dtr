package com.si_charginganimation.nilesh_charginganimation.act;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.AnimHorVideoCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.app_uses.UsageDaily;
import com.si_charginganimation.nilesh_charginganimation.app_uses.UsageDailyTask;
import com.si_charginganimation.nilesh_charginganimation.app_uses.UsedTimeTask;
import com.si_charginganimation.nilesh_charginganimation.bluetooth.BLEConnectHelper;
import com.si_charginganimation.nilesh_charginganimation.bluetooth.BLEInFo;
import com.si_charginganimation.nilesh_charginganimation.bluetooth.BluetoothConnectedReceiver;
import com.si_charginganimation.nilesh_charginganimation.AnimCAApi.FirstCAApi;
import com.si_charginganimation.nilesh_charginganimation.AnimCAApi.FristCAAPIInterface;
import com.si_charginganimation.nilesh_charginganimation.AnimCAApi.FristCAAPIClient;
import com.si_charginganimation.nilesh_charginganimation.AnimCAApi1.APIInterface;
import com.si_charginganimation.nilesh_charginganimation.AnimCAApi1.CAAPIClient;

import com.si_charginganimation.nilesh_charginganimation.AnimCAApi1.CADatum;
import com.si_charginganimation.nilesh_charginganimation.AnimCAApi1.CAExample;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActivityMainBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.model.TimeUsedAU;
import com.si_charginganimation.nilesh_charginganimation.model.UtilsAU;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DBettryplication;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.michaelrocks.paranoid.Obfuscate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION.SDK_INT;


@Obfuscate
public class MainAct extends Activity implements BLEConnectHelper.BluetoothDataCallback {
    private int REQUEST_CODE = 20001;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    private ArrayList<TimeUsedAU> timeUsedArrayList = new ArrayList<>();
    public static ActivityMainBinding b;
    String pluggedTv;
    SharedPreferences sharedPreference;
    String strChargeVia;
    String technologyTv;
    String tempTv;
    double temps;
    String theftAlarmStatus = "off";
    String totalCapacity;
    String voltageTv;
    String whenNotifyUser = "";
    String whichCheck;
    String ChargingLevel;
    String FullAlarmStatus;
    String Status;
    String batteryPctTv;
    String capacityTv;
    String chargingStatusTv;
    Context context;
    String healthTv = "";
    ShCAPreference shCAPreference;
    private int REQ_USAGE = 9090;
    private int themeColor;
    private APIInterface apiInterface;
    private FristCAAPIInterface fristAPIInterface;
    private AlertDialog.Builder builder;
    AlertDialog create;
    private AlertDialog.Builder dowBuilder;
    AlertDialog dowCreate;
    private TextView tvDowPer;
    private AnimHorVideoCAAdapter animHorVideoCAAdapter;
    public String filePath_low;
    File videoPath = null;

    String filePath;

    public static MainAct mainAct;
    private static final int PT_RC = 101;
    private static final int CT_RC = 102;
    private static final int AT_RC = 103;

    private static final int ANIM_RC = 104;
    private static final int ANIMP_RC = 107;
    private static final int GALLERY_RC = 105;
    private static final int BA_RC = 106;
    private static final int ALARM_RC = 109;
    public static List<CADatum> videoList = new ArrayList<>();
    private ConstraintLayout cvLBG;

    private static ArrayList<BLEInFo> bleInFoArrayList = new ArrayList<>();
    private static BluetoothConnectedReceiver bluetoothConnectedReceiver;
    private static BLEConnectHelper mBLEConnectHelper;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1300;


    public static String packagename = "com.batteryanimation.charginganimation";
    public static String category = "abstract";
    public static String username = "batteryanimation";
    public static String password = "batteryanimation.si";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        mainAct = this;

        b.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(new Intent(MainAct.this, ListActivity.class));
                    }
                });


            }
        });
        shCAPreference = new ShCAPreference(this);
        registerReceiver(this.cBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        setBluetoothConnectedReceiver();
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        FrameLayout banner = findViewById(R.id.banner);
        NatBetsAll.getInstance().natVolBetsl(MainAct.this, banner, findViewById(R.id.cardBAnner), admobNativeLarge2, cardView);


        b.btPremiumTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions(PT_RC)) {
                    openMyPt();
                }
            }
        });

        b.btClassicTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions(CT_RC)) {
                    openMyCt();
                }
            }
        });
        b.btPopularTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions(AT_RC)) {
                    openMyAt();
                }

            }
        });


        b.btSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(MainAct.this, AppUsesAct.class);
                        startActivity(intent);
                    }
                });
            }
        });
        b.btExplosive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(MainAct.this, ExplosiveAct.class);
                        startActivity(intent);
                    }
                });
            }
        });
        b.btNewTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(MainAct.this, NewThemeCreateAct.class);
                        startActivity(intent);
                    }
                });

            }
        });
        b.btOwlTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(MainAct.this, OwlThemeCreateAct.class);
                        startActivity(intent);
                    }
                });

            }
        });
        b.btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(MainAct.this, SettingAct.class);
                        startActivity(intent);
                    }
                });

            }
        });
        b.btAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions(ANIM_RC)) {
                    openMyAnim();
                }

            }
        });

        b.btAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyCustomAnim();


            }
        });
        b.btAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions(ALARM_RC)) {
                    openMyAlarm();
                }

            }
        });
        b.btGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions(GALLERY_RC)) {
                    openMyGallery();
                }

            }
        });
        b.btBtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bi = new Intent(MainAct.this, BtInfoAct.class);
                bi.putExtra("totalCapacity", totalCapacity);
                bi.putExtra("health", healthTv);
                bi.putExtra("batteryPct", batteryPctTv);
                bi.putExtra("plugged", pluggedTv);
                bi.putExtra("chargingStatus", chargingStatusTv);
                bi.putExtra("voltage", voltageTv);
                bi.putExtra("temp", tempTv);
                bi.putExtra("technology", technologyTv);

                GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(bi);
                    }
                });


            }
        });
        b.btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        b.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
                    intent.putExtra("android.intent.extra.TEXT", "Download " + getResources().getString(R.string.app_name) + " app from   - https://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent, "Share Application"));
                } catch (Exception e) {
                }
                try {
                    b.drawerLayout.closeDrawer(GravityCompat.START);
                } catch (Exception e) {

                }
            }
        });
        b.cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                try {
                    String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainAct.this, " unable to find market app", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
                try {
                    b.drawerLayout.closeDrawer(GravityCompat.START);
                } catch (Exception e) {

                }
            }
        });

        b.cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (DBettryplication.getInstance().getAppDetail() != null) {
                        if (DBettryplication.getInstance().getAppDetail().getPrivacy() != null && !DBettryplication.getInstance().getAppDetail().getPrivacy().trim().isEmpty()) {

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(DBettryplication.getInstance().getAppDetail().getPrivacy()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setPackage("com.android.chrome");
                            try {
                                startActivity(i);
                            } catch (ActivityNotFoundException e) {
                                i.setPackage(null);
                                startActivity(i);
                            }
                        } else {
                            Toast.makeText(MainAct.this, "Unable to open", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainAct.this, "Unable to open", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(MainAct.this, "Unable to open", Toast.LENGTH_SHORT).show();
                }
                try {
                    b.drawerLayout.closeDrawer(GravityCompat.START);
                } catch (Exception e) {

                }

            }
        });
        b.cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoChBetryNils.getInstance().showChBetryNilster((Activity) context, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(MainAct.this, MoreActivity.class);
                        startActivity(intent);
                    }
                });


            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setThem();
                setPresetData();

                createDialog();
                downlaodownDialog();
                getCbaApiData();
                setBlL();
            }
        }, 50);


    }


    private void setAppUsed() {
        try {
            if (checkIfGetPermission()) {
                b.cvPerc2.setVisibility(View.GONE);
                getApp();
            } else {
                b.cvPerc2.setVisibility(View.VISIBLE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        b.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), REQ_USAGE);
            }
        });
        b.switchc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), REQ_USAGE);
            }
        });


    }

    public void getApp() {

        this.timeUsedArrayList.clear();
        new UsageDailyTask(this, new UsageDailyTask.OnLoadUsageDaily() {

            @Override
            public final void onLoadFinish(UsageDaily usageDaily) {
                timeUsedApp(usageDaily);
            }
        }).execute(new Void[0]);
    }

    private void timeUsedApp(UsageDaily usageDaily) {
        new UsedTimeTask(usageDaily, this, new UsedTimeTask.OnLoad() {
            @Override
            public final void onLoadFinish(ArrayList arrayList) {
                updateLsit(arrayList);
            }


        }).execute(new Void[0]);
    }

    private void updateLsit(ArrayList arrayList) {

        this.timeUsedArrayList.addAll(arrayList);
        Collections.sort(timeUsedArrayList, TimeUsedAU.ListLM);

        if (!timeUsedArrayList.isEmpty()) {
            TimeUsedAU timeUsed = timeUsedArrayList.get(0);
            b.appLogo1.setImageDrawable(timeUsed.getIconApp());
            b.appName1.setText(timeUsed.getAppName());
            b.appTime1.setText(UtilsAU.convertTimes((int) timeUsed.getTotalTime()));

            TimeUsedAU timeUsed2 = timeUsedArrayList.get(1);
            b.appLogo2.setImageDrawable(timeUsed2.getIconApp());
            b.appName2.setText(timeUsed2.getAppName());
            b.appTime2.setText(UtilsAU.convertTimes((int) timeUsed2.getTotalTime()));

            TimeUsedAU timeUsed3 = timeUsedArrayList.get(2);
            b.appLogo3.setImageDrawable(timeUsed3.getIconApp());
            b.appName3.setText(timeUsed3.getAppName());
            b.appTime3.setText(UtilsAU.convertTimes((int) timeUsed3.getTotalTime()));
        }


    }

    private void setPresetData() {
        float progress = 1.0f - (((float) 500 / 1000.0f));
        b.waveView.setWaveXAxisPositionMultiplier(progress);
        b.tvDay.setText(ManyCAUSed.getDate());
        b.tvDay1.setText(ManyCAUSed.getDate2());
        b.tvDay2.setText(ManyCAUSed.getDate3());
    }

    private void setThem() {


        if (shCAPreference.getThemeType() == 1) {
            themeColor = getResources().getColor(R.color.th_1);
        } else if (shCAPreference.getThemeType() == 2) {

            themeColor = getResources().getColor(R.color.th_2);
        } else if (shCAPreference.getThemeType() == 3) {

            themeColor = getResources().getColor(R.color.th_3);
        } else if (shCAPreference.getThemeType() == 4) {
            themeColor = getResources().getColor(R.color.th_4);
        }
        b.bPer.setProgressColor(themeColor);
        b.circularProgressBar.setProgressBarColor(themeColor);
        b.appTime2.setTextColor(themeColor);
        b.appTime1.setTextColor(themeColor);
        b.appTime3.setTextColor(themeColor);
        //  b.txtf.setTextColor(themeColor);
        b.ivAnimation.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivAnimator.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivAlarm.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivGallery.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivBti.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivExplosive.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.fluid.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);


        Glide.with(this).load(R.drawable.wal3).into(b.ivPt);
        Glide.with(this).load(R.drawable.wal).into(b.ivCt);
        Glide.with(this).load(R.drawable.wal2).into(b.ivAt);

        int c = ColorUtils.setAlphaComponent(themeColor, 100);
        b.switch1.setBgOnColor(c);
        b.switch1.setToggleOnColor(themeColor);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USAGE) {
            setAppUsed();
        }

    }

    private BroadcastReceiver cBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {

            int level = intent.getIntExtra("level", 0);
            int temperature = intent.getIntExtra("temperature", 0);
            int plugged = intent.getIntExtra("plugged", -1);
            updateBatteryData(intent);


            ChargingLevel = String.valueOf(level);

            temps = ((double) temperature) / 10.0d;

            strChargeVia = getPlugTypeString(plugged);


            String TotalCapacity = getBatteryCapacity();
            setRemainingCapacity(level, TotalCapacity);

            if (level > 74) {

                shCAPreference.setThemeType(4);

            } else if (level > 49) {
                shCAPreference.setThemeType(3);
            } else if (level > 24) {
                shCAPreference.setThemeType(2);
            } else if (level > 0) {
                shCAPreference.setThemeType(1);
            }


            b.bPer.setProgress(level);
            if (level == 100) {
                b.cvFull.setBackgroundColor(getResources().getColor(R.color.th_4));
            } else {
                b.cvFull.setBackgroundColor(getResources().getColor(R.color.theme_bg2));
            }
            b.tvMainPer.setText(level + "%");
            BtInfoAct.setData(pluggedTv, chargingStatusTv);

        }
    };


    public String getBatteryCapacity() {
        Object mPowerProfile_ = null;
        try {
            mPowerProfile_ = Class.forName("com.android.internal.os.PowerProfile").getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.totalCapacity = String.valueOf((long) ((Double) Class.forName("com.android.internal.os.PowerProfile").getMethod("getAveragePower", String.class).invoke(mPowerProfile_, "battery.capacity")).doubleValue());
            return this.totalCapacity;
        } catch (Exception e2) {
            e2.printStackTrace();
            return this.totalCapacity;
        }
    }

    private int setRemainingCapacity(int batteryLevel2, String totalCapacity2) {
        int k = Math.round(((float) (((long) Integer.parseInt(totalCapacity2)) / 100)) * ((float) batteryLevel2));
        return k;
    }

    private String getPlugTypeString(int plugged) {
        switch (plugged) {
            case 1:

                return "Power Adapter";
            case 2:

                return "USB";
            default:
                return "Unknown";
        }
    }

    private void updateBatteryData(Intent intent) {

        if (intent.getBooleanExtra("present", false)) {
            switch (intent.getIntExtra("health", 0)) {

                case 2:
                    healthTv = "Good";
                    break;
                case 3:
                    healthTv = "Overheat";
                    break;
                case 4:
                    healthTv = "Dead";
                    break;
                case 5:
                    healthTv = "Voltage";
                    break;
                case 6:
                    healthTv = "Failure";
                    break;
                case 7:
                    healthTv = "Cold";
                    break;
            }
            int level = intent.getIntExtra("level", -1);
            int scale = intent.getIntExtra("scale", -1);
            if (!(level == -1 || scale == -1)) {
                this.batteryPctTv = ((int) ((((float) level) / ((float) scale)) * 100.0f)) + " %";
            }
            switch (intent.getIntExtra("plugged", 0)) {
                case 1:
                    pluggedTv = "AC";
                    break;
                case 2:
                    pluggedTv = "USB";
                    break;
                case 3:
                case 4:
                    pluggedTv = "Wireless";
                    break;
                default:
                    pluggedTv = "None";
                    break;

            }

            switch (intent.getIntExtra("status", -1)) {
                case 1:
                    chargingStatusTv = "";
                    break;
                case 2:
                    chargingStatusTv = "CHARGING";
                    break;
                case 3:
                    chargingStatusTv = "DISCHARGING";
                    break;
                case 4:
                default:
                    chargingStatusTv = "DISCHARGING";
                    break;
                case 5:
                    chargingStatusTv = "FULL";
                    break;
            }

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString("technology");
                if (!"".equals(technology)) {
                    this.technologyTv = technology;
                }
            }
            int temperature = intent.getIntExtra("temperature", 0);
            if (temperature > 0) {
                this.tempTv = (((float) temperature) / 10.0f) + " °C";
            }
            int voltage = intent.getIntExtra("voltage", 0);
            if (voltage > 0) {
                this.voltageTv = ((float) (voltage / 1000)) + " V";
            }
            long capacity = getBatteryCapacity(this);
            if (capacity > 0) {
                this.capacityTv = capacity + " mAh";
                return;
            }
            return;
        }
        Toast.makeText(this, "No Battery present", Toast.LENGTH_SHORT).show();
    }

    public long getBatteryCapacity(Context ctx) {
        if (Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        BatteryManager mBatteryManager = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
        return (long) ((((float) Long.valueOf(mBatteryManager.getLongProperty(1)).longValue()) / ((float) Long.valueOf(mBatteryManager.getLongProperty(4)).longValue())) * 100.0f);
    }

    @SuppressLint({"WrongConstant"})
    private boolean checkIfGetPermission() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return ((AppOpsManager) getSystemService("appops")).checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, getPackageName()) == 0;
        }
        return false;
    }

    private void createDialog() {
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_lodding, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(false);

        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        create.show();
    }

    private void downlaodownDialog() {

        dowBuilder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_download2, (ViewGroup) null);
        dowBuilder.setView(inflate);
        dowBuilder.setCancelable(false);
        dowCreate = dowBuilder.create();
        dowCreate.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        tvDowPer = inflate.findViewById(R.id.tvDownPer);
        cvLBG = inflate.findViewById(R.id.cvLBG);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_btn_bg2);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        cvLBG.setBackground(wrappedDrawable);
    }

    private void getCbaApiData() {

        b.rvAnimVideo.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        b.rvAnimVideo.setItemAnimator(new DefaultItemAnimator());
        apiInterface = CAAPIClient.getClient().create(APIInterface.class);
        fristAPIInterface = FristCAAPIClient.getClient().create(FristCAAPIInterface.class);


        Call<FirstCAApi> call = fristAPIInterface.doCreateUserWithField(packagename, category, username, password);
        call.enqueue(new Callback<FirstCAApi>() {


            @Override
            public void onResponse(Call<FirstCAApi> call, Response<FirstCAApi> response) {
                FirstCAApi firstBaApi = response.body();
                if (response.body() == null) {
                    create.dismiss();
                    b.cvAnim.setVisibility(View.GONE);
                    Toast.makeText(MainAct.this, "Go Back And Reopen Or Check Internet", Toast.LENGTH_SHORT).show();
                } else {

                    getCbaApiVideo(firstBaApi.getPackageName(), firstBaApi.getCategory(), firstBaApi.getUsername(), firstBaApi.getPassword(), firstBaApi.getHeaderkey());
                }

            }

            @Override
            public void onFailure(Call<FirstCAApi> call, Throwable t) {
                create.dismiss();
                b.cvAnim.setVisibility(View.GONE);
                Toast.makeText(MainAct.this, "Go Back And Reopen Or Check Internet", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getCbaApiVideo(String packageName, String category, String username, String password, String headerkey) {

        String credentials = username + ":" + password;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<CAExample> call3 = apiInterface.doCreateUserWithField(basic, packageName, category, headerkey);
        call3.enqueue(new Callback<CAExample>() {

            @Override
            public void onResponse(Call<CAExample> call, Response<CAExample> response) {
                CAExample example = response.body();
                videoList = example.getData();

                setDataInRv(videoList);
            }

            @Override
            public void onFailure(Call<CAExample> call, Throwable t) {
                create.dismiss();
                b.cvAnim.setVisibility(View.GONE);
                Toast.makeText(MainAct.this, "Go Back And Reopen Or Check Internet", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setDataInRv(List<CADatum> vl) {
        List<CADatum> videoList = new ArrayList<>();
        if (vl.size() > 0) {
            if (shCAPreference.getAnimList_ca() == 1) {
                videoList.add(vl.get(0));
                videoList.add(vl.get(1));
                videoList.add(vl.get(2));
                videoList.add(vl.get(3));
                videoList.add(vl.get(4));
                videoList.add(vl.get(5));
                shCAPreference.setAnimList_ca(2);
            } else if (shCAPreference.getAnimList_ca() == 2) {
                videoList.add(vl.get(6));
                videoList.add(vl.get(7));
                videoList.add(vl.get(8));
                videoList.add(vl.get(9));
                videoList.add(vl.get(10));
                videoList.add(vl.get(11));
                shCAPreference.setAnimList_ca(3);
            } else if (shCAPreference.getAnimList_ca() == 3) {
                videoList.add(vl.get(12));
                videoList.add(vl.get(13));
                videoList.add(vl.get(14));
                videoList.add(vl.get(15));
                videoList.add(vl.get(16));
                videoList.add(vl.get(17));
                shCAPreference.setAnimList_ca(1);
            }
        }


        animHorVideoCAAdapter = new AnimHorVideoCAAdapter(this, (ArrayList<CADatum>) videoList);
        b.rvAnimVideo.setAdapter(animHorVideoCAAdapter);
        animHorVideoCAAdapter.OnItemClickListenerS(new AnimHorVideoCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, CADatum datum) {
                if (checkBcaPermissions(ANIMP_RC)) {

                    filePath = datum.getOriginalUrl();
                    filePath_low = datum.getResizedUrl();
                    checkFile();
                }
            }
        });

        create.dismiss();

    }

    private boolean checkBcaPermissions(int RC) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(MainAct.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), RC);

            return false;
        }
        return true;
    }

    private void checkFile() {
        if (ManyCAUSed.isNetworkAvailable(MainAct.this)) {
            String fileName = URLUtil.guessFileName(filePath_low, null, null);
            File file = new File(getFilesDir(), "bca");
            File videoPathCheck = new File(file, "low_resize_" + fileName);
            if (videoPathCheck.exists()) {
                openAnimPerView(videoPathCheck.getAbsolutePath(), filePath);
            } else {
                new animDownlaod().execute("low");
            }


        } else {

            noInterNetDialog(1);
        }
    }

    private void noInterNetDialog(int i) {
        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_no_internet, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView btCancel = inflate.findViewById(R.id.btCancel);
        TextView btRetry = inflate.findViewById(R.id.btRetry);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_btn_bg2);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        btRetry.setBackground(wrappedDrawable);
        btCancel.setTextColor(themeColor);


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();

            }
        });
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();
                if (i == 1) {
                    checkFile();
                } else if (i == 2) {
                    openMyAnim();
                }


            }
        });
        create.show();

    }

    public class animDownlaod extends AsyncTask<String, String, String> {
        int count;

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            dowCreate.show();
            tvDowPer.setText("0%");
        }

        @Override
        public String doInBackground(String... strings) {
            String chekQ = strings[0];
            String urls;

            if (chekQ.equals("high")) {

                urls = filePath;
            } else {
                urls = filePath_low;


            }

            File file = new File(getFilesDir(), "bca");
            if (!file.exists()) {
                file.mkdirs();

            }


            String fileName = URLUtil.guessFileName(urls, null, null);
            videoPath = new File(file, "low_resize_" + fileName);
            URL url = null;
            try {
                url = new URL(urls);

                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();


                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                FileOutputStream output = new FileOutputStream(videoPath);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }


                output.flush();

                output.close();
                input.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return videoPath.getAbsolutePath();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvDowPer.setText(values[0] + "%");
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            dowCreate.dismiss();

            if (s == null) {


                videoPath.delete();
                Toast.makeText(MainAct.this, "Download Failed...", Toast.LENGTH_SHORT).show();

            } else {


                openCbaAskDialog();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            videoPath.delete();
            Toast.makeText(MainAct.this, "Download Failed...", Toast.LENGTH_SHORT).show();

        }
    }

    private void openCbaAskDialog() {
        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_after_downlaod, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView btCancel = inflate.findViewById(R.id.btCancel);
        TextView btShow = inflate.findViewById(R.id.btShow);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_btn_bg2);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        btShow.setBackground(wrappedDrawable);
        btCancel.setTextColor(themeColor);


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();

            }
        });
        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();


                openAnimPerView(videoPath.getAbsolutePath(), filePath);

            }
        });
        create.show();

    }

    private void openAnimPerView(String absolutePath, String filePath) {
        Intent intent = new Intent(MainAct.this, AnimEditAct.class);

        intent.putExtra("type", "animVideo");
        intent.putExtra("path", absolutePath);
        intent.putExtra("path_high", filePath);


        GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
            @Override
            public void aChBetryNilsCall() {
                startActivity(intent);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == PT_RC) {
                    openMyPt();
                }
                if (requestCode == CT_RC) {
                    openMyCt();
                }
                if (requestCode == AT_RC) {
                    openMyAt();
                }
                if (requestCode == GALLERY_RC) {
                    openMyGallery();
                }

                if (requestCode == ANIM_RC) {
                    openMyAnim();
                }
                if (requestCode == ALARM_RC) {
                    openMyAlarm();
                }
                if (requestCode == ANIMP_RC) {

                }

            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permDialog();
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                if (requestCode == PT_RC) {
                    openMyPt();
                }
                if (requestCode == CT_RC) {
                    openMyCt();
                }
                if (requestCode == AT_RC) {
                    openMyAt();
                }
                if (requestCode == GALLERY_RC) {
                    openMyGallery();
                }

                if (requestCode == ANIM_RC) {
                    openMyAnim();
                }
                if (requestCode == ALARM_RC) {
                    openMyAlarm();
                }
                if (requestCode == ANIMP_RC) {

                }
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainAct.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permDialog();
                }
            }
        }
        if (requestCode == 1010) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                setBlL();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainAct.this, android.Manifest.permission.BLUETOOTH_SCAN)) {
                    permDialog();
                }
            }

        }
    }

    private void openMyAt() {

        GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
            @Override
            public void aChBetryNilsCall() {
                Intent intent = new Intent(MainAct.this, PopularCreateThemeAct.class);
                startActivity(intent);
            }
        });

    }

    private void openMyCt() {


        GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
            @Override
            public void aChBetryNilsCall() {
                Intent intent = new Intent(MainAct.this, ClassicCreateThemeAct.class);
                startActivity(intent);
            }
        });


    }

    private void openMyAnim() {
        if (ManyCAUSed.isNetworkAvailable(MainAct.this)) {

            GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, () -> {
                Intent intent = new Intent(MainAct.this, AnimationListAct.class);
                startActivity(intent);
            });
        } else {
            noInterNetDialog(2);
        }


    }

    private void openMyCustomAnim() {
        GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, () -> {

            Intent intent = new Intent(MainAct.this, AnimatorAct.class);
            startActivity(intent);
        });

    }

    private void openMyAlarm() {
        GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, () -> {
            Intent intent = new Intent(MainAct.this, AlarmAct.class);
            startActivity(intent);
        });
    }

    private void openMyGallery() {
        GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
            @Override
            public void aChBetryNilsCall() {

                Intent intent = new Intent(MainAct.this, GalleryAct.class);
                startActivity(intent);
            }
        });

    }

    private void openMyPt() {
        GoChBetryNils.getInstance().showChBetryNilster(MainAct.this, new GoChBetryNils.AChBetryNilInterface() {
            @Override
            public void aChBetryNilsCall() {
                Intent intent = new Intent(MainAct.this, PremiumCreateThemeAct.class);
                startActivity(intent);
            }
        });


    }

    private void permDialog() {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_permission, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);


        TextView tvOk = inflate.findViewById(R.id.textView_ok);
        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView textView_cancle = inflate.findViewById(R.id.textView_cancle);
        textView_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();


            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();


                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });


        create1.show();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (b.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            b.drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            DBettryplication myAppWCleaUm = DBettryplication.getInstance();

            if (myAppWCleaUm.getAppDetail() != null && myAppWCleaUm.getAppDetail().getAppscreennumber() != null && !TextUtils.isEmpty(myAppWCleaUm.getAppDetail().getAppscreennumber()) && myAppWCleaUm.getAppDetail().getAppscreennumber().equals("0")) {

                startActivity(new Intent(MainAct.this, ExitAct.class));
            } else {

                if (myAppWCleaUm.getAppDetail() != null && myAppWCleaUm.getAppDetail().getAppscreennumber() != null && !TextUtils.isEmpty(myAppWCleaUm.getAppDetail().getAppscreennumber())) {

                    finish();

                } else {

                    startActivity(new Intent(MainAct.this, ExitAct.class));
                }


            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (this.cBatInfoReceiver != null) {
                unregisterReceiver(this.cBatInfoReceiver);
            }
            if (bluetoothConnectedReceiver != null) {
                unregisterReceiver(this.bluetoothConnectedReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (videoList != null) {
            videoList.clear();
        }
        if (create != null && create.isShowing()) {
            create.dismiss();
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAppUsed();
            }
        }, 50);

        if (animHorVideoCAAdapter != null) {
            animHorVideoCAAdapter.notifyDataSetChanged();
        }

    }


    private void setBluetoothConnectedReceiver() {
        this.bluetoothConnectedReceiver = new BluetoothConnectedReceiver();
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
        intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        intentFilter.addAction("android.bluetooth.adapter.action.SCAN_MODE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        registerReceiver(this.bluetoothConnectedReceiver, intentFilter);
    }

    @Override
    public void onFailure(BLEInFo bLEInFo) {
        if (bLEInFo.getBluetoothDevice() != null) {
            this.bleInFoArrayList.add(bLEInFo);
            runOnUiThread(new Runnable() {
                @Override
                public final void run() {

                }
            });
        }

        ArrayList<BLEInFo> arrayList = this.bleInFoArrayList;
        if (arrayList == null) {
            return;
        }
    }

    @Override
    public void onSuccess(BLEInFo bLEInFo) {
        if (bLEInFo != null) {
            this.bleInFoArrayList.add(bLEInFo);
            runOnUiThread(new Runnable() {
                @Override
                public final void run() {
                    updateData();
                }
            });
        }
        ArrayList<BLEInFo> arrayList = this.bleInFoArrayList;
        if (arrayList == null) {
            return;
        }


    }

    @SuppressLint("WrongConstant")
    public static void getBLE() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!checkBLPermissions()) {
                return;
            }
        }

        try {
            bleInFoArrayList.clear();
            if (ActivityCompat.checkSelfPermission(mainAct, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            for (BluetoothDevice bluetoothDevice : ((BluetoothManager) mainAct.getSystemService("bluetooth")).getConnectedDevices(7)) {
                mBLEConnectHelper.connect(bluetoothDevice);
            }
            for (BluetoothDevice bluetoothDevice2 : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {

                int intValue = ((Integer) bluetoothDevice2.getClass().getMethod("getBatteryLevel", new Class[0]).invoke(bluetoothDevice2, new Object[0])).intValue();
                if (intValue != -1) {
                    BLEInFo bLEInFo = new BLEInFo();
                    bLEInFo.setBatteryLevel(intValue);
                    bLEInFo.setBattery(intValue + "%");
                    bLEInFo.setBluetoothDevice(bluetoothDevice2);
                    bLEInFo.setDeviceName(bluetoothDevice2.getName());
                    mainAct.bleInFoArrayList.add(bLEInFo);
                    updateData();
                } else {

                }

            }


            updateData();

            ArrayList<BLEInFo> arrayList = mainAct.bleInFoArrayList;
            if (arrayList == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateData() {
        if (!bleInFoArrayList.isEmpty()) {
            b.cvPerc1.setVisibility(View.GONE);
            String sPer = bleInFoArrayList.get(0).getBattery();
            b.tvBlName.setText(bleInFoArrayList.get(0).getDeviceName());
            b.tvBlPer.setText(bleInFoArrayList.get(0).getBattery());
            sPer = sPer.replace("%", "");
            b.circularProgressBar.setProgress(Float.parseFloat(sPer));
        } else {

            b.tvBlName.setText("None of the devices are connected");
            b.tvBlPer.setText("00%");
            b.circularProgressBar.setProgress(0f);
        }
    }

    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private void setBlL() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkBLPermissions()) {
                b.cvPerc1.setVisibility(View.GONE);
                getBLE();
            } else {
                b.cvPerc1.setVisibility(View.VISIBLE);
            }
        } else {
            b.cvPerc1.setVisibility(View.GONE);
            getBLE();
        }
        b.switchc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reqPermissions()) {
                    setBlL();
                }
            }
        });
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.isEnabled()) {

            b.switch1.setChecked(true);

        } else {
            b.switch1.setChecked(false);
            b.tvBlName.setText("None of the devices are connected");
            b.tvBlPer.setText("00%");
            b.circularProgressBar.setProgress(0f);
            b.cvBluetooth.setAlpha(0.5f);

        }

        b.switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (!checkBLPermissions()) {
                        return;
                    }
                }
                try {
                    if (adapter.isEnabled()) {
                        adapter.disable();
                        b.switch1.setChecked(false);
                        b.tvBlName.setText("None of the devices are connected");
                        b.tvBlPer.setText("00%");
                        b.circularProgressBar.setProgress(0f);
                        b.cvBluetooth.setAlpha(0.5f);
                        //
                    } else {
                        adapter.enable();
                        b.switch1.setChecked(true);
                    }
                } catch (Exception e) {

                }

                getBLE();

            }
        });

    }

    public static void setConnected(boolean b) {
        try {
            if (mainAct != null) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBLE();
                    }
                }, 5000);
                getBLE();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBlOnOFF(boolean bl) {
        try {
            if (bl) {
                b.switch1.setChecked(true);
                b.cvBluetooth.setAlpha(1f);
            } else {
                b.cvBluetooth.setAlpha(0.5f);
                b.switch1.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkBLPermissions() {
        int permission2 = ActivityCompat.checkSelfPermission(mainAct, Manifest.permission.BLUETOOTH_SCAN);
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private boolean reqPermissions() {
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            requestBlePermissions(this, 1010);
            return false;
        }
        return true;
    }

    public static void requestBlePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
        else
            ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, requestCode);
    }
}
package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.WalVideoCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActWallpaperListBinding;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.WalFirstCAApi;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.WalFristCAAPIInterface;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.WalFristCAAPIClient;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi2.WalCAPIInterface;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi2.WalCAAPIClient;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi2.WalCADatum;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi2.WalCAExample;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi2.WallpaperCA;

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
import java.util.List;

import io.michaelrocks.paranoid.Obfuscate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Obfuscate
public class WallpaperListAct extends Activity {

    ActWallpaperListBinding b;
    WalCAPIInterface apiInterface;
    WalFristCAAPIInterface fristAPIInterface;
    WalVideoCAAdapter walVideoCAAdapter;
    AlertDialog.Builder builder;
    AlertDialog create;
    private AlertDialog.Builder dowBuilder;
    AlertDialog dowCreate;
    File videoPath = null;
    String filePath;
    BufferedInputStream bis = null;
    FileOutputStream fis;
    private TextView tvDowPer;
    private ConstraintLayout cvLBG;
    private ShCAPreference shCAPreference;
    private int themeColor;


    public static String packagename = "com.wallpaper.hdwallpaper";
    public static String category = "abstract";
    public static String username = "hdwallpaper";
    public static String password = "hdwallpaper.si";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActWallpaperListBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        shCAPreference = new ShCAPreference(this);

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NatBetsAll.getInstance().natVolBetsl(f, WallpaperListAct.this, c, findViewById(R.id.admobNative_Banner), findViewById(R.id.nativesmallcard));

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        b.rvAnimVideo.setLayoutManager(gridLayoutManager);
        b.rvAnimVideo.setItemAnimator(new DefaultItemAnimator());

        apiInterface = WalCAAPIClient.getClient().create(WalCAPIInterface.class);
        fristAPIInterface = WalFristCAAPIClient.getClient().create(WalFristCAAPIInterface.class);

        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setThemeApp();
        createDialog();
        downlaodownDialog();
        getApiData();
    }

    private void setThemeApp() {
        if (shCAPreference.getThemeType() == 1) {
            themeColor = getResources().getColor(R.color.th_1);
        } else if (shCAPreference.getThemeType() == 2) {

            themeColor = getResources().getColor(R.color.th_2);
        } else if (shCAPreference.getThemeType() == 3) {

            themeColor = getResources().getColor(R.color.th_3);
        } else if (shCAPreference.getThemeType() == 4) {
            themeColor = getResources().getColor(R.color.th_4);
        }

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

    private void createDialog() {
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_lodding, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(false);

        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        create.show();

    }

    private void getApiData() {

        Call<WalFirstCAApi> call = fristAPIInterface.doCreateUserWithField(packagename, category, username, password);
        call.enqueue(new Callback<WalFirstCAApi>() {

            @Override
            public void onResponse(Call<WalFirstCAApi> call, Response<WalFirstCAApi> response) {
                WalFirstCAApi firstApi = response.body();
                getApiVideo(firstApi.getPackageName(), firstApi.getCategory(), firstApi.getUsername(), firstApi.getPassword(), firstApi.getHeaderkey());

            }

            @Override
            public void onFailure(Call<WalFirstCAApi> call, Throwable t) {
                create.dismiss();
                noInterNetDialog(1);


            }
        });

    }

    private void getApiVideo(String packageName, String category, String username, String password, String headerkey) {

        String credentials = username + ":" + password;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<WalCAExample> call3 = apiInterface.doCreateUserWithField(basic, packageName, category, headerkey);
        call3.enqueue(new Callback<WalCAExample>() {


            @Override
            public void onResponse(Call<WalCAExample> call, Response<WalCAExample> response) {
                WalCAExample example = response.body();
                List<WalCADatum> videoList = example.getData();
                setDataInRv(videoList);
            }

            @Override
            public void onFailure(Call<WalCAExample> call, Throwable t) {
                create.dismiss();
                noInterNetDialog(1);



            }
        });

    }

    public class downlaod extends AsyncTask<String, String, String> {
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


            urls = filePath;


            File file = new File(getFilesDir(), "bca");
            if (!file.exists()) {
                file.mkdirs();

            }


            String getUser = filePath.substring(filePath.lastIndexOf("/") - 20);
            String string = getUser.replace("/", "_");
            String fileName = string;
            videoPath = new File(file, fileName);
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
                Toast.makeText(WallpaperListAct.this, "Download Failed...", Toast.LENGTH_SHORT).show();

            } else {


                openAskDialog();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            videoPath.delete();
            Toast.makeText(WallpaperListAct.this, "Download Failed...", Toast.LENGTH_SHORT).show();

        }
    }

    private void openAskDialog() {
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
        TextView tvLabel = inflate.findViewById(R.id.tvLabel);
        btShow.setText("Select");
        tvLabel.setText("Background Download Successful");
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

                Intent returnIntent = new Intent();
                returnIntent.putExtra("path", videoPath.getAbsolutePath());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        create.show();

    }

    private void setDataInRv(List<WalCADatum> catList) {
        List<WallpaperCA> videoList = new ArrayList<>();

        for (int i = 0; i < catList.size(); i++) {
            if (i == 8 || i == 10) {

                for (WallpaperCA w : catList.get(i).getWallpaper()) {
                    videoList.add(w);
                }

            }
        }

        if(videoList.isEmpty()){
            b.rvAnimVideo.setVisibility(View.GONE);
            b.tvNoData.setVisibility(View.VISIBLE);
        }else {
            b.tvNoData.setVisibility(View.GONE);
            b.rvAnimVideo.setVisibility(View.VISIBLE);
            walVideoCAAdapter = new WalVideoCAAdapter(this, (ArrayList<WallpaperCA>) videoList);
            b.rvAnimVideo.setAdapter(walVideoCAAdapter);
            walVideoCAAdapter.OnItemClickListenerS(new WalVideoCAAdapter.OnItemClickListenerS() {
                @Override
                public void onItemClick1(View view, WallpaperCA datum) {
                    filePath = datum.getOriginal();

                    String getUser = filePath.substring(filePath.lastIndexOf("/") - 20);
                    String string = getUser.replace("/", "_");
                    String fileName = string;
                    File file = new File(getFilesDir(), "bca");


                    File videoPathCheck = new File(file, fileName);
                    if (videoPathCheck.exists()) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("path", videoPathCheck.getAbsolutePath());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        downLoadWal();
                    }


                }
            });
        }

        create.dismiss();


    }
    private void downLoadWal() {
        if (ManyCAUSed.isNetworkAvailable(WallpaperListAct.this)) {
            new downlaod().execute("low");} else {

            noInterNetDialog(2);

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
                if(i==2) {
                    downLoadWal();
                }
                if(i==1){
                    getApiData();
                }


            }
        });
        create.show();

    }
}

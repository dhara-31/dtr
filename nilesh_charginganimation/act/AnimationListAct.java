package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
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
import com.si_charginganimation.nilesh_charginganimation.adapter.AnimVideoCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.AnimCAApi1.CADatum;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActAnimListBinding;
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
import java.util.List;

public class AnimationListAct extends Activity {

    ActAnimListBinding b;
    AnimVideoCAAdapter animVideoCAAdapter;
    AlertDialog.Builder builder;
    AlertDialog create;
    private AlertDialog.Builder dowBuilder;
    AlertDialog dowCreate;
    public String filePath_low;
    File videoPath = null;
    String filePath;

    private TextView tvDowPer;
    private int cpos = 101;
    private ConstraintLayout cvLBG;
    private int themeColor;
    private ShCAPreference shCAPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActAnimListBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        shCAPreference = new ShCAPreference(this);

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NatBetsAll.getInstance().natVolBetsl(f, AnimationListAct.this, c, findViewById(R.id.admobNative_Banner), findViewById(R.id.nativesmallcard));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        b.rvAnimVideo.setLayoutManager(gridLayoutManager);
        b.rvAnimVideo.setItemAnimator(new DefaultItemAnimator());

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setThemeApp();
        createLoadingDialog();
        downlaodownDialog();
        DispalyDataInRv();

    }

    private void noInterNetDialog() {
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

                downLoad();
            }
        });
        create.show();

    }


    private void downLoad() {
        if (ManyCAUSed.isNetworkAvailable(AnimationListAct.this)) {
            new downlaodCba().execute("low");
        } else {
            noInterNetDialog();
        }
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
        tvDowPer.setText("0%");
    }

    private void createLoadingDialog() {
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_lodding, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(false);

        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        create.show();

    }


    public class downlaodCba extends AsyncTask<String, String, String> {
        int count = 0;

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
                return null;
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
                Toast.makeText(AnimationListAct.this, "Download Failed...", Toast.LENGTH_SHORT).show();

            } else {


                openCbaAskDialog();
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            videoPath.delete();
            Toast.makeText(AnimationListAct.this, "Download Failed...", Toast.LENGTH_SHORT).show();

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
                openPer();


            }
        });
        create.show();

    }


    private void DispalyDataInRv() {

        List<CADatum> videoList = MainAct.videoList;

        if (videoList == null){
            videoList=new ArrayList<>();
        }
        if ( videoList.isEmpty()) {
            b.tvNoData.setVisibility(View.VISIBLE);
            b.rvAnimVideo.setVisibility(View.GONE);
            create.dismiss();

        } else {

            animVideoCAAdapter = new AnimVideoCAAdapter(this, (ArrayList<CADatum>) videoList);
            b.rvAnimVideo.setAdapter(animVideoCAAdapter);
            animVideoCAAdapter.OnItemClickListenerS(new AnimVideoCAAdapter.OnItemClickListenerS() {
                @Override
                public void onItemClick1(View view, CADatum CADatum, int position) {

                    filePath = CADatum.getOriginalUrl();
                    filePath_low = CADatum.getResizedUrl();

                        String fileName = URLUtil.guessFileName(CADatum.getResizedUrl(), null, null);
                        File file = new File(getFilesDir(), "bca");

                        cpos = position;
                        videoPath = new File(file, "low_resize_" + fileName);
                        if (videoPath.exists()) {

                            openPer();

                        } else {
                           downLoad();
                        }



                }
            });

            create.dismiss();
        }

    }

    private void openPer() {
        Intent intent = new Intent(AnimationListAct.this, AnimEditAct.class);

        intent.putExtra("type", "animVideo");
        intent.putExtra("path", videoPath.getAbsolutePath());
        intent.putExtra("path_high", filePath);


        GoChBetryNils.getInstance().showChBetryNilster(AnimationListAct.this, new GoChBetryNils.AChBetryNilInterface() {
            @Override
            public void aChBetryNilsCall() {
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (animVideoCAAdapter != null) {
            animVideoCAAdapter.notifyDataSetChanged();
        }
    }
}

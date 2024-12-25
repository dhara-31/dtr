package com.test.testing12345.frgment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.test.testing12345.R;
import com.test.testing12345.activity.CreateMoreStickerCsActivity;
import com.test.testing12345.adapter.StiAdapter;
import com.test.testing12345.adapter.StiAdapter2;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.StoreageCkPref;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class CustonmStickerCkFragment extends Fragment {

    ListView listView, listView2;
    TextView tvClick;
    ImageView btnSpin;
    ArrayList<Integer> stiList;
    ArrayList<Integer> stiList2;
    StiAdapter stiAdapter;
    StiAdapter2 stiAdapter2;
    ImageView btnAdd;

    int DEFAULT_TIME = 750;
    int DEFAULT_TIME2 = 2870;
    private long lClickTime = 0;
    int count = 0;
    ImageView ivPre;
    private Animation animation;
    private StoreageCkPref storeageCkPref;
    private int resourceId;
    ConstraintLayout cvPreview, btnShare;
    public static int listSet = 0;
    public static int listSet2 = 0;
    private boolean changed = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.frgment_custom_sticker, container, false);
        storeageCkPref = new StoreageCkPref(getContext());
        listView = rootView.findViewById(R.id.lisiview_sti);
        listView2 = rootView.findViewById(R.id.lisiview_sti2);
        btnSpin = rootView.findViewById(R.id.tvSpin);
        ivPre = rootView.findViewById(R.id.ivPre);
        tvClick = rootView.findViewById(R.id.tvClick);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        cvPreview = rootView.findViewById(R.id.cvPreview);
        btnShare = rootView.findViewById(R.id.btnShare);


        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lClickTime < 1000) {
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();
                spinn1();
                count++;
                if (count == 31) {
                    count = 0;
                }

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        setAdapater1();
        setAdapater2();


        listView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        listView2.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GogleAsKeboard.getInstance().showInterKeboa(getActivity(), new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(getContext(), CreateMoreStickerCsActivity.class);
                        startActivity(intent);
                    }
                });


            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sticker add to keyboard", Toast.LENGTH_LONG).show();
                storeageCkPref.addCs(resourceId);
                btnAdd.setVisibility(View.GONE);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShareDialog();
            }
        });
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(900);
        anim.setStartOffset(10);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tvClick.startAnimation(anim);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);


        return rootView;

    }

    private Drawable getRID(String name) {
        Resources resources = getResources();
        resourceId = resources.getIdentifier(name, "drawable",
                getContext().getPackageName());
        return resources.getDrawable(resourceId);
    }

    private void setAdapater1() {
        stiList = new ArrayList<>();
        stiList.add(R.drawable.csa1);
        stiList.add(R.drawable.csa2);
        stiList.add(R.drawable.csa3);
        stiList.add(R.drawable.csa4);
        stiList.add(R.drawable.csa5);
        stiList.add(R.drawable.csa1);
        stiList.add(R.drawable.csa2);
        stiList.add(R.drawable.csa3);
        stiList.add(R.drawable.csa4);
        stiList.add(R.drawable.csa5);
        stiList.add(R.drawable.csa1);
        stiList.add(R.drawable.csa2);
        stiList.add(R.drawable.csa3);
        stiList.add(R.drawable.csa4);
        stiList.add(R.drawable.csa5);
        stiList.add(R.drawable.csa1);
        stiList.add(R.drawable.csa2);
        stiList.add(R.drawable.csa3);
        stiList.add(R.drawable.csa4);
        stiList.add(R.drawable.csa5);
        stiList.add(R.drawable.csa1);
        stiList.add(R.drawable.csa2);
        stiList.add(R.drawable.csa3);
        stiList.add(R.drawable.csa4);
        stiList.add(R.drawable.csa5);
        stiList.add(R.drawable.csa1);
        stiList.add(R.drawable.csa2);
        stiList.add(R.drawable.csa3);
        stiList.add(R.drawable.csa4);
        stiList.add(R.drawable.csa5);


        stiAdapter = new StiAdapter(getContext(), stiList);
        listView.setAdapter(stiAdapter);


    }

    private void setAdapater2() {
        stiList2 = new ArrayList<>();
        stiList2.add(R.drawable.csb1);
        stiList2.add(R.drawable.csb2);
        stiList2.add(R.drawable.csb3);
        stiList2.add(R.drawable.csb4);
        stiList2.add(R.drawable.csb5);
        stiList2.add(R.drawable.csb6);
        stiList2.add(R.drawable.csb7);
        stiList2.add(R.drawable.csb8);
        stiList2.add(R.drawable.csb9);
        stiList2.add(R.drawable.csb10);
        stiList2.add(R.drawable.csb11);
        stiList2.add(R.drawable.csb12);
        stiList2.add(R.drawable.csb13);
        stiList2.add(R.drawable.csb14);
        stiList2.add(R.drawable.csb15);
        stiList2.add(R.drawable.csb1);
        stiList2.add(R.drawable.csb2);

        stiAdapter2 = new StiAdapter2(getContext(), stiList2);
        listView2.setAdapter(stiAdapter2);
        spinn();


    }

    private void spinn() {
        listView.setSelection(0);
        listView2.setSelection(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.bringToFront();


                listView.smoothScrollBy(500000, 40000);

                listView2.bringToFront();
                listView2.smoothScrollBy(500000, 40000);

            }
        }, 5);
    }

    private void spinn1() {
        listView.setSelection(0);
        listView2.setSelection(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.bringToFront();


                listView.smoothScrollBy(500000, 40000);


                listView2.bringToFront();

                listView2.smoothScrollBy(100000, 100000);
                setLoop(DEFAULT_TIME, getSetItem1(count));
                setLoop2(DEFAULT_TIME2, getSetItem1(count), getSetItem2(count));

            }
        }, 5);
    }


    private void setLoop(int defaultTime, int setItem1) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(setItem1);

            }
        }, defaultTime);

    }

    private void setLoop2(int defaultTime, int setItem1, int setItem2) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView2.setSelection(setItem2);
                setPreview(setItem1, setItem2);
            }
        }, defaultTime);

    }

    private void setPreview(int setItem1, int setItem2) {

        cvPreview.setVisibility(View.VISIBLE);
        setItem1++;
        setItem2++;

        int pos1 = stiList.get(setItem1);
        int pos2 = stiList2.get(setItem2);


        String name = getResources().getResourceEntryName(pos1);
        String name2 = getResources().getResourceEntryName(pos2);
        ivPre.setImageDrawable(getRID(name + name2));
        ivPre.startAnimation(animation);
        if (storeageCkPref.getCsList() != null && !storeageCkPref.getCsList().isEmpty()) {
            if (checkAdd(resourceId)) {
                btnAdd.setVisibility(View.GONE);
            } else {
                btnAdd.setVisibility(View.VISIBLE);
            }

        } else {
            btnAdd.setVisibility(View.VISIBLE);
        }

    }

    private boolean checkAdd(int resourceId) {
        boolean check = false;
        for (int i = 0; i < storeageCkPref.getCsList().size(); i++) {
            if (storeageCkPref.getCsList().get(i).equals(resourceId)) {
                check = true;
                break;
            }
        }

        return check;
    }

    private void openShareDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share_app, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);

        ImageView ivPreview = inflate.findViewById(R.id.ivSticker);
        ImageView ivBg = inflate.findViewById(R.id.ivBg);
        ImageView btnWaShare = inflate.findViewById(R.id.btnWaShare);
        ImageView btnCopy = inflate.findViewById(R.id.btnCopy);
        ImageView btnMore = inflate.findViewById(R.id.btnMore);
        ConstraintLayout ivFrame = inflate.findViewById(R.id.constraintLayout33);
        if (changed) {
            ivBg.setImageResource(R.drawable.ck_send_bg2);
            changed = false;
        } else {
            changed = true;
            ivBg.setImageResource(R.drawable.ck_send_bg);
        }
        ivPreview.setImageResource(resourceId);

        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();
            }
        });

        btnWaShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = setImage(ivFrame);
                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", file);

                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setPackage("com.whatsapp");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "App have not been installed.", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = setImage(ivFrame);

                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", file);

                Intent shareIntent = new Intent("android.intent.action.SEND");
                //  intent.putExtra("android.intent.extra.SUBJECT", string);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                startActivity(Intent.createChooser(shareIntent, "Share Image"));

            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence charSequence;

                String text = "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName();
                @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService("clipboard");

                clipboardManager.setPrimaryClip(ClipData.newPlainText("newtext", text));
                Toast.makeText(getContext(), "Copy to ClipBord", Toast.LENGTH_LONG).show();


            }
        });
        create1.show();


    }

    private File setImage(ConstraintLayout clFrame) {
        File file;
        File file2 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getContext().getFilesDir(), "custom_case");

        } else {
            file = new File(Environment.getExternalStorageDirectory(), "custom_case");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        try {


            file2 = new File(file, "IMG_Share" + ".jpg");


            clFrame.setDrawingCacheEnabled(true);
            Bitmap bitmapStamp = Bitmap.createBitmap(clFrame.getDrawingCache());

            FileOutputStream outputStream = new FileOutputStream(file2);
            bitmapStamp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            clFrame.setDrawingCacheEnabled(false);

        } catch (Throwable e) {

            e.printStackTrace();
        }

        return file2;
    }

    private int getSetItem1(int count) {

        int pos = 0;
        if (count == 1) {
            pos = 0;
        } else if (count == 2) {
            pos = 1;
        } else if (count == 3) {
            pos = 2;
        } else if (count == 4) {
            pos = 3;
        } else if (count == 5) {
            pos = 4;
        } else if (count == 6) {
            pos = 0;
        } else if (count == 7) {
            pos = 1;
        } else if (count == 8) {
            pos = 2;
        } else if (count == 9) {
            pos = 3;
        } else if (count == 10) {
            pos = 4;
        } else if (count == 11) {
            pos = 0;
        } else if (count == 12) {
            pos = 1;
        } else if (count == 13) {
            pos = 2;
        } else if (count == 14) {
            pos = 3;
        } else if (count == 15) {
            pos = 4;
        } else if (count == 16) {
            pos = 1;
        } else if (count == 17) {
            pos = 2;
        } else if (count == 18) {
            pos = 3;
        } else if (count == 19) {
            pos = 4;
        } else if (count == 20) {
            pos = 0;
        } else if (count == 21) {
            pos = 2;
        } else if (count == 22) {
            pos = 3;
        } else if (count == 23) {
            pos = 1;
        } else if (count == 24) {
            pos = 4;
        } else if (count == 25) {
            pos = 0;
        } else if (count == 26) {
            pos = 4;
        } else if (count == 27) {
            pos = 3;
        } else if (count == 28) {
            pos = 1;
        } else if (count == 29) {
            pos = 2;
        } else if (count == 30) {
            pos = 0;
        }

        listSet = pos + 1;
        return pos;


    }

    private int getSetItem2(int count) {

        int pos = 0;
        if (count == 1) {
            pos = 14;
        } else if (count == 2) {
            pos = 13;
        } else if (count == 3) {
            pos = 12;
        } else if (count == 4) {
            pos = 11;
        } else if (count == 5) {
            pos = 10;
        } else if (count == 6) {
            pos = 9;
        } else if (count == 7) {
            pos = 8;
        } else if (count == 8) {
            pos = 7;
        } else if (count == 9) {
            pos = 6;
        } else if (count == 10) {
            pos = 5;
        } else if (count == 11) {
            pos = 4;
        } else if (count == 12) {
            pos = 3;
        } else if (count == 13) {
            pos = 2;
        } else if (count == 14) {
            pos = 1;
        } else if (count == 15) {
            pos = 0;
        } else if (count == 16) {
            pos = 0;
        } else if (count == 17) {
            pos = 2;
        } else if (count == 18) {
            pos = 4;
        } else if (count == 19) {
            pos = 6;
        } else if (count == 20) {
            pos = 8;
        } else if (count == 21) {
            pos = 13;
        } else if (count == 22) {
            pos = 10;
        } else if (count == 23) {
            pos = 12;
        } else if (count == 24) {
            pos = 14;
        } else if (count == 25) {
            pos = 1;
        } else if (count == 26) {
            pos = 3;
        } else if (count == 27) {
            pos = 5;
        } else if (count == 28) {
            pos = 7;
        } else if (count == 29) {
            pos = 9;
        } else if (count == 30) {
            pos = 11;
        }

        listSet2 = pos + 1;
        return pos;


    }


}

package com.test.testing12345.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.test.testing12345.R;
import com.test.testing12345.adapter.StiAdapter;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;

import java.util.ArrayList;

public class CusotmCsActivty extends Activity {

    ListView listView, listView2;
    TextView tvSpin;
    ArrayList<Integer> stiList;
    ArrayList<Integer> stiList2;
    StiAdapter stiAdapter;
    StiAdapter stiAdapter2;

    int DEFAULT_TIME = 750;
    int DEFAULT_TIME2 = 2870;
    private long lClickTime =0;
    int count = 0;
    ImageView ivPre;
    private Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_cus);

        listView = findViewById(R.id.lisiview_sti);
        listView2 = findViewById(R.id.lisiview_sti2);
        tvSpin = findViewById(R.id.tvSpin);
        ivPre = findViewById(R.id.ivPre);

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.banerAllShowKeboa(admobNativeLarge2, CusotmCsActivty.this , c);


        tvSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lClickTime < 1000){
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();
                spinn1();
                count++;
                if(count==31){
                    count=0;
                }
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
          animation = AnimationUtils.loadAnimation(this, R.anim.bounce);

    }

    private Drawable getRID(String name) {
        Resources resources = getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                getPackageName());
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


        stiAdapter = new StiAdapter(this, stiList);
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


        stiAdapter2 = new StiAdapter(this, stiList2);
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
                setLoop(DEFAULT_TIME,getSetItem1(count),getSetItem2(count));
                setLoop2(DEFAULT_TIME2,getSetItem1(count),getSetItem2(count));

            }
        }, 5);
    }



    private void setLoop(int defaultTime, int setItem1, int setItem2) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(setItem1);

            }
        }, defaultTime);

    } private void setLoop2(int defaultTime, int setItem1, int setItem2) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 listView2.setSelection(setItem2);
                setPreview(setItem1,setItem2);
            }
        }, defaultTime);

    }

    private void setPreview(int setItem1, int setItem2) {
        setItem1++;
        setItem2++;

        int pos1= stiList.get(setItem1);
        int pos2= stiList2.get(setItem2);


        String name = getResources().getResourceEntryName(pos1);
        String name2 = getResources().getResourceEntryName(pos2);


       ivPre.setImageDrawable(getRID(name+name2));
        ivPre.startAnimation(animation);


    }

    private int getSetItem1(int count) {

        int pos=0;
        if(count==1){
            pos=0;
        }else if(count==2){
            pos=1;
        }else if(count==3){
            pos=2;
        }else if(count==4){
            pos=3;
        }else if(count==5){
            pos=4;
        } else if(count==6){
            pos=0;
        }else if(count==7){
            pos=1;
        }else if(count==8){
            pos=2;
        }else if(count==9){
            pos=3;
        } else if(count==10){
            pos=4;
        }else if(count==11){
            pos=0;
        }else if(count==12){
            pos=1;
        }else if(count==13){
            pos=2;
        } else if(count==14){
            pos=3;
        }else if(count==15){
            pos=4;
        }else if(count==16){
            pos=1;
        }else if(count==17){
            pos=2;
        } else if(count==18){
            pos=3;
        }else if(count==19){
            pos=4;
        }else if(count==20){
            pos=0;
        }else if(count==21){
            pos=2;
        } else if(count==22){
            pos=3;
        }else if(count==23){
            pos=1;
        }else if(count==24){
            pos=4;
        }else if(count==25){
            pos=0;
        }else if(count==26){
            pos=4;
        }   else if(count==27){
            pos=3;
        }else if(count==28){
            pos=1;
        }else if(count==29){
            pos=2;
        }else if(count==30){
            pos=0;
        }
        return  pos;


    }
    private int getSetItem2(int count) {

        int pos=0;
        if(count==1){
            pos=14;
        }else if(count==2){
            pos=13;
        }else if(count==3){
            pos=12;
        }else if(count==4){
            pos=11;
        }else if(count==5){
            pos=10;
        } else if(count==6){
            pos=9;
        }else if(count==7){
            pos=8;
        }else if(count==8){
            pos=7;
        }else if(count==9){
            pos=6;
        } else if(count==10){
            pos=5;
        }else if(count==11){
            pos=4;
        }else if(count==12){
            pos=3;
        }else if(count==13){
            pos=2;
        } else if(count==14){
            pos=1;
        }
        else if(count==15){
            pos=0;
        }

        else if(count==16){
            pos=0;
        }else if(count==17){
            pos=2;
        } else if(count==18){
            pos=4;
        }else if(count==19){
            pos=6;
        }else if(count==20){
            pos=8;
        }else if(count==21){
            pos=13;
        } else if(count==22){
            pos=10;
        }else if(count==23){
            pos=12;
        }else if(count==24){
            pos=14;
        }else if(count==25){
            pos=1;
        }else if(count==26){
            pos=3;
        } else if(count==27){
            pos=5;
        }else if(count==28){
            pos=7;
        }else if(count==29){
            pos=9;
        }else if(count==30){
            pos=11;
        }
        return  pos;


    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(CusotmCsActivty.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }
 }

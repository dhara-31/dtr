package com.si_charginganimation.nilesh_charginganimation.act;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DBettryplication;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DetailAds;


import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends AppCompatActivity {


    RecyclerView rvAdListView;
    TextView textView_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_app);


        rvAdListView = findViewById(R.id.recycler_app_list);
        textView_no_data = findViewById(R.id.textView_no_data);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        ImageView btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {

            ArrayList<DetailAds> adArrayList = DBettryplication.getInstance().getAdsDetails();

            if (adArrayList == null || adArrayList.size() == 0) {
                rvAdListView.setVisibility(View.GONE);
                textView_no_data.setVisibility(View.VISIBLE);
            } else {
                rvAdListView.setVisibility(View.VISIBLE);
                textView_no_data.setVisibility(View.GONE);
                MoviesAdapter mAdapter = new MoviesAdapter(adArrayList);
                LinearLayoutManager ampLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                rvAdListView.setLayoutManager(ampLayoutManager);
                rvAdListView.setItemAnimator(new DefaultItemAnimator());
                rvAdListView.setAdapter(mAdapter);
            }
        }catch (Exception e){

        }

    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private List<DetailAds> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView ivAppLogo;
            TextView tvAppName;
            ConstraintLayout constraintLayout;

            public MyViewHolder(View view) {
                super(view);
                ivAppLogo = view.findViewById(R.id.ivLogo);
                tvAppName = view.findViewById(R.id.tvName);
                constraintLayout = view.findViewById(R.id.constraintLayout5);
            }
        }

        public MoviesAdapter(List<DetailAds> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_more_app, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final DetailAds movie = moviesList.get(position);
            Glide.with(MoreActivity.this).load(movie.getApp_icon()).into(holder.ivAppLogo);
            holder.tvAppName.setText(movie.getApp_name());

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToMarket = new Intent("android.intent.action.VIEW", Uri.parse(movie.getApp_url()));
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(movie.getApp_url())));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }


    public void onBackPressed() {
        finish();
    }

}
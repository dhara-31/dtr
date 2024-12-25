package com.test.testing12345.adsclass;

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
import com.test.testing12345.R;


import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends AppCompatActivity {


    RecyclerView rvAdListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_app);


        rvAdListView = findViewById(R.id.recycler_app_list);
        ArrayList<DetailAds> adArrayList = DKeboaApplication.getInstance().getAdsDetails();


        if (adArrayList == null || adArrayList.size() == 0) {
            rvAdListView.setVisibility(View.GONE);
        } else {
            rvAdListView.setVisibility(View.VISIBLE);
            MoviesAdapter mAdapter = new MoviesAdapter(adArrayList);
            rvAdListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvAdListView.setItemAnimator(new DefaultItemAnimator());
            rvAdListView.setAdapter(mAdapter);
        }
    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private List<DetailAds> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView ivAppLogo;
            TextView tvAppName;
            ConstraintLayout cl;

            public MyViewHolder(View view) {
                super(view);
                ivAppLogo = view.findViewById(R.id.ivLogo);
                tvAppName = view.findViewById(R.id.tvName);
                cl = view.findViewById(R.id.cl);
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

            holder.cl.setOnClickListener(new View.OnClickListener() {
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
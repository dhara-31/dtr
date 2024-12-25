package com.test.testing12345.frgment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.penfeizhou.animation.loader.ResourceStreamLoader;
import com.github.penfeizhou.animation.webp.WebPDrawable;
import com.test.testing12345.R;
import com.test.testing12345.activity.AllStickerAddCsActivity;

public class ThirdPreFragmentCk extends Fragment {

    private ImageView ivTheme;
    private int themeId=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View rootView = inflater.inflate(R.layout.frgment_third_pre,
                container, false);


        ivTheme = rootView.findViewById(R.id.ivTheme);

        ivTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllStickerAddCsActivity.class);
                intent.putExtra("set",0);
                startActivity(intent);
            }
        });



        return rootView;
    }

    private void setData() {


        if(themeId==0){

            ResourceStreamLoader resourceLoader = new ResourceStreamLoader(getContext(), R.raw.as1);
            WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
            ivTheme.setImageDrawable(webpDrawable);
            themeId++;
        }else if (themeId==1){
            ResourceStreamLoader resourceLoader = new ResourceStreamLoader(getContext(), R.raw.as2);
            WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
            ivTheme.setImageDrawable(webpDrawable);
            themeId++;
        }else if (themeId==2){
            ResourceStreamLoader resourceLoader = new ResourceStreamLoader(getContext(), R.raw.as3);
            WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
            ivTheme.setImageDrawable(webpDrawable);
            themeId++;
        }else if (themeId==3){
            ResourceStreamLoader resourceLoader = new ResourceStreamLoader(getContext(), R.raw.as4);
            WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
            ivTheme.setImageDrawable(webpDrawable);
            themeId++;
        }else if (themeId==4){
            ResourceStreamLoader resourceLoader = new ResourceStreamLoader(getContext(), R.raw.as5);
            WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
            ivTheme.setImageDrawable(webpDrawable);
            themeId++;
        }else if (themeId==5){
            ResourceStreamLoader resourceLoader = new ResourceStreamLoader(getContext(), R.raw.as6);
            WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
            ivTheme.setImageDrawable(webpDrawable);
            themeId=0;
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}

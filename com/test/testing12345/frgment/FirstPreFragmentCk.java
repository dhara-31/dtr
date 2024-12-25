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

import com.test.testing12345.R;
import com.test.testing12345.activity.ThemeCreateCsActivity;
import com.test.testing12345.adsclass.StoreageCkPref;

public class FirstPreFragmentCk extends Fragment {
    ImageView ivTheme,ivOverlay;
    StoreageCkPref storeageCkPref;
    private int themeId=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View rootView = inflater.inflate(R.layout.frgment_frist_pre,
                container, false);
        storeageCkPref = new StoreageCkPref(getContext());
        ivTheme = rootView.findViewById(R.id.ivTheme);
        ivOverlay = rootView.findViewById(R.id.ivOverlay);



        ivTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ThemeCreateCsActivity.class);
                intent.putExtra("set",1);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        SecondPreFragmentCk.setData();
        setData();
    }



    private void setData() {
//        final int themeId = KeyboardTheme.getKeyboardTheme(getContext()).mThemeId;

        if(themeId==0){
            ivTheme.setImageResource(R.drawable.keyboard_1);
                themeId++;

        }else if(themeId==1){


            ivTheme.setImageResource(R.drawable.keyboard_2);
            themeId++;
        }else if(themeId==2){


            ivTheme.setImageResource(R.drawable.keyboard_3);
            themeId++;
        }else if(themeId==3){


            ivTheme.setImageResource(R.drawable.keyboard_4);
            themeId++;
        }else if(themeId==4){


            ivTheme.setImageResource(R.drawable.keyboard_5);
            themeId++;
        }else if(themeId==5) {

            ivTheme.setImageResource(R.drawable.keyboard_6);
            themeId=0;
        }


    }


}

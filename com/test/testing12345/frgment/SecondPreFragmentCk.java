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

public class SecondPreFragmentCk extends Fragment {
    public static ImageView ivTheme;
    public static int count=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View rootView = inflater.inflate(R.layout.frgment_second_pre,
                container, false);

        ivTheme = rootView.findViewById(R.id.ivTheme);

        ivTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ThemeCreateCsActivity.class);
                intent.putExtra("set",0);
                startActivity(intent);
            }
        });

        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        setData();
    }




    public static void setData() {

        if(ivTheme !=null) {
            if (count == 0) {
                ivTheme.setImageResource(R.drawable.bg_1);
                count++;
            } else if (count == 1) {
                ivTheme.setImageResource(R.drawable.bg_5);
                count++;
            } else if (count == 2) {
                ivTheme.setImageResource(R.drawable.bg_3);
                count++;
            } else if (count == 3) {
                ivTheme.setImageResource(R.drawable.bg_4);
                count++;
            } else if (count == 4) {
                ivTheme.setImageResource(R.drawable.bg_2);
                count = 0;
            }
        }



    }
}
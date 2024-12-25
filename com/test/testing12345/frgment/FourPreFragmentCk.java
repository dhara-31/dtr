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
import com.test.testing12345.activity.AllStickerAddCsActivity;

public class FourPreFragmentCk extends Fragment {

    private ImageView ivTheme;
    private int themeId=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View rootView = inflater.inflate(R.layout.frgment_four_pre,
                container, false);


        ivTheme = rootView.findViewById(R.id.ivTheme);
        ivTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllStickerAddCsActivity.class);
                intent.putExtra("set",1);
                startActivity(intent);
            }
        });




        return rootView;
    }

    private void setData() {



    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}

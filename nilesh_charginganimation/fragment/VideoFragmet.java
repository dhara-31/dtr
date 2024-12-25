package com.si_charginganimation.nilesh_charginganimation.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.VideoCAAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoFragmet extends Fragment {

RecyclerView recyclerView;
TextView tvNoData;
VideoCAAdapter videoCAAdapter;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    @Nullable
     @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_video,
                container, false);



        recyclerView = rootView.findViewById(R.id.rvImage);
        tvNoData = rootView.findViewById(R.id.tvNoData);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());




        if(checkBcaPermissions()) {
            setData();
        }

        return rootView;
    }
    private boolean checkBcaPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            perDialog();

            return false;
        }
        return true;
    }
    private void perDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_permission, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(false);


        TextView tvOk = inflate.findViewById(R.id.textView_ok);
        TextView textView17 = inflate.findViewById(R.id.textView17);

        textView17.setText("GO Back And Restart");
        tvOk.setText("Ok");
        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView textView_cancle = inflate.findViewById(R.id.textView_cancle);
        textView_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();



            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();



            }
        });


        create1.show();
    }

    private void setData() {

        ArrayList<DataModel> imageList = getImagelist(getActivity());


        if(imageList.isEmpty())
        {
            recyclerView.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }else {
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if(videoCAAdapter != null){
            videoCAAdapter.setNewData(imageList);
        }else {
            videoCAAdapter = new VideoCAAdapter(getContext(),imageList);
            recyclerView.setAdapter(videoCAAdapter);

        }





    }

    @SuppressLint("Range")
    private ArrayList<DataModel> getImagelist(Context context) {
        ArrayList<DataModel> fileList = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(uri, null, null, null, "title ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {

                int height = 0;

                String data = null;
                DataModel dataModel = new DataModel();

                try {
                    data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dataModel.setUri(Uri.parse(data));
                dataModel.setFilePath(data);

                fileList.add(dataModel);


            } while (cursor.moveToNext());



            cursor.close();
        }

        return fileList;

    }
    @Override
    public void onResume() {
        super.onResume();
        if(videoCAAdapter !=null ){
            videoCAAdapter.notifyDataSetChanged();
        }
    }

}

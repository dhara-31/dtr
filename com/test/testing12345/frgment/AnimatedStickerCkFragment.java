package com.test.testing12345.frgment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.penfeizhou.animation.loader.ResourceStreamLoader;
import com.github.penfeizhou.animation.webp.WebPDrawable;
import com.test.testing12345.R;
import com.test.testing12345.adsclass.StoreageCkPref;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AnimatedStickerCkFragment extends Fragment {
    RecyclerView recyclerView;
    ConstraintLayout cvBtn,cvBtnshare;
    AdapterStia adapterStia;
    ImageView ivPreview;
    TextView tvBtn;
    StoreageCkPref storeageCkPref;
    private Integer integer ;
    boolean changed=false;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.frgment_animated_sticker,
                container, false);
        storeageCkPref = new StoreageCkPref(getContext());

        recyclerView = rootView.findViewById(R.id.recyclerSti);
        cvBtn = rootView.findViewById(R.id.cvBtn);
        ivPreview = rootView.findViewById(R.id.ivPreview);
        cvBtnshare = rootView.findViewById(R.id.cvBtnshare);
        tvBtn = rootView.findViewById(R.id.tvBtn);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);


        cvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!storeageCkPref.getSHOW_AS_LST()) {

                    Toast.makeText(getContext(), "All animated sticker add to keyboard", Toast.LENGTH_LONG).show();
                    storeageCkPref.setSHOW_AS_LST(true);
                    cvBtn.setBackground(getResources().getDrawable(R.drawable.dr_btn2));
                    tvBtn.setTextColor(Color.BLACK);
                }
            }
        });


        cvBtnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShareDialog();
            }
        });


        setButton();
        setAnimPreview(R.raw.as1);
        setDataInRV();

        return rootView;
    }

    private void openShareDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share_app, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);

        ImageView ivPreview = inflate.findViewById(R.id.ivSticker);
        ImageView btnWaShare = inflate.findViewById(R.id.btnWaShare);
        ImageView btnCopy = inflate.findViewById(R.id.btnCopy);
        ImageView btnMore = inflate.findViewById(R.id.btnMore);
        ImageView ivBg = inflate.findViewById(R.id.ivBg);
        ConstraintLayout ivFrame = inflate.findViewById(R.id.constraintLayout33);

        if(changed){
            ivBg.setImageResource(R.drawable.ck_send_bg2);
            changed=false;
        }else {
            changed=true;
            ivBg.setImageResource(R.drawable.ck_send_bg);
        }

        ivPreview.setImageResource(integer);

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
              File file =   setImage(ivFrame);
                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName()+".fileprovider", file);

                Intent shareIntent = new Intent("android.intent.action.SEND");
              //  intent.putExtra("android.intent.extra.SUBJECT", string);
                shareIntent.setPackage("com.whatsapp");
                 shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(),"App have not been installed.",Toast.LENGTH_LONG).show();
                }

            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file =   setImage(ivFrame);

                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName()+".fileprovider", file);

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

                    String text="Hey check out this app at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName();
                    @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService("clipboard");

                        clipboardManager.setPrimaryClip(ClipData.newPlainText("newtext",text));
                Toast.makeText(getContext(), "Copy to ClipBord", Toast.LENGTH_LONG).show();



            }
        });
        create1.show();



    }

    private File setImage(ConstraintLayout clFrame)

    {
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
    private void setButton() {
        if(storeageCkPref.getSHOW_AS_LST()){
           cvBtn.setBackground(getResources().getDrawable(R.drawable.dr_btn2));
            tvBtn.setTextColor(Color.BLACK);

        }

    }

    private void setAnimPreview(int as1) {
        integer = as1;
        ResourceStreamLoader resourceLoader = new ResourceStreamLoader(getContext(), as1);
        WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
        ivPreview.setImageDrawable(webpDrawable);

    }

    private void setDataInRV() {
        ArrayList<Integer> stiList = new ArrayList<>();

        stiList.add(R.raw.as1);
        stiList.add(R.raw.as2);
        stiList.add(R.raw.as3);
        stiList.add(R.raw.as4);
        stiList.add(R.raw.as5);
        stiList.add(R.raw.as6);
        stiList.add(R.raw.as7);
        stiList.add(R.raw.as8);
        stiList.add(R.raw.as9);
        stiList.add(R.raw.as10);
         stiList.add(R.raw.as12);
         stiList.add(R.raw.as15);




        adapterStia = new AdapterStia(getActivity(), stiList);
        recyclerView.setAdapter(adapterStia);


    }


    public class AdapterStia extends RecyclerView.Adapter<AdapterStia.ViewHolder> {

        Context context;
        ArrayList<Integer> listOfSti;
       int tempPos=0;

        public AdapterStia(Context context, ArrayList<Integer> gifList) {
            this.context = context;
            this.listOfSti = gifList;
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stia_item_layout, parent, false);
            return new ViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O) {


            ResourceStreamLoader resourceLoader = new ResourceStreamLoader(getContext(), listOfSti.get(position));
            WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
            holder.ivSti.setImageDrawable(webpDrawable);
        }else {
            holder.ivSti.setImageResource(listOfSti.get(position));
        }
            holder.ivSti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempPos =position;
                    setAnimPreview(listOfSti.get(position));
                notifyDataSetChanged();
                }
            });
            if(tempPos == position){
                holder.cvBg.setBackground(getResources().getDrawable(R.drawable.dr_bg2_border));
            }else {
                holder.cvBg.setBackground(getResources().getDrawable(R.drawable.dr_bg2));
            }
        }

        @Override
        public int getItemCount() {
            return listOfSti.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivSti;
            ConstraintLayout cvBg;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);

                ivSti = itemView.findViewById(R.id.tvSti);
                cvBg = itemView.findViewById(R.id.cvBg);
            }
        }

    }
 }

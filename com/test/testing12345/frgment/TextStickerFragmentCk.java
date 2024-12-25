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

import com.bumptech.glide.Glide;
import com.test.testing12345.R;
import com.test.testing12345.adsclass.StoreageCkPref;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TextStickerFragmentCk extends Fragment {
    RecyclerView recyclerView;
    ConstraintLayout cvBtn,cvBtnshare;
    AdapterStia adapterStia;
    StoreageCkPref storeageCkPref;
    private int integer=R.raw.s1;
    private TextView tvBtn;
    private boolean changed=true;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View rootView = inflater.inflate(R.layout.frgment_text_sticker,
                container, false);
         storeageCkPref = new StoreageCkPref(getContext());

        recyclerView = rootView.findViewById(R.id.recyclerSti);
        cvBtn = rootView.findViewById(R.id.cvBtn);
        tvBtn = rootView.findViewById(R.id.tvBtn);
        cvBtnshare = rootView.findViewById(R.id.cvBtnshare);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);



         cvBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                if(!storeageCkPref.getSHOW_TS_LST()) {
                    Toast.makeText(getContext(), "All Text sticker add to keyboard", Toast.LENGTH_LONG).show();

                    storeageCkPref.setSHOW_TS_LST(true);
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
         setDataInRV();

        return rootView;
    }
    private void setButton() {
        if(storeageCkPref.getSHOW_TS_LST()){
            cvBtn.setBackground(getResources().getDrawable(R.drawable.dr_btn2));
            tvBtn.setTextColor(Color.BLACK);
        }

    }

    private void setDataInRV() {
        ArrayList<Integer> stiList = new ArrayList<>();

        stiList.add(R.raw.s1);
        stiList.add(R.raw.s2);
        stiList.add(R.raw.s3);
        stiList.add(R.raw.s4);
        stiList.add(R.raw.s5);
        stiList.add(R.raw.s6);
        stiList.add(R.raw.s7);
        stiList.add(R.raw.s8);
        stiList.add(R.raw.s9);
        stiList.add(R.raw.s10);
        stiList.add(R.raw.s11);
        stiList.add(R.raw.s12);
        stiList.add(R.raw.s13);
        stiList.add(R.raw.s14);
        stiList.add(R.raw.s15);
        stiList.add(R.raw.s16);




        adapterStia = new AdapterStia(getActivity(),stiList);
        recyclerView.setAdapter(adapterStia);





    }


    public  class AdapterStia extends RecyclerView.Adapter<AdapterStia.ViewHolder> {

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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.textt_stia_item_layout, parent, false);
            return new  ViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(@NonNull @NotNull  ViewHolder holder, int position) {
           //  holder.ivSti.setImageResource(listOfSti.get(position));
             Glide.with(context).load(listOfSti.get(position)).into(holder.ivSti);

            holder.ivSti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    integer=listOfSti.get(position);

                    openPerDialog(listOfSti.get(position));
                    tempPos=position;
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

    private void openPerDialog(Integer integer) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_per_stiker, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);

        ImageView ivPreview = inflate.findViewById(R.id.ivPreview);

        ivPreview.setImageResource(integer);

        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();




            }
        });


        create1.show();


    }
    @SuppressLint("ResourceType")
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

}

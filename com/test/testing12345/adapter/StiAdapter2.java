package com.test.testing12345.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.test.testing12345.R;
import com.test.testing12345.frgment.CustonmStickerCkFragment;

import java.util.ArrayList;

public class StiAdapter2 extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> arrayList;
    ImageView imageView_app_icon;

    public StiAdapter2(Context context, ArrayList<Integer> userTaskInfos) {
        this.context = context;
        this.arrayList = userTaskInfos;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        view = LayoutInflater.from(context).inflate(R.layout.cus_sti_item_layout, viewGroup, false);
        imageView_app_icon = view.findViewById(R.id.tvSti);
        imageView_app_icon.setImageResource(arrayList.get(i));

        if(CustonmStickerCkFragment.listSet2 == i){
            imageView_app_icon.setAlpha(1f);
        }else {
            imageView_app_icon.setAlpha(0.6f);

        }
        return view;
    }
}

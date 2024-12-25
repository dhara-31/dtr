package com.test.testing12345.frgment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.Intro3CsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ThirdFragmentCk extends Fragment {
    public static LanAdapter lanAdapter;
    RecyclerView recyclerView;

    public static void setData() {
        lanAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View rootView = inflater.inflate(R.layout.frgment_third
                ,
                container, false);




        recyclerView = rootView.findViewById(R.id.recyclerLan);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);


        setLan();

        return rootView;
    }

    private void setLan() {
        ArrayList<String> stringArrayList = new ArrayList<>();


        stringArrayList.add("Vietnamese");
        stringArrayList.add("Romanian");
        stringArrayList.add("Serbian");
        stringArrayList.add("Malay");
        stringArrayList.add("Kannada");
        stringArrayList.add("Kyrgyz");






        lanAdapter = new LanAdapter(getContext(),stringArrayList);
        recyclerView.setAdapter(lanAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();

      }

    private class LanAdapter extends RecyclerView.Adapter<LanAdapter.ViewHolder>{

        Context context;

        ArrayList<String> adArrayList;
        public LanAdapter(Context context, ArrayList<String> stringArrayList) {

            this.context = context;
            this.adArrayList = stringArrayList;
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lan_item_layout, parent, false);
            return new LanAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            holder.tvLan.setText(adArrayList.get(position));


            holder.tvLan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intro3CsActivity.selectedLan = adArrayList.get(position);
                    notifyDataSetChanged();
                }
            });


            if(Intro3CsActivity.selectedLan.equals(adArrayList.get(position))){
                holder.cv1.setBackground(getResources().getDrawable(R.drawable.dr_tv_bg_s));
            }else {
                holder.cv1.setBackground(getResources().getDrawable(R.drawable.dr_tv_bg));

            }
        }

        @Override
        public int getItemCount() {
            return adArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvLan;
            ConstraintLayout cv1;
            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                tvLan = itemView.findViewById(R.id.tv1);
                cv1 = itemView.findViewById(R.id.cv1);

            }
        }
    }

}

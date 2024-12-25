package com.test.testing12345.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.StylishFontCkModel;
import com.test.testing12345.other.SelectingCkFontStyle;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterKeyFontStyle extends RecyclerView.Adapter<AdapterKeyFontStyle.ViewHolder> {
     Context context;
    String locale;
    ArrayList<StylishFontCkModel> stylishFontCkModelArrayList;
    View view;
    OnItemClickListener1 onItemClickListener1;

    public AdapterKeyFontStyle(Context fontStyleActivity, ArrayList<StylishFontCkModel> fetchingJsonFromAssets) {
        this.context = fontStyleActivity;
        this.stylishFontCkModelArrayList = fetchingJsonFromAssets;

    }

    @NonNull
     @Override
    public ViewHolder onCreateViewHolder(@NonNull   ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(this.context).inflate(R.layout.layout_recycler_key_font_style, parent, false);
         return new ViewHolder(this.view);    }




    @Override
    public void onBindViewHolder(@NonNull   AdapterKeyFontStyle.ViewHolder holder, int position) {
        if (SelectingCkFontStyle.getSelectedFont(this.context).getFontStyleName().equals(this.stylishFontCkModelArrayList.get(position).getFontStyleName())) {
            holder.text_font_style_name.setTextColor(-1);
            holder.selected_font_card.setCardBackgroundColor(this.context.getResources().getColor(R.color.colorSelectedCard));
        } else {
            holder.text_font_style_name.setTextColor(this.context.getResources().getColor(R.color.black));
            holder.selected_font_card.setCardBackgroundColor(this.context.getResources().getColor(R.color.key_text_inactive_color_lxx_dark));
        }
        holder.text_font_style_name.setText(this.stylishFontCkModelArrayList.get(position).getStyledString("font"));
    }


    @Override
    public int getItemCount() {
        return stylishFontCkModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView selected_font_card;
        TextView text_font_style_name;
        public ViewHolder(@NonNull   View itemView) {
            super(itemView);
            this.selected_font_card = (CardView) view.findViewById(R.id.selected_font_card);
            this.text_font_style_name = (TextView) view.findViewById(R.id.text_font_style_name);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectingCkFontStyle.setSelectedFont(AdapterKeyFontStyle.this.context, AdapterKeyFontStyle.this.stylishFontCkModelArrayList, ViewHolder.this.getAdapterPosition());
                    try {
                        onItemClickListener1.onItemClick1(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AdapterKeyFontStyle.this.notifyDataSetChanged();
                }
            });
        }
    }
    public void setOnItemClickListener(OnItemClickListener1 onItemClickListener1) {
        this.onItemClickListener1 = (OnItemClickListener1) onItemClickListener1;
    }
    public interface OnItemClickListener1 {
        void onItemClick1(View v) throws IOException;
    }
}

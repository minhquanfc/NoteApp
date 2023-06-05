package com.example.noteapp.userUi.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;

public class TaiLieuHolder extends RecyclerView.ViewHolder {
    TextView title_item_tailieu,des_item_tailieu;
    ImageView img_item_tailieu;
    LinearLayout cardview_item_tailieu;
    public TaiLieuHolder(@NonNull View itemView) {
        super(itemView);
        title_item_tailieu = itemView.findViewById(R.id.title_item_tailieu);
        des_item_tailieu = itemView.findViewById(R.id.des_item_tailieu);
        img_item_tailieu = itemView.findViewById(R.id.img_item_tailieu);
        cardview_item_tailieu = itemView.findViewById(R.id.cardview_item_tailieu);
    }
}

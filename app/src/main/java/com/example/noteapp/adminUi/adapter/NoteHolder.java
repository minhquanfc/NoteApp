package com.example.noteapp.adminUi.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;

public class NoteHolder extends RecyclerView.ViewHolder {

    TextView title_item_note,des_item_note;
    ImageView img_item_note;
    LinearLayout cardview_item_note;
    ImageView btn_edit_note,btn_delete_note;
    public NoteHolder(@NonNull View itemView) {
        super(itemView);
        cardview_item_note = itemView.findViewById(R.id.cardview_item_note);
        img_item_note = itemView.findViewById(R.id.img_item_note);
        title_item_note = itemView.findViewById(R.id.title_item_note);
        des_item_note = itemView.findViewById(R.id.des_item_note);
        btn_edit_note = itemView.findViewById(R.id.btn_edit_note);
        btn_delete_note = itemView.findViewById(R.id.btn_delete_note);

    }
}

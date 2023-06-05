package com.example.noteapp.adminUi.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;

public class UserHolder extends RecyclerView.ViewHolder {
    TextView name_user,role_user;
    ImageView img_item_user;
    ImageView btn_edit_user,btn_delete_user;
    public UserHolder(@NonNull View itemView) {
        super(itemView);
        name_user = itemView.findViewById(R.id.name_user);
        role_user = itemView.findViewById(R.id.role_user);
        img_item_user = itemView.findViewById(R.id.img_item_user);
        btn_edit_user = itemView.findViewById(R.id.btn_edit_user);
        btn_delete_user = itemView.findViewById(R.id.btn_delete_user);
    }
}

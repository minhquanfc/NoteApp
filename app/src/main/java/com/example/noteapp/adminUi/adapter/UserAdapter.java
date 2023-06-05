package com.example.noteapp.adminUi.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.noteapp.R;
import com.example.noteapp.adminUi.EditNoteActivity;
import com.example.noteapp.adminUi.EditUserActivity;
import com.example.noteapp.interFace.ItemOnClickUser;
import com.example.noteapp.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
    Context context;
    List<Users> usersList;
    private ItemOnClickUser mListener;


    public UserAdapter(Context context, List<Users> usersList, ItemOnClickUser mListener) {
        this.context = context;
        this.usersList = usersList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, @SuppressLint("RecyclerView") int position) {
        Users users = usersList.get(position);
        Glide.with(context).load(users.getAvt()).into(holder.img_item_user);
        holder.name_user.setText(users.getName());
        holder.role_user.setText(users.getRole());
        holder.btn_edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditUserActivity.class);
                intent.putExtra("id",usersList.get(position).getuId());
                context.startActivity(intent);
            }
        });
        holder.btn_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(usersList.get(position).getuId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}

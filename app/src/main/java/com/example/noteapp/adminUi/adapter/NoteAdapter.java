package com.example.noteapp.adminUi.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.noteapp.R;
import com.example.noteapp.adminUi.EditNoteActivity;
import com.example.noteapp.adminUi.ViewPDFActivity;
import com.example.noteapp.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
    Context context;
    List<Note> noteList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Notes");
    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note,parent,false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, @SuppressLint("RecyclerView") int position) {
        Note note = noteList.get(position);
        Glide.with(context).load(note.getImg()).into(holder.img_item_note);
        holder.title_item_note.setText(note.getTitle());
        holder.des_item_note.setText(note.getDes());
        holder.title_item_note.setMaxLines(1);
        holder.des_item_note.setMaxLines(2);
        holder.cardview_item_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewPDFActivity.class);
                intent.putExtra("title",noteList.get(position).getTitle());
                intent.putExtra("linkPdf",noteList.get(position).getFile());
                context.startActivity(intent);
            }
        });
        holder.btn_delete_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Bạn có muốn xóa không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference.child(note.getId()).removeValue();
                        noteList.remove(position);
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        holder.btn_edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditNoteActivity.class);
                intent.putExtra("id",noteList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}

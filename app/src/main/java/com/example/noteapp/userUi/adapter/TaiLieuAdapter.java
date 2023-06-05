package com.example.noteapp.userUi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.noteapp.R;
import com.example.noteapp.adminUi.ViewPDFActivity;
import com.example.noteapp.adminUi.adapter.NoteHolder;
import com.example.noteapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class TaiLieuAdapter extends RecyclerView.Adapter<TaiLieuHolder> {
    Context context;
    List<Note> noteList;
    private List<Note> filteredList;

    public TaiLieuAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
//        this.filteredList = new ArrayList<>(noteList); // Sao chép danh sách để lọc
    }

    @NonNull
    @Override
    public TaiLieuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tailieu,parent,false);
        return new TaiLieuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaiLieuHolder holder, @SuppressLint("RecyclerView") int position) {
        Note note = noteList.get(position);
        Glide.with(context).load(note.getImg()).into(holder.img_item_tailieu);
        holder.title_item_tailieu.setText(note.getTitle());
        holder.des_item_tailieu.setText(note.getDes());
        holder.title_item_tailieu.setMaxLines(1);
        holder.des_item_tailieu.setMaxLines(2);
        holder.cardview_item_tailieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewPDFActivity.class);
                intent.putExtra("linkPdf",noteList.get(position).getFile());
                intent.putExtra("title",noteList.get(position).getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
//    public List<Note> search(String query) {
//        filteredList.clear(); // Xoá dữ liệu trong danh sách lọc hiện tại
//
//        if (query.isEmpty()) {
//            filteredList.addAll(noteList); // Nếu nội dung tìm kiếm rỗng, trả về toàn bộ danh sách
//        } else {
//            for (Note person : noteList) {
//                if (person.getTitle().toLowerCase().contains(query.toLowerCase())) {
//                    filteredList.add(person);
//                }
//            }
//        }
//        return filteredList;
//    }
}

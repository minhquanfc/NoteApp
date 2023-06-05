package com.example.noteapp.userUi.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;
import com.example.noteapp.adminUi.NoteActivity;
import com.example.noteapp.adminUi.adapter.NoteAdapter;
import com.example.noteapp.model.Note;
import com.example.noteapp.userUi.adapter.TaiLieuAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ImageButton btn_img_noti;
    RecyclerView rc_view_tailieu;
    FirebaseDatabase database;
    DatabaseReference mRef;
    List<Note> noteList;
    TaiLieuAdapter adapter;
    EditText search_edit_text;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btn_img_noti = view.findViewById(R.id.btn_img_noti);
        rc_view_tailieu = view.findViewById(R.id.rc_view_tailieu);
        search_edit_text = view.findViewById(R.id.search_edit_text);


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Notes");
        noteList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rc_view_tailieu.setLayoutManager(linearLayoutManager);
        getData();
        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = search_edit_text.getText().toString().trim();
                List<Note> filteredList = new ArrayList<>();
                filteredList.clear();
                for (Note item : noteList) {
                    if (item.getTitle().toLowerCase().contains(query.toString().toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                adapter = new TaiLieuAdapter(getContext(), filteredList);
                rc_view_tailieu.setAdapter(adapter);
//                if (query.isEmpty()) {
//                    getData();
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void getData() {
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Note note = snapshot.getValue(Note.class);
                if (note != null) {
                    noteList.add(note);
                    adapter = new TaiLieuAdapter(getContext(), noteList);
                    rc_view_tailieu.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Note note = snapshot.getValue(Note.class);
                if (note == null || noteList == null || noteList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < noteList.size(); i++) {
                    if (note.getId() == noteList.get(i).getId()) {
                        noteList.set(i, note);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Note note = snapshot.getValue(Note.class);
                noteList.remove(note);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
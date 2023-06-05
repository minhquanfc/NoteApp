package com.example.noteapp.adminUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.noteapp.R;
import com.example.noteapp.adminUi.adapter.NoteAdapter;
import com.example.noteapp.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton imageButton;
    FloatingActionButton btn_floating_add;
    FirebaseDatabase database;
    DatabaseReference mRef;
    List<Note> noteList;
    NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        recyclerView = findViewById(R.id.rc_view_note);
        imageButton = findViewById(R.id.btnback_note);
        btn_floating_add = findViewById(R.id.btn_floating_add);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Notes");
//        Map<String,Object> map = new HashMap<>();
//        String key = mRef.push().getKey();
//        map.put("id",key);
//        map.put("title","aaa");
//        map.put("des","test");
//        map.put("img","aaa");
//        map.put("file","aaa");
//        map.put("time","aaa");
//
//        mRef.child(key).setValue(map);
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(NoteActivity.this, noteList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NoteActivity.this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getData();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this,AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Note note = snapshot.getValue(Note.class);
                if (note != null) {
                    noteList.add(note);
                    Log.d("aaa","a: "+note)
;                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Note note = snapshot.getValue(Note.class);
                if (note == null || noteList == null || noteList.isEmpty()){
                    return;
                }
                for (int i =0; i<noteList.size(); i++) {
                    if (note.getId() == noteList.get(i).getId()) {
                        noteList.set(i,note);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
package com.example.noteapp.adminUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.adminUi.adapter.NoteAdapter;
import com.example.noteapp.adminUi.adapter.UserAdapter;
import com.example.noteapp.interFace.ItemOnClickUser;
import com.example.noteapp.model.Note;
import com.example.noteapp.model.Users;
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

public class UsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton imageButton;
    FloatingActionButton btn_floating_add;
    FirebaseDatabase database;
    DatabaseReference mRef;
    List<Users> usersList;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        recyclerView = findViewById(R.id.rc_view_users);
        imageButton = findViewById(R.id.btnback_users);
        btn_floating_add = findViewById(R.id.btn_floating_add_user);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");

        usersList = new ArrayList<>();
        adapter = new UserAdapter(UsersActivity.this, usersList,
                new ItemOnClickUser() {
                    @Override
                    public void onClick(String position) {
                        Toast.makeText(UsersActivity.this, "Users: " + position, Toast.LENGTH_SHORT).show();
                        mRef.child(position).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users users1 = snapshot.getValue(Users.class);
                                if (users1 != null) {
                                    boolean mokhoa;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                                    if (users1.isActive()) {
                                        builder.setTitle("Bạn có muốn khoá không?");
                                        mokhoa = false;
                                    } else {
                                        builder.setTitle("Bạn có muốn mở khoá không?");
                                        mokhoa = true;
                                    }
                                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("active", mokhoa);
                                            mRef.child(position).updateChildren(map);
                                        }
                                    });
                                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UsersActivity.this, RecyclerView.VERTICAL, false);
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
                Intent intent = new Intent(UsersActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getData() {
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Users users = snapshot.getValue(Users.class);
                if (users != null) {
                    usersList.add(users);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Users users = snapshot.getValue(Users.class);
                if (users == null || usersList == null || usersList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < usersList.size(); i++) {
                    if (users.getuId() == usersList.get(i).getuId()) {
                        usersList.set(i, users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                usersList.remove(users);
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
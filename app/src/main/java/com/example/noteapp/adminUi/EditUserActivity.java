package com.example.noteapp.adminUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.activity.SignUpActivity;
import com.example.noteapp.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditUserActivity extends AppCompatActivity {
    EditText ed_name_user, ed_email_user;
    FirebaseDatabase database;
    DatabaseReference mRef;
    String idUser;
    private ProgressDialog progressDialog;
    Button btn_edit_user;
    RadioButton radioAdmin, radioUser;
    ImageButton imageButton;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        btn_edit_user = findViewById(R.id.btn_edit_user);
        ed_name_user = findViewById(R.id.ed_name_user);
        ed_email_user = findViewById(R.id.ed_email_user);
        radioAdmin = findViewById(R.id.radio_Admin);
        radioUser = findViewById(R.id.radio_User);

        imageButton = findViewById(R.id.btnback_edit_user);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng chờ...");
        Intent intent = getIntent();
        idUser = intent.getStringExtra("id");
        getData();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                updateData();
            }
        });
    }

    private void updateData() {
        if (ed_name_user.getText().toString().isEmpty()) {
            Toast.makeText(EditUserActivity.this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
            ed_name_user.requestFocus();
            return;
        } else if (ed_email_user.getText().toString().isEmpty()) {
            Toast.makeText(EditUserActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            ed_email_user.requestFocus();
            return;
        }else if (!validateEmail(ed_email_user.getText().toString().trim())) {
            Toast.makeText(EditUserActivity.this, "Vui lòng nhập email đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (radioAdmin.isChecked()) {
                role = "admin";
            } else {
                role = "user";
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", ed_name_user.getText().toString().trim());
            map.put("email", ed_email_user.getText().toString().trim());
            map.put("role", role);
            mRef.child(idUser).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        UpdateEmail();
                        progressDialog.dismiss();
                        Toast.makeText(EditUserActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditUserActivity.this, "Sửa không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void UpdateEmail() {
        String email = ed_email_user.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getData() {
        progressDialog.show();
        mRef.child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (users != null) {
                    ed_email_user.setText(users.getEmail());
                    ed_name_user.setText(users.getName());
                    progressDialog.dismiss();
                    Log.e("aaa","role "+ users.getRole());
                    if (users.getRole().equalsIgnoreCase("admin")) {
                        radioAdmin.setChecked(true);
                    } else {
                        radioUser.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
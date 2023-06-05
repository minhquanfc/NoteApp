package com.example.noteapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.adminUi.AdminMainActivity;
import com.example.noteapp.model.Users;
import com.example.noteapp.userUi.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText ed_email_login, ed_pass_login;
    TextView txtQuenPass, txt_signup_login;
    Button btn_login;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference mref;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = findViewById(R.id.btn_login);
        ed_email_login = findViewById(R.id.ed_email_login);
        ed_pass_login = findViewById(R.id.ed_pass_login);
        txtQuenPass = findViewById(R.id.txtQuenPass);
        txt_signup_login = findViewById(R.id.txt_signup_login);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mref = database.getReference("Users");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng chờ...");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_email_login.getText().toString().trim();
                String pass = ed_pass_login.getText().toString().trim();
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!validateEmail(email)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getInfoUser();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        txt_signup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    private void getInfoUser() {
        String idUser = auth.getCurrentUser().getUid();
        mref.child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (users != null) {
                    if (users.isActive()) {
                        if (users.role.equalsIgnoreCase("admin")) {
                            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                            startActivity(intent);
                        }
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Tài khoản đã bị khóa.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
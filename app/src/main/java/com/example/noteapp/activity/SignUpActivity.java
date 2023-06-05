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
import com.example.noteapp.userUi.PasswordActivity;
import com.example.noteapp.userUi.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText ed_name_sign_up, ed_email_sign_up, ed_pass_sign_up;
    Button btn_sign_up;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;


    private ProgressDialog progressDialog;
    TextView txt_signup_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        ed_name_sign_up = findViewById(R.id.ed_name_sign_up);
        ed_email_sign_up = findViewById(R.id.ed_email_sign_up);
        ed_pass_sign_up = findViewById(R.id.ed_pass_sign_up);
        txt_signup_login = findViewById(R.id.txt_signup_login);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        progressDialog.setMessage("Vui lòng chờ...");

        txt_signup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = ed_name_sign_up.getText().toString().trim();
                String email = ed_email_sign_up.getText().toString().trim();
                String pass = ed_pass_sign_up.getText().toString().trim();
                if ( name.isEmpty() ||email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!validateEmail(email)) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập email đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu trên 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("uId", mAuth.getCurrentUser().getUid());
                            map.put("name", name);
                            map.put("email", email);
                            map.put("passWord", pass);
                            map.put("role", "user");
                            map.put("active", true);
                            map.put("avt","https://firebasestorage.googleapis.com/v0/b/noteapp-9a1b9.appspot.com/o/1680011907133.jpg?alt=media&token=e17b4895-065b-40fd-a0de-02c491a2cbcf");
                            myRef.child(mAuth.getCurrentUser().getUid()).setValue(map);
                            Intent intent = new Intent(SignUpActivity.this, UserMainActivity.class);
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
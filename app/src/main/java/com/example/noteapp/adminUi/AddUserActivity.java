package com.example.noteapp.adminUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.activity.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AddUserActivity extends AppCompatActivity {

    ImageButton imageButton;
    Button btn_add;
    EditText name,email,pass;
    RadioButton rd_admin,rd_user;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ProgressDialog progressDialog;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        imageButton = findViewById(R.id.btnback_add_user);
        btn_add = findViewById(R.id.btn_add_user);
        name = findViewById(R.id.ed_name_add_user);
        email = findViewById(R.id.ed_email_add_user);
        pass = findViewById(R.id.ed_pass_add_user);
        rd_admin = findViewById(R.id.radio_Admin_add_user);
        rd_user = findViewById(R.id.radio_User_add_user);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        progressDialog.setMessage("Vui lòng chờ...");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        String name1 = name.getText().toString().trim();
        String email1 = email.getText().toString().trim();
        String pass1 = pass.getText().toString().trim();
        if (name1.isEmpty() || email1.isEmpty() || pass1.isEmpty()){
            Toast.makeText(AddUserActivity.this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
            return;
        } else if (!validateEmail(email1)) {
            Toast.makeText(AddUserActivity.this, "Vui lòng nhập email đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        } else if (pass.length() < 6) {
            Toast.makeText(AddUserActivity.this, "Mật khẩu trên 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email1,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (rd_admin.isChecked()) {
                        role = "admin";
                    } else {
                        role = "user";
                    }
                    Toast.makeText(AddUserActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Map<String,Object> map = new HashMap<>();
                    map.put("uId",mAuth.getCurrentUser().getUid());
                    map.put("name",name1);
                    map.put("email",email1);
                    map.put("passWord",pass1);
                    map.put("role",role);
                    map.put("active",true);
                    map.put("avt","https://printgo.vn/uploads/file-logo/1/512x512.25154818607b12a506e35927d36d5649.ai.1.png");
                    myRef.child(mAuth.getCurrentUser().getUid()).setValue(map);
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(AddUserActivity.this, "Tạo tài khoản không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
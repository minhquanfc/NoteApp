package com.example.noteapp.userUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    EditText ed_pass_cu, ed_pass_moi, ed_nhaplai_pass_moi;
    Button btn_doipass;
    ImageButton btnback_doi_pass;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseAuth auth;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);
        ed_pass_cu = findViewById(R.id.ed_pass_cu);
        ed_pass_moi = findViewById(R.id.ed_pass_moi);
        ed_nhaplai_pass_moi = findViewById(R.id.ed_nhaplai_pass_moi);
        btn_doipass = findViewById(R.id.btn_doipass);
        btnback_doi_pass = findViewById(R.id.btnback_doi_pass);
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");
        auth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Vui lòng chờ...");

        mRef.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (users != null) {
                    password = users.getPassWord();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnback_doi_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_doipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passcu = ed_pass_cu.getText().toString().trim();
                String passmoi = ed_pass_moi.getText().toString().trim();
                String nhaplaipassmoi = ed_nhaplai_pass_moi.getText().toString().trim();

                if (passcu.isEmpty() || passmoi.isEmpty() || nhaplaipassmoi.isEmpty()) {
                    Toast.makeText(PasswordActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!passcu.equals(password)) {
                    Toast.makeText(PasswordActivity.this, "Mật khẩu nhập cũ không đúng", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passmoi.length() < 6) {
                    Toast.makeText(PasswordActivity.this, "Vui lòng nhập mật khẩu trên 6 số", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!nhaplaipassmoi.equals(passmoi)) {
                    Toast.makeText(PasswordActivity.this, "Mật khẩu nhập lại không đúng", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressDialog.show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(nhaplaipassmoi)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Mật khẩu đã được thay đổi thành công
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("passWord", nhaplaipassmoi);
                                    mRef.child(auth.getCurrentUser().getUid()).updateChildren(map);
                                    Toast.makeText(PasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    // Xảy ra lỗi khi thay đổi mật khẩu
                                    Toast.makeText(PasswordActivity.this, "Đổi mật khẩu không thành công, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
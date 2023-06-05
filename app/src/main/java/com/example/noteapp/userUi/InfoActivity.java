package com.example.noteapp.userUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.noteapp.R;
import com.example.noteapp.adminUi.EditUserActivity;
import com.example.noteapp.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class InfoActivity extends AppCompatActivity {

    EditText name_info, email_info;
    Button btn_change_info;
    ImageView img_profile;
    ImageButton btn_back;

    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private int REQUEST_CODE_PICK_IMAGE = 99;
    private Uri selectedImageUri;
    private StorageReference Sreference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        name_info = findViewById(R.id.ed_name_change_info);
        email_info = findViewById(R.id.ed_email_change_info);
        btn_back = findViewById(R.id.btnback_change_info);
        img_profile = findViewById(R.id.profile_image_change_info);
        btn_change_info = findViewById(R.id.btn_change_info);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Vui lòng chờ...");
        getData();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        btn_change_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_info.getText().toString().trim();
                String email = email_info.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(InfoActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!validateEmail(email)) {
                    Toast.makeText(InfoActivity.this, "Email không đúng định đạng", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressDialog.show();
                    if (selectedImageUri == null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name);
                        map.put("email", email);
                        mRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    UpdateEmail();
                                    progressDialog.dismiss();
                                    Toast.makeText(InfoActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(InfoActivity.this, "Sửa không thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        final StorageReference fileImg = Sreference.child(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
                        fileImg.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("name", name);
                                        map.put("email", email);
                                        map.put("avt", uri.toString());
                                        mRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    UpdateEmail();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(InfoActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(InfoActivity.this, "Sửa không thành công", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE_PICK_IMAGE);
    }

    private String getFileExtension(Uri imgUrl) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(imgUrl));
    }

    private void getData() {
        progressDialog.show();
        mRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (users != null) {
                    name_info.setText(users.getName());
                    email_info.setText(users.getEmail());
                    if (!InfoActivity.this.isFinishing()) {
                        Glide.with(InfoActivity.this).load(users.getAvt()).into(img_profile);
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void UpdateEmail() {
        String email = email_info.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            img_profile.setImageURI(selectedImageUri);
        }
    }
}
package com.example.noteapp.adminUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class EditNoteActivity extends AppCompatActivity {

    RelativeLayout layout_img_edit, layout_pdf_edit;
    EditText ed_title_edit, ed_des_edit;
    Button btn_edit_note;
    ImageButton btnback_edit_note;
    TextView tv_anh_note_edit, tv_file_pdf_edit;
    private Uri selectedImageUri;
    private Uri selectedPDFUri;
    private int REQUEST_CODE_PICK_IMAGE = 3;
    private int REQUEST_CODE_PICK_PDF = 4;
    private StorageReference Sreference = FirebaseStorage.getInstance().getReference();
    FirebaseDatabase database;
    DatabaseReference mRef;
    String linkanh, linkpdf;
    String idNote;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        btn_edit_note = findViewById(R.id.btn_edit_note);
        btnback_edit_note = findViewById(R.id.btnback_edit_note);
        ed_title_edit = findViewById(R.id.ed_title_edit);
        ed_des_edit = findViewById(R.id.ed_des_edit);
        layout_img_edit = findViewById(R.id.layout_img_edit);
        layout_pdf_edit = findViewById(R.id.layout_pdf_edit);
        tv_anh_note_edit = findViewById(R.id.tv_anh_note_edit);
        tv_file_pdf_edit = findViewById(R.id.tv_file_pdf_edit);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Notes");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng chờ...");

        layout_img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        layout_pdf_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPDF();
            }
        });
        btnback_edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ed_title_edit.getText().toString().trim();
                String des = ed_des_edit.getText().toString().trim();
                if (title.isEmpty() || des.isEmpty()) {
                    Toast.makeText(EditNoteActivity.this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressDialog.show();
                    saveData(title, des, selectedImageUri, selectedPDFUri);
                }
            }
        });

        Intent intent = getIntent();
        idNote = intent.getStringExtra("id");
        getData();
    }

    private void getData() {
        mRef.child(idNote).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Note note = snapshot.getValue(Note.class);
                if (note != null) {
                    ed_title_edit.setText(note.getTitle());
                    ed_des_edit.setText(note.getDes());
                } else {
                    Toast.makeText(EditNoteActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveData(String title, String des, Uri selectedImageUri, Uri selectedPDFUri) {
        // if nguoi dung khong chon anh hoac chon file thi update title va des
        if (selectedImageUri == null && selectedPDFUri == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("des", des);
            mRef.child(idNote).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(EditNoteActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(EditNoteActivity.this, "Sửa không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return;
        } else if (selectedImageUri == null && selectedPDFUri != null) {
            final StorageReference fileRef = Sreference.child(System.currentTimeMillis() + "." + getFileExtension(selectedPDFUri));
            fileRef.putFile(selectedPDFUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            linkpdf = uri.toString();
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("des", des);
                            map.put("file", linkpdf);
                            mRef.child(idNote).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditNoteActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditNoteActivity.this, "Sửa không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
            return;
        } else if (selectedPDFUri == null && selectedImageUri != null) {
            final StorageReference fileImg = Sreference.child(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
            fileImg.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            linkanh = uri.toString();
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("des", des);
                            map.put("img", linkanh);
                            mRef.child(idNote).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditNoteActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditNoteActivity.this, "Sửa không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        } else {
            final StorageReference fileImg1 = Sreference.child(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
            fileImg1.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileImg1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            linkanh = uri.toString();
                            final StorageReference filePDF1 = Sreference.child(System.currentTimeMillis() + "." + getFileExtension(selectedPDFUri));
                            filePDF1.putFile(selectedPDFUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    filePDF1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            linkpdf = uri.toString();
                                            Map<String, Object> map = new HashMap<>();
                                            String key = mRef.push().getKey();
                                            map.put("id", key);
                                            map.put("title", title);
                                            map.put("des", des);
                                            map.put("img", linkanh);
                                            map.put("file", linkpdf);
                                            map.put("time", "aaa");
                                            mRef.child(idNote).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditNoteActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditNoteActivity.this, "Sửa không thành công", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE_PICK_IMAGE);
    }

    private void pickPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_CODE_PICK_PDF);
    }

    private String getFileExtension(Uri imgUrl) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(imgUrl));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_PICK_PDF) {
                selectedPDFUri = data.getData();
                String pdf = getFileName(selectedPDFUri);
                tv_file_pdf_edit.setText(pdf);
            } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                selectedImageUri = data.getData();
                String anh = getFileName(selectedImageUri);
                tv_anh_note_edit.setText(anh);

            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                int name = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(name);
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
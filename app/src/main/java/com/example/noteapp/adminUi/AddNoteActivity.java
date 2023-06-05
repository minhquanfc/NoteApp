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
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {

    RelativeLayout layout_img, layout_pdf;
    EditText ed_title, ed_des;
    Button btn_add_note;
    ImageButton btnback_add_note;
    TextView tv_anh_note, tv_file_pdf;
    private Uri selectedImageUri;
    private Uri selectedPDFUri;
    private int REQUEST_CODE_PICK_IMAGE = 1;
    private int REQUEST_CODE_PICK_PDF = 2;
    private StorageReference Sreference = FirebaseStorage.getInstance().getReference();
    FirebaseDatabase database;
    DatabaseReference mRef;
    String linkanh,linkpdf;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        btn_add_note = findViewById(R.id.btn_add_note);
        ed_title = findViewById(R.id.ed_title);
        ed_des = findViewById(R.id.ed_des);
        layout_img = findViewById(R.id.layout_img);
        layout_pdf = findViewById(R.id.layout_pdf);
        btnback_add_note = findViewById(R.id.btnback_add_note);
        tv_anh_note = findViewById(R.id.tv_anh_note);
        tv_file_pdf = findViewById(R.id.tv_file_pdf);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Notes");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng chờ...");

        layout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        layout_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPDF();
            }
        });
        btnback_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ed_title.getText().toString().trim();
                String des = ed_des.getText().toString().trim();
                if (title.isEmpty()|| des.isEmpty()){
                    Toast.makeText(AddNoteActivity.this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                    return;
                }else if (selectedImageUri ==null){
                    Toast.makeText(AddNoteActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }else if (selectedPDFUri == null){
                    Toast.makeText(AddNoteActivity.this, "Vui lòng chọn file PDF", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    progressDialog.show();
                    saveData(title,des,selectedImageUri,selectedPDFUri);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void saveData(String title, String des, Uri linkimg, Uri linkPDF) {
        final StorageReference fileRef = Sreference.child(System.currentTimeMillis() + "." + getFileExtension(linkimg));
        fileRef.putFile(linkimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        linkanh = uri.toString();
                        final StorageReference fileRef1 = Sreference.child(System.currentTimeMillis() + "." + getFileExtension(linkPDF));
                        fileRef1.putFile(linkPDF).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                        mRef.child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(AddNoteActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(AddNoteActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
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
                Log.d("aaa", "pdf: " + selectedPDFUri);
                String pdf = getFileName(selectedPDFUri);
                tv_file_pdf.setText(pdf);
            } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                selectedImageUri = data.getData();
                Log.d("aaa", "anh: " + selectedImageUri);
                String anh = getFileName(selectedImageUri);
                tv_anh_note.setText(anh);

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
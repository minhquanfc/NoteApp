package com.example.noteapp.adminUi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.noteapp.R;
import com.example.noteapp.userUi.fragment.AccountFragment;

public class AdminMainActivity extends AppCompatActivity {

    CardView cardview_postnews,cardview_users;
    ImageView profile_image_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cardview_postnews = findViewById(R.id.cardview_postnews);
        cardview_users = findViewById(R.id.cardview_users);
        profile_image_admin = findViewById(R.id.profile_image_admin);

        cardview_postnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,NoteActivity.class);
                startActivity(intent);
            }
        });
        cardview_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,UsersActivity.class);
                startActivity(intent);
            }
        });
        profile_image_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AccountFragment.class);
                startActivity(intent);
            }
        });
    }
}
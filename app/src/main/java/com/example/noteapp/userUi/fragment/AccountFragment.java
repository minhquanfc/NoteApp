package com.example.noteapp.userUi.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.noteapp.R;
import com.example.noteapp.activity.SplashActivity;
import com.example.noteapp.model.Users;
import com.example.noteapp.userUi.InfoActivity;
import com.example.noteapp.userUi.PasswordActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    RelativeLayout layout_pass_user,layout_sign_out,layout_info_user;
    TextView tv_name_account,tv_email_account;
    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        layout_info_user = view.findViewById(R.id.layout_info_user);
        layout_sign_out = view.findViewById(R.id.layout_sign_out);
        layout_pass_user = view.findViewById(R.id.layout_pass_user);
        tv_name_account = view.findViewById(R.id.tv_name_account);
        tv_email_account = view.findViewById(R.id.tv_email_account);
        imageView = view.findViewById(R.id.profile_image);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Vui lòng chờ...");
        getData();

        layout_info_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InfoActivity.class);
                getContext().startActivity(intent);
            }
        });
        layout_pass_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PasswordActivity.class);
                getContext().startActivity(intent);
            }
        });
        layout_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        return view;
    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bạn có muốn đăng xuất không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void getData() {
//        progressDialog.show();
        mRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Log.e("aaa","aaa: "+users);
                if (users !=null){
                    if (getActivity() == null) {
                        return;
                    }
                    tv_name_account.setText(users.getName());
                    tv_email_account.setText(users.getEmail());
                    Glide.with(getActivity()).load(users.getAvt()).into(imageView);
//                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
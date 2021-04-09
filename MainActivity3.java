package com.shg.socialloginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MainActivity3 extends AppCompatActivity {

    private ImageView profilePic;
    private TextView userName;
    private TextView email;
    private TextView id;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        profilePic = findViewById(R.id.profile_pic);
        userName = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        id = findViewById(R.id.id);
        logout = findViewById(R.id.logout);

        SharedPreferences prefs = getSharedPreferences("facebook", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("Buffer", "");
        Buffer buffer = gson.fromJson(json, Buffer.class);
        updateUI(buffer);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity3.this, MainActivity.class));
                finish();
            }
        });
    }

    private void updateUI(Buffer buffer){
        if(buffer.getId() != null){
            userName.setText(buffer.getName());
            email.setText(buffer.getEmail());
            id.setText(buffer.getId());
            Picasso.get().load(buffer.getURL()).placeholder(R.mipmap.ic_launcher).into(profilePic);
        }else{
            startActivity(new Intent(MainActivity3.this, MainActivity.class));
            finish();
        }
    }
}
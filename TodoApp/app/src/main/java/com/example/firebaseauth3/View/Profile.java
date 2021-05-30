package com.example.firebaseauth3.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firebaseauth3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private TextView textViewEmail;
    private TextView textViewUid;
    private Button button_LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewEmail = findViewById(R.id.txtView_Email);
        button_LogOut = findViewById(R.id.button_LogOut);
        textViewUid = findViewById(R.id.txtView_uid);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        button_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        gotoHomeActivity();
    }

    public void gotoHomeActivity(){

        Button button_gotoProfile = findViewById(R.id.button_gotoHome);

        button_gotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Home.class));
                finish();
            }
        });
    }


    private void checkUser(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            // user not logged in
            startActivity(new Intent(Profile.this, MainActivity.class));
            finish();
        }
        else{
            // user logged in
            // get user info

            String uid = firebaseUser.getUid();
            textViewUid.setText(uid.toString());

            String email = firebaseUser.getEmail();
            textViewEmail.setText(email.toString());
        }
    }













}
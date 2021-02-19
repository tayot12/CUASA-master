package com.project.cuasa.models;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.cuasa.MainActivity;
import com.project.cuasa.activity.LoginActivity;

public class Home extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            Intent myIntent = new Intent(Home.this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            //startActivity(new Intent(Home.this, MainActivity.class));
        } else {
            Intent myIntent = new Intent(Home.this, LoginActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            //startActivity(new Intent(Home.this, LoginActivity.class));
        }
    }
}

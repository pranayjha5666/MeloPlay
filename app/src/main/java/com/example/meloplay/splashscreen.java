package com.example.meloplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(new Runnable() {


            FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
            @Override
            public void run() {
                if(currentUser==null){
                    startActivity(new Intent(splashscreen.this,loginactivity.class));
                }
                else {
                    startActivity(new Intent(splashscreen.this,MainActivity.class));
                }

                finish();
            }
        },3000);




    }
}
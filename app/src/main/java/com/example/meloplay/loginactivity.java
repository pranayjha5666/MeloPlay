package com.example.meloplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginactivity extends AppCompatActivity {
    ImageView loginicon;
    EditText email_edittext,password_edittext;
    Button login_btn;
    TextView create_acount_textview_btn,forgot_password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        loginicon=findViewById(R.id.loginicon);
        loginicon.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink));


        email_edittext=findViewById(R.id.email_edittext);
        password_edittext=findViewById(R.id.password_edittext);
        login_btn=findViewById(R.id.login_btn);
        progressBar=findViewById(R.id.progress_bar);
        create_acount_textview_btn=findViewById(R.id.create_account_textview_btn);
        forgot_password=findViewById(R.id.forgot_password);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_edittext.getText().toString();
                String password=password_edittext.getText().toString();

                boolean isValidated=validateData(email,password);

                if(!isValidated){
                    return;
                }
                loginaccountInFirebase(email,password);
            }
        });

        create_acount_textview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginactivity.this,createaccountactivity.class));
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginactivity.this,forgot_password.class));
            }
        });

    }

    void loginaccountInFirebase(String email,String password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(loginactivity.this,MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(loginactivity.this, "Email Not Veirified !!!, Please Verify it", Toast.LENGTH_SHORT).show();
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();
                    }
                }
                else{
                    Toast.makeText(loginactivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            login_btn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            login_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_edittext.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            password_edittext.setError("Password length is invalid");
            return false;
        }

        return  true;
    }
}
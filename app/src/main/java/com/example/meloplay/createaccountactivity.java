package com.example.meloplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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

public class createaccountactivity extends AppCompatActivity {
    ImageView signupicon;
    EditText email_edittext,password_edittext,conform_password_edittext;
    Button create_account_btn;
    TextView login_textview_btn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccountactivity);
        signupicon=findViewById(R.id.signupicon);
        signupicon.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink));

        email_edittext=findViewById(R.id.email_edittext);
        password_edittext=findViewById(R.id.password_edittext);
        conform_password_edittext=findViewById(R.id.conform_password_edittext);
        create_account_btn=findViewById(R.id.create_account_btn);
        progressBar=findViewById(R.id.progress_bar);
        login_textview_btn=findViewById(R.id.login_textview_btn);

        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_edittext.getText().toString();
                String password=password_edittext.getText().toString();
                String conformPassword=conform_password_edittext.getText().toString();
                boolean isValidated=validateData(email,password,conformPassword);

                if(!isValidated){
                    return;
                }
                createaccountInFirebase(email,password);
            }
        });

        login_textview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(createaccountactivity.this,loginactivity.class));
            }
        });


    }


    void createaccountInFirebase(String email,String password){
        changeInProgress(true);


        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(createaccountactivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    Toast.makeText(createaccountactivity.this, "Successfully Created,Check Emial to verify", Toast.LENGTH_LONG).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }
                else {
                    Toast.makeText(createaccountactivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            create_account_btn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            create_account_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password,String conform_password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_edittext.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            password_edittext.setError("Password must be more than 5 character");
            return false;
        }
        if(!password.equals(conform_password)){
            conform_password_edittext.setError("Password not matched");
            return false;
        }
        return  true;
    }
}
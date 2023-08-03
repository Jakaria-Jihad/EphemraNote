package com.example.ephemranote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText userEmail, userPassword;
    Button loginbtn;
    ProgressBar creationProgressbar;
    TextView createAccountTextviewbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.user_email_edited_text);
        userPassword = findViewById(R.id.user_password_edited_text);
        loginbtn = findViewById(R.id.login_btn);
        creationProgressbar = findViewById(R.id.onCreate_progressbar);
        createAccountTextviewbtn = findViewById(R.id.create_account_textView_btn_lgnpg);

        loginbtn.setOnClickListener(v -> loginUser());
        createAccountTextviewbtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));

    }
    void loginUser(){
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        boolean is_validated = validateData(email, password);
        if (!is_validated){
            return;
        }else {
            loginAccountInFirebase(email, password);
        }

    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changingProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changingProgress(false);
                if (task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Utility.showToastMessage(LoginActivity.this, "Email not verified. Please verify your email, also check spam in email if you can't see any in inbox");
                    }
                }else{
                    Utility.showToastMessage(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });

    }

    void changingProgress(boolean inProgress){
        if (inProgress){
            creationProgressbar.setVisibility(View.VISIBLE);
            loginbtn.setVisibility(View.GONE);
        }else {
            creationProgressbar.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6){
            userPassword.setError("Password is too short");
            return false;
        }
        return true;
    }
}
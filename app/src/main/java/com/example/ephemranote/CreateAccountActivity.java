package com.example.ephemranote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    EditText userEmail, userPassword, confirmPassword;
    Button createAccountbtn;
    ProgressBar creationProgressbar;
    TextView loginTextviewbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        userEmail = findViewById(R.id.user_email_edited_text);
        userPassword = findViewById(R.id.user_password_edited_text);
        confirmPassword = findViewById(R.id.user_password_confirmed_edited_text);
        createAccountbtn = findViewById(R.id.create_account_btn);
        creationProgressbar = findViewById(R.id.onCreate_progressbar);
        loginTextviewbtn = findViewById(R.id.login_textView_btn_sgnpg);

        createAccountbtn.setOnClickListener(v -> createAccount());
        loginTextviewbtn.setOnClickListener(v -> startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class)));
    }
    void createAccount(){
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confirm_password = confirmPassword.getText().toString();

        boolean is_validated = validateData(email, password, confirm_password);
        if (!is_validated){
            return;
        }else {
            createAccountFirebase(email, password);
        }


    }

    boolean validateData(String email, String password, String confirmpassword){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6){
            userPassword.setError("Password is too short");
            return false;
        }
        if (!password.equals(confirmpassword)){
            confirmPassword.setError("Password doesn't match");
            return false;
        }
        return true;
    }

    void createAccountFirebase(String email, String password){
        changingProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changingProgress(false);
                        if (task.isSuccessful()){
                            Utility.showToastMessage(CreateAccountActivity.this, "Account created successfully, check email for verification");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else {
                            Utility.showToastMessage(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                        }

                    }
                });

    }

    void changingProgress(boolean inProgress){
        if (inProgress){
            creationProgressbar.setVisibility(View.VISIBLE);
            createAccountbtn.setVisibility(View.GONE);
        }else {
            creationProgressbar.setVisibility(View.GONE);
            createAccountbtn.setVisibility(View.VISIBLE);
        }
    }
}
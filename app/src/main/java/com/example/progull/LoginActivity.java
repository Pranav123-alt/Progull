package com.example.progull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout inputEmail,inputPassword;
    Button btnLogin;
    TextView createNewAccount,forgotPassword;
    ProgressDialog mLoadingBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        createNewAccount = findViewById(R.id.createNewAccount);
        forgotPassword = findViewById(R.id.forgotPassword);
        mLoadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptLogin();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });




    }

    private void AttemptLogin() {
        String email = inputEmail.getEditText().getText().toString();
        String password = inputPassword.getEditText().getText().toString();


        if(email.isEmpty()) {
            showError(inputEmail,"Please enter the Email");
        }
        else if(password.isEmpty()||password.length()<5) {
            showError(inputPassword,"Password must be greater than 5 letters");
        }
        else{

            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait, While check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        mLoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Login is succesfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,SetupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        mLoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }



                }
            });



        }



    }

    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();

    }
}

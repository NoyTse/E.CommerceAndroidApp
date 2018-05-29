package com.example.noytse.loginfacebook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noytse.loginfacebook.analytics.AnalyticsManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG  = "SignUp";
    private TextView email;
    private TextView password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.Emailtxt);
        password = findViewById(R.id.Passwordtxt);
        mAuth = FirebaseAuth.getInstance();
        ((AppCompatButton)findViewById(R.id.SignUpBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClick(view);
            }
        });

    }



    public void onSignUpClick(View view) {

        boolean valid = true;
        if (email.getText().toString().isEmpty() || !email.getText().toString().contains("@")) {
            //findViewById(R.id.lblSignInInvalidEmail).setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        else if (password.getText().toString().isEmpty() || password.getText().toString().length() < 8) {
            //findViewById(R.id.lblSignInInvalidEmail).setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Invalid Password. must be 8 characters", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (valid)
        {
            createAccount(email.getText().toString(), password.getText().toString());
        }
    }

    public void createAccount(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //AnalyticsManager.getInstance().trackSignupEvent(user.getDisplayName());
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        // ...
                    }
                });
    }
    @Override
    public void onStop(){
        super.onStop();
        AnalyticsManager.getInstance().trackTimeInsideTheApp();
    }
}

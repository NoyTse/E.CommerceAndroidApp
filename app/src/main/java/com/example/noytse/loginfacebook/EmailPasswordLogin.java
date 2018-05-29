package com.example.noytse.loginfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noytse.loginfacebook.MainActivity;
import com.example.noytse.loginfacebook.analytics.AnalyticsManager;
import com.example.noytse.loginfacebook.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.startActivities;
import static android.support.v4.content.ContextCompat.startActivity;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by user on 24/04/2018.
 */

public class EmailPasswordLogin {
    public static final int INVALID_EMAIL = 0;
    public static final int INVALID_PASSWORD = 1;
    private static final String TAG  = "EmailPasswordLogin";
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private MainActivity activity;


    public void setInput(String i_email, String i_password){
        this.email = i_email;
        this.password = i_password;
    }
    public EmailPasswordLogin(MainActivity activity,FirebaseAuth auth) {
        this.activity = activity;
        this.email = email;
        this.password = password;
        mAuth = FirebaseAuth.getInstance();
    }

    public void onSignInClick(View view) {
        if (email.isEmpty() || !email.contains("@")) {
            //activity.showInvalidToolTip(INVALID_EMAIL);
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty() || password.length() < 8) {
            Toast.makeText(getApplicationContext(), "Invalid password, must be 8 characters", Toast.LENGTH_SHORT).show();
        }
        else{
            signIn(email, password);
        }
    }

    public void onForgotMyPasswordClick(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;
        if (email.isEmpty() || !email.contains("@")) {
            //activity.showInvalidToolTip(INVALID_EMAIL);
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
        }
        else{
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
        }
    }
        private void signIn(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AnalyticsManager.getInstance().trackSignupEvent("Email-password");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            activity.onLoggedInUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            activity.onLoggedInUser(null);
                        }
                    }
                });
    }
}

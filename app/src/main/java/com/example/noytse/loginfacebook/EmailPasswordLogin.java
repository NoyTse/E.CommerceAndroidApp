package com.example.noytse.loginfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noytse.loginfacebook.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.support.v4.content.ContextCompat.startActivities;
import static android.support.v4.content.ContextCompat.startActivity;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by user on 24/04/2018.
 */

public class EmailPasswordLogin {
    private static final String TAG  = "EmailPasswordLogin";
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private MainActivity activity;

    public EmailPasswordLogin(MainActivity activity,String email, String password,FirebaseAuth auth) {
        this.activity = activity;
        this.email = email;
        this.password = password;
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser Start() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }



    public void onSignInClick(View view) {
        if (email.isEmpty()) {
            displayMessage("Invalid Email field");
        } else if (!email.contains("@")) {
            displayMessage("Email nust contains @");
        } else if (password.isEmpty()) {
            displayMessage("Invalid Password field");
        }
        signIn(email, password);
    }

    public void signIn(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
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

                        // ...
                    }
                });
    }

    //FindView<>.onClick = () => {EmailPassonSign}


    public void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}

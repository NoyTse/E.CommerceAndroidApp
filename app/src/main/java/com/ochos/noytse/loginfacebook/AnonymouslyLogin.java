package com.ochos.noytse.loginfacebook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AnonymouslyLogin {
    private static final String TAG = "AnonymouseLogin Class";
    private MainActivity mLoginActivity;
    private FirebaseAuth mAuth;

    public AnonymouslyLogin(MainActivity loginActivity, final FirebaseAuth mAuth, TextView skipBtn) {
        this.mAuth = mAuth;
        this.mLoginActivity = loginActivity;
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(mLoginActivity, new OnCompleteListener<AuthResult>() {
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInAnonymously:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    mLoginActivity.onLoggedInUser(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                                    Toast.makeText(mLoginActivity, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    mLoginActivity.onLoggedInUser(null);
                                }
                            }
                        });
            }
        });
    }
}

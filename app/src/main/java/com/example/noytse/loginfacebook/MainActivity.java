package com.example.noytse.loginfacebook;

import android.app.Application;
import android.content.Intent;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private FacebookLogin mFacebookLogin;
    private GmailLogin mGmailLogin;
    private EmailPasswordLogin mEmailPassLogin;
    private FirebaseAuth mAuth;
    private boolean mAnonymosEnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mFacebookLogin = new FacebookLogin(this,mAuth,(LoginButton)findViewById(R.id.facebookLoginBtn));
        mGmailLogin = new GmailLogin(this);
        mEmailPassLogin = new EmailPasswordLogin(this,mAuth);

        if (!mAnonymosEnable)
            findViewById(R.id.lblSkip).setVisibility(View.INVISIBLE);

        ((AppCompatButton)findViewById(R.id.btnSignIn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = ((EditText)findViewById(R.id.txtSignInEmail)).getText().toString();
                String inputPassword = ((EditText)findViewById(R.id.txtSignInPassword)).getText().toString();
                mEmailPassLogin.setInput(inputEmail,inputPassword);
                mEmailPassLogin.onSignInClick(view);
            }
        });
        ((AppCompatButton)findViewById(R.id.btnSignUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    public void showInvalidToolTip(int errorID){
        if (errorID == EmailPasswordLogin.INVALID_EMAIL){
            findViewById(R.id.lblSignInInvalidEmail).setVisibility(View.VISIBLE);
        }
        if (errorID == EmailPasswordLogin.INVALID_PASSWORD) {
            findViewById(R.id.lblSignInInvalidPassword).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookLogin.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        onLoggedInUser(currentUser);

    }


    public void onLoggedInUser(FirebaseUser loggedInUser){
        if (loggedInUser != null) {
            Intent userProfileIntent = new Intent(this,UserProfileActivity.class);
            userProfileIntent.putExtra(UserProfileActivity.k_UserName, loggedInUser.getDisplayName());
            userProfileIntent.putExtra(UserProfileActivity.k_UserEmail, loggedInUser.getEmail());
            if (loggedInUser.getPhotoUrl() != null)
                userProfileIntent.putExtra(UserProfileActivity.k_UserPhotoURL,loggedInUser.getPhotoUrl());
            startActivity(userProfileIntent);
            finish();
        }
    }

}

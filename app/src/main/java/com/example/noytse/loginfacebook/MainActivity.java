package com.example.noytse.loginfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.provider.FirebaseInitProvider;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private FacebookLogin mFacebookLogin;
    private GmailLogin mGmailLogin;
    private EmailPasswordLogin mEmailPassLogin;
    private AnonymouslyLogin mAnonymouslyLogin;
    private FirebaseAuth mAuth;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean mAnonymouseEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAnonymouseEnable();
        mAuth = FirebaseAuth.getInstance();
        mFacebookLogin = new FacebookLogin(this,mAuth,(LoginButton)findViewById(R.id.facebookLoginBtn));

        mEmailPassLogin = new EmailPasswordLogin(this,mAuth);
        SignInButton gmailSignInBtn = (SignInButton)findViewById(R.id.btnGoogleSignIn);
        mGmailLogin = new GmailLogin(this,gmailSignInBtn);
        if (!mAnonymouseEnable)
            findViewById(R.id.lblSkip).setVisibility(View.INVISIBLE);
        else{
            mAnonymouslyLogin = new AnonymouslyLogin(this, mAuth, (TextView)findViewById(R.id.lblSkip));
        }
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

    private void checkAnonymouseEnable() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.activateFetched();
        mAnonymouseEnable = mFirebaseRemoteConfig.getBoolean("AnonymouseEnable");
    }

    public void showInvalidToolTip(boolean validEmail, boolean validPassword){
        if(!validEmail)
            findViewById(R.id.lblSignInInvalidEmail).setVisibility(View.VISIBLE);
        else if (findViewById(R.id.lblSignInInvalidEmail).getVisibility() != View.GONE)
            findViewById(R.id.lblSignInInvalidEmail).setVisibility(View.GONE);


        if(!validPassword)
            findViewById(R.id.lblSignInInvalidPassword).setVisibility(View.VISIBLE);
        else if (findViewById(R.id.lblSignInInvalidPassword).getVisibility() != View.GONE)
            findViewById(R.id.lblSignInInvalidPassword).setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GmailLogin.GoogleSignIN)
            mGmailLogin.onActivityResult(requestCode,resultCode,data);

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
                userProfileIntent.putExtra(UserProfileActivity.k_UserPhotoURL,loggedInUser.getPhotoUrl().toString());
            startActivity(userProfileIntent);
            finish();
        }
    }
}

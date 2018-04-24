package com.example.noytse.loginfacebook;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * Created by Hezi on 24/04/2018.
 */

public class GmailLogin {
    private GoogleSignInClient mGoogleSignInClient;
    private MainActivity mMainActivity;
    public GmailLogin(MainActivity loginActivity){
        mMainActivity = loginActivity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("13742759186-io5on76vb4qa1692prus5gkucnfk7fk5.apps.googleusercontent.com")
                .requestEmail()
                .build();
    }

    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}

package com.ochos.noytse.loginfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ochos.noytse.loginfacebook.analytics.AnalyticsManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private FacebookLogin mFacebookLogin;
    private GmailLogin mGmailLogin;
    private EmailPasswordLogin mEmailPassLogin;
    private AnonymouslyLogin mAnonymouslyLogin;
    private FirebaseAuth mAuth;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private ProductsDatabase mDataBase;
    private boolean mAnonymouseEnable = true;
    public static Date enterAppTime;
    public static boolean isInTheApp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enterAppTime=new Date();
        AnalyticsManager.getInstance().init(getApplicationContext());
        AnalyticsManager.getInstance().trackAppEntrance();


        boolean isFromProductDetails = getIntent().getBooleanExtra("isFromProductDetails", false);
        boolean isAnonymouse = getIntent().getBooleanExtra("isAnonymouse", false);

        checkAnonymouseEnable();
        mAuth = FirebaseAuth.getInstance();
        if(isFromProductDetails && isAnonymouse){
            mAuth.signOut();
        }

        mFacebookLogin = new FacebookLogin(this,mAuth,(LoginButton)findViewById(R.id.facebookLoginBtn));

        mEmailPassLogin = new EmailPasswordLogin(this,mAuth);
        SignInButton gmailSignInBtn = (SignInButton)findViewById(R.id.btnGoogleSignIn);
        mGmailLogin = new GmailLogin(this,gmailSignInBtn);
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
    @Override
    public void onUserLeaveHint() {
        AnalyticsManager.getInstance().trackTimeInsideTheApp();
    }

    private void checkAnonymouseEnable() {
        final String anonymosParamName = "AnonymouseEnable";
        Map<String,Object> remoteParams = new HashMap<>();
        remoteParams.put(anonymosParamName,true);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(remoteParams);

        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        }
                        mAnonymouseEnable = mFirebaseRemoteConfig.getBoolean(anonymosParamName);
                        if (!mAnonymouseEnable)
                            findViewById(R.id.lblSkip).setVisibility(View.INVISIBLE);
                        else
                            mAnonymouslyLogin = new AnonymouslyLogin(MainActivity.this, mAuth, (TextView) findViewById(R.id.lblSkip));
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GmailLogin.GoogleSignIN)
            mGmailLogin.onActivityResult(requestCode,resultCode,data);

        mFacebookLogin.onActivityResult(requestCode,resultCode,data);
    }



    public void onLoggedInUser(FirebaseUser loggedInUser){
        if (loggedInUser != null) {


            Intent userMoreDetailsIntent = new Intent(this,user_details_form.class);
            startActivity(userMoreDetailsIntent);
            finish();
        }
    }
}

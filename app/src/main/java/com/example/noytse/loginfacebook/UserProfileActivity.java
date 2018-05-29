package com.example.noytse.loginfacebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.noytse.loginfacebook.analytics.AnalyticsManager;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class UserProfileActivity extends AppCompatActivity {
    public static final String k_UserPhotoURL = "photoUrl";
    public static final String k_UserName = "DisplayName";
    public static final String k_UserEmail = "Email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        String userName = this.getIntent().getStringExtra(k_UserName);
        String userPhotoUrl = this.getIntent().getStringExtra(k_UserPhotoURL);
        String userEmail = this.getIntent().getStringExtra(k_UserEmail);
        AppCompatButton btnSignOut = (AppCompatButton)findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfileActivity.this,MainActivity.class));
                finish();
            }
        });


        ((TextView)findViewById(R.id.lblUserName)).setText(userName);
        ((TextView)findViewById(R.id.lblUserEmail)).setText(userEmail);
        ImageView imgUserPhoto = ((ImageView)findViewById(R.id.imgUserPhoto));
        //Load image
        if (userPhotoUrl != null)
            Picasso.with(this).load(userPhotoUrl).into(imgUserPhoto);
    }

    @Override
    public void onUserLeaveHint() {
        AnalyticsManager.getInstance().trackTimeInsideTheApp();
    }
}

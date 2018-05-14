package com.example.noytse.loginfacebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class productDetails extends AppCompatActivity {

    public final String TAG = "ProductDetailsActivity";

    private Button mPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "onCreate()>>>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "buyBag.onClick() >>");

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                while (mAuth.getCurrentUser().isAnonymous()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}

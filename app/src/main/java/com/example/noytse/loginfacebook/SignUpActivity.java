package com.example.noytse.loginfacebook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noytse.loginfacebook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    }
    public void onSignUpClick(View view) {


        if (email.getText().toString().isEmpty()) {
            displayMessage("Invalid Email field");
        } else if (!email.getText().toString().contains("@")) {
            displayMessage("Email nust contains @");
        } else if (password.getText().toString().isEmpty()) {
            displayMessage("Invalid Password field");
        }
        createAccount(email.getText().toString(), password.getText().toString());
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
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null)
        {
            // To change to the activity welcome!!!!!!
            //Intent i = new Intent(getApplicationContext(), welcome.class);
            //i.putExtra("UserName",user.getEmail().toString());
            //startActivity(i);
        }
    }


    public void displayMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

}

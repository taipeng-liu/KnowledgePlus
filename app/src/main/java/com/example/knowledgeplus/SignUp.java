package com.example.knowledgeplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {
    EditText fullName, email, password;
    Button registerButton;
    FirebaseAuth fAuth;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");

        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.loginText);


        fAuth = FirebaseAuth.getInstance();
//
//        // user has already loggedin
//        if(fAuth.getCurrentUser() != null) {
//            fAuth.updateCurrentUser(null);
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordString = password.getText().toString().trim();
                String emailString = email.getText().toString().trim();
                String fullNameString = fullName.getText().toString();
                if (TextUtils.isEmpty(passwordString)) {
                    password.setError("Password is required");
                    return;
                }
                if(TextUtils.isEmpty(fullNameString)) {
                    fullName.setError("Full name is required");
                    return;
                }
                if (TextUtils.isEmpty(emailString)) {
                    email.setError("Email address is required");
                    return;
                }

                // register the user in firebase
                fAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "User Created.", Toast.LENGTH_SHORT).show();
                            FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fullNameString).build();
                            newUser.updateProfile(profileUpdates);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(SignUp.this, "Error !" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
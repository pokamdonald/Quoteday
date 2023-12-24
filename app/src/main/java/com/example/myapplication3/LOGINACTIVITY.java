package com.example.myapplication3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LOGINACTIVITY extends AppCompatActivity {

    private EditText editTextTextEmailAddress, editTextTextPassword;
    private Button Btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        Btn = findViewById(R.id.button);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserAccount();
            }
        });
    }

    private void loginUserAccount() {
        String email, password;
        email = editTextTextEmailAddress.getText().toString();
        password = editTextTextPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter valid email and password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful, retrieve user data from Cloud Firestore
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserRoleAndRedirect(user.getUid());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                            "Login failed!!", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    private void checkUserRoleAndRedirect(String userId) {
        firestore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String role = document.getString("role");

                                // Now you can check the user role
                                if ("admin".equals(role)) {
                                    // Start Admin Activity
                                    startActivity(new Intent(LOGINACTIVITY.this, INFOSPAGE.class));
                                    finish();
                                } else if ("user".equals(role)) {
                                    // Start User Activity
                                    // Replace UserActivity.class with your actual User Activity
                                    startActivity(new Intent(LOGINACTIVITY.this,
                                            User.class));
                                    finish();
                                } else {
                                    // Unknown role, handle accordingly
                                    Toast.makeText(getApplicationContext(),
                                                    "Unknown user role: " + role, Toast.LENGTH_LONG)
                                            .show();
                                }
                            } else {
                                // Document doesn't exist, handle accordingly
                                Toast.makeText(getApplicationContext(),
                                                "User document doesn't exist", Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            // Handle task failure
                            Toast.makeText(getApplicationContext(),
                                            "Error checking user role: " + task.getException(), Toast.LENGTH_LONG)
                                    .show();
                            Log.e("Firebase", "Error checking user role", task.getException());
                        }
                    }
                });
    }

}


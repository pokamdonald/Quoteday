package com.example.myapplication3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {

    private Button btn;
    private EditText editTextTextEmailAddress2, editTextTextTextAddress2, editTextTextPassword3, editTextPhone;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        editTextTextEmailAddress2 = findViewById(R.id.editTextTextEmailAddress2);
        editTextTextTextAddress2 = findViewById(R.id.editTextTextEmailAddress2);
        editTextTextPassword3 = findViewById(R.id.editTextTextPassword3);
        editTextPhone = findViewById(R.id.editTextPhone);
        btn = findViewById(R.id.button4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        String email, password;
        email = editTextTextEmailAddress2.getText().toString();
        password = editTextTextPassword3.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter valid email and password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, set initial user role
                            setInitialUserRole(task);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Registration failed! Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setInitialUserRole(Task<AuthResult> task) {
        // Get the UID of the newly registered user
        String userId = task.getResult().getUser().getUid();

        // Create a new user document in Firestore with initial role as "user"
        Map<String, Object> user = new HashMap<>();
        user.put("email", task.getResult().getUser().getEmail());
        user.put("role", "user");

        firestore.collection("users")
                .document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                            // Redirect to the login activity
                            Intent intent = new Intent(SigninActivity.this, LOGINACTIVITY.class);
                            startActivity(intent);
                        } else {
                            // Handle the failure to create the user document
                            Toast.makeText(getApplicationContext(),
                                    "Failed to set user role. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

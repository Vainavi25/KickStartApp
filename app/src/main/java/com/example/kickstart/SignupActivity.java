package com.example.kickstart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    EditText signupEmail, signupPassword;
    Button btnCreateAccount;
    CheckBox showSignupPassword;
    TextView alreadyHaveAccount;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        showSignupPassword = findViewById(R.id.showSignupPassword);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        auth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        // Show/hide password
        showSignupPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signupPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                signupPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            signupPassword.setSelection(signupPassword.length());
        });

        // Create account button
        btnCreateAccount.setOnClickListener(v -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase Signup
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            // Go to Dashboard
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Already have account click
        alreadyHaveAccount.setOnClickListener(v -> {
            Toast.makeText(this, "You already have an account. Login from dashboard.", Toast.LENGTH_SHORT).show();
        });
    }
}

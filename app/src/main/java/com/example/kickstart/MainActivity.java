package com.example.kickstart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button btnLogin, btnSignup;
    CheckBox showPassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        // ✅ 1) Check SharedPreferences + Firebase to auto-skip login if already logged in
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (isLoggedIn && currentUser != null) {
            // User already logged in → go directly to Dashboard
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
            return; // Stop here so login UI doesn't show
        }

        // 🔹 If not logged in → normal login screen
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        showPassword = findViewById(R.id.showPassword);

        // Show/hide password
        showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loginPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                loginPassword.setInputType(
                        android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
            }
            loginPassword.setSelection(loginPassword.length());
        });

        // ✅ 2) Login button click
        btnLogin.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 Firebase Login
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // ✅ Save login status in SharedPreferences
                            SharedPreferences sp = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            String msg = task.getException() != null
                                    ? task.getException().getMessage()
                                    : "Login failed";
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Go to signup page
        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        });
    }
}

package com.example.kickstart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText signupEmail, signupPassword;
    Button btnCreateAccount;
    CheckBox showSignupPassword;
    TextView alreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        showSignupPassword = findViewById(R.id.showSignupPassword);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        // Toggle password visibility
        showSignupPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signupPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                signupPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            signupPassword.setSelection(signupPassword.length());
        });

        // When Create Account button is clicked
        btnCreateAccount.setOnClickListener(v -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save credentials
            getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("email", email)
                    .putString("password", password)
                    .apply();

            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            // Go back to login page
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });

        // Go to login page manually
        alreadyHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });
    }
}

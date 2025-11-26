package com.example.kickstart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    Button btnStepTracker, btnStopwatchTimer, btnReminder, btnOvulationTracker, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // XML should be activity_dashboard.xml

        btnStepTracker = findViewById(R.id.btnStepTracker);
        btnStopwatchTimer = findViewById(R.id.btnStopwatchTimer);
        btnReminder = findViewById(R.id.btnReminder);
        btnOvulationTracker = findViewById(R.id.btnOvulationTracker);
        logoutButton = findViewById(R.id.logoutButton);

        // Step Tracker
        btnStepTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, StepTrackerActivity.class);
                startActivity(intent);
            }
        });

        // Stopwatch & Timer
        btnStopwatchTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, StopwatchTimerActivity.class);
                startActivity(intent);
            }
        });

        // Reminder
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });

        // Ovulation Tracker
        btnOvulationTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, OvulationTrackerActivity.class);
                startActivity(intent);
            }
        });

        // ✅ Logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1) Sign out from Firebase
                FirebaseAuth.getInstance().signOut();

                // 2) Update SharedPreferences flag
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                // 3) Go back to login screen
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                // Clear back stack so user can't go back to Dashboard by back button
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}

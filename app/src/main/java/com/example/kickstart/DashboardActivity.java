package com.example.kickstart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnStepTracker, btnStopwatchTimer, btnReminder, btnOvulationTracker, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnStepTracker = findViewById(R.id.btnStepTracker);
        btnStopwatchTimer = findViewById(R.id.btnStopwatchTimer);
        btnReminder = findViewById(R.id.btnReminder);
        btnOvulationTracker = findViewById(R.id.btnOvulationTracker);
        logoutButton = findViewById(R.id.logoutButton);

        // Step Tracker
        btnStepTracker.setOnClickListener(v -> startActivity(new Intent(this, StepTrackerActivity.class)));

        // Stopwatch & Timer
        btnStopwatchTimer.setOnClickListener(v -> startActivity(new Intent(this, StopwatchTimerActivity.class)));

        // Reminder
        btnReminder.setOnClickListener(v -> startActivity(new Intent(this, ReminderActivity.class)));

        // Ovulation Tracker
        btnOvulationTracker.setOnClickListener(v -> startActivity(new Intent(this, OvulationTrackerActivity.class)));

        // Logout
        logoutButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}

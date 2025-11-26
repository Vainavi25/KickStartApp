package com.example.kickstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepTrackerActivity extends AppCompatActivity {

    TextView tvTodaySteps, tvGoalValue, tvProgressPercent, tvCaloriesValue,
            tvStreakValue, tvStepInfo;
    ProgressBar progressSteps;
    Button btnAddSteps, btnResetSteps, btnSetGoal;
    EditText etGoalInput;

    // Step tracking
    private int todaySteps = 0;
    private int dailyGoal = 8000;  // default
    private int streakDays = 0;

    // For date tracking
    private String lastUpdatedDate = "";

    // SharedPreferences keys
    private static final String PREFS_NAME = "StepTrackerPrefs";
    private static final String KEY_TODAY_STEPS = "today_steps";
    private static final String KEY_STREAK = "streak_days";
    private static final String KEY_LAST_DATE = "last_date";
    private static final String KEY_DAILY_GOAL = "daily_goal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_tracker);

        tvTodaySteps = findViewById(R.id.tvTodaySteps);
        tvGoalValue = findViewById(R.id.tvGoalValue);
        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        tvCaloriesValue = findViewById(R.id.tvCaloriesValue);
        tvStreakValue = findViewById(R.id.tvStreakValue);
        tvStepInfo = findViewById(R.id.tvStepInfo);
        progressSteps = findViewById(R.id.progressSteps);
        btnAddSteps = findViewById(R.id.btnAddSteps);
        btnResetSteps = findViewById(R.id.btnResetSteps);
        etGoalInput = findViewById(R.id.etGoalInput);
        btnSetGoal = findViewById(R.id.btnSetGoal);

        // Load saved data
        loadData();

        // Show current goal
        tvGoalValue.setText(dailyGoal + " steps");

        // Check new day
        checkForNewDay();

        // Initial UI
        updateUI();

        // Set Goal button
        btnSetGoal.setOnClickListener(v -> {
            String goalText = etGoalInput.getText().toString().trim();
            if (goalText.isEmpty()) {
                Toast.makeText(this, "Please enter a step goal", Toast.LENGTH_SHORT).show();
                return;
            }

            int newGoal;
            try {
                newGoal = Integer.parseInt(goalText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newGoal <= 0) {
                Toast.makeText(this, "Goal must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            dailyGoal = newGoal;
            tvGoalValue.setText(dailyGoal + " steps");
            Toast.makeText(this, "Goal updated!", Toast.LENGTH_SHORT).show();
            updateUI();
            saveData();
        });

        // Add steps demo
        btnAddSteps.setOnClickListener(v -> {
            todaySteps += 100; // demo increment
            Toast.makeText(this, "Added 100 steps", Toast.LENGTH_SHORT).show();
            updateUI();
            saveData();
        });

        // Reset today steps
        btnResetSteps.setOnClickListener(v -> {
            todaySteps = 0;
            updateUI();
            saveData();
        });
    }

    private void updateUI() {
        tvTodaySteps.setText(String.valueOf(todaySteps));

        float percent = (todaySteps * 100f) / dailyGoal;
        if (percent > 100) percent = 100;
        progressSteps.setProgress((int) percent);
        tvProgressPercent.setText(String.format(Locale.getDefault(), "%.0f%% of goal", percent));

        float calories = todaySteps * 0.04f; // demo formula
        tvCaloriesValue.setText(String.format(Locale.getDefault(), "%.1f kcal", calories));

        tvStreakValue.setText(streakDays + " days");

        // streak logic handled in checkForNewDay()
    }

    private void checkForNewDay() {
        String todayDate = getTodayDateString();

        if (lastUpdatedDate == null || lastUpdatedDate.isEmpty()) {
            lastUpdatedDate = todayDate;
            saveData();
            return;
        }

        if (!todayDate.equals(lastUpdatedDate)) {
            // New day
            if (todaySteps >= dailyGoal) {
                streakDays++;
            }
            todaySteps = 0;
            lastUpdatedDate = todayDate;
            saveData();
        }
    }

    private String getTodayDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        todaySteps = prefs.getInt(KEY_TODAY_STEPS, 0);
        streakDays = prefs.getInt(KEY_STREAK, 0);
        lastUpdatedDate = prefs.getString(KEY_LAST_DATE, "");
        dailyGoal = prefs.getInt(KEY_DAILY_GOAL, 8000); // default 8000
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_TODAY_STEPS, todaySteps);
        editor.putInt(KEY_STREAK, streakDays);
        editor.putString(KEY_LAST_DATE, getTodayDateString());
        editor.putInt(KEY_DAILY_GOAL, dailyGoal);
        editor.apply();
    }
}

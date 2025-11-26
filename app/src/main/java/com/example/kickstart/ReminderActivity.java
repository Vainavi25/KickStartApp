package com.example.kickstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ReminderActivity extends AppCompatActivity {

    TextView tvWaterDetails, tvWaterTimeRange, tvMedTimeRange, tvReminderInfo;
    Button btnSaveReminderSettings;
    CheckBox cbWaterReminder, cbMedReminder;

    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String KEY_WATER_ENABLED = "water_enabled";
    private static final String KEY_MED_ENABLED = "med_enabled";

    // By default: ON
    private boolean waterReminderEnabled = true;
    private boolean medReminderEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Find views
        tvWaterDetails = findViewById(R.id.tvWaterDetails);
        tvWaterTimeRange = findViewById(R.id.tvWaterTimeRange);
        tvMedTimeRange = findViewById(R.id.tvMedTimeRange);
        tvReminderInfo = findViewById(R.id.tvReminderInfo);
        btnSaveReminderSettings = findViewById(R.id.btnSaveReminderSettings);
        cbWaterReminder = findViewById(R.id.cbWaterReminder);
        cbMedReminder = findViewById(R.id.cbMedReminder);

        // Load data from SharedPreferences
        loadData();

        // Set initial checkbox state
        cbWaterReminder.setChecked(waterReminderEnabled);
        cbMedReminder.setChecked(medReminderEnabled);

        // When user manually clicks checkbox, update variable
        cbWaterReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            waterReminderEnabled = isChecked;
        });

        cbMedReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            medReminderEnabled = isChecked;
        });

        // Save button click
        btnSaveReminderSettings.setOnClickListener(v -> {
            saveData();
            String msg = "Settings saved.\nWater: " +
                    (waterReminderEnabled ? "ON" : "OFF") +
                    " | Medication: " +
                    (medReminderEnabled ? "ON" : "OFF");
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        waterReminderEnabled = prefs.getBoolean(KEY_WATER_ENABLED, true); // default ON
        medReminderEnabled = prefs.getBoolean(KEY_MED_ENABLED, true);     // default ON
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_WATER_ENABLED, waterReminderEnabled);
        editor.putBoolean(KEY_MED_ENABLED, medReminderEnabled);
        editor.apply();
    }
}

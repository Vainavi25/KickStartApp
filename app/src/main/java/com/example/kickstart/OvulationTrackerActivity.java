package com.example.kickstart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OvulationTrackerActivity extends AppCompatActivity {

    Button btnSelectDate, btnCalculateOvulation;
    TextView tvSelectedDate, tvNextPeriod, tvOvulationDay, tvFertileWindow, tvReminderMessage;
    EditText etCycleLength;

    // We’ll store the selected date in a Calendar object
    private Calendar lastPeriodCalendar = null;

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovulation_tracker);

        // Find views
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnCalculateOvulation = findViewById(R.id.btnCalculateOvulation);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvNextPeriod = findViewById(R.id.tvNextPeriod);
        tvOvulationDay = findViewById(R.id.tvOvulationDay);
        tvFertileWindow = findViewById(R.id.tvFertileWindow);
        tvReminderMessage = findViewById(R.id.tvReminderMessage);
        etCycleLength = findViewById(R.id.etCycleLength);

        // 1) Select date button – opens DatePickerDialog
        btnSelectDate.setOnClickListener(v -> openDatePicker());

        // 2) Calculate button
        btnCalculateOvulation.setOnClickListener(v -> calculateOvulationData());
    }

    private void openDatePicker() {
        // Use today's date as default in picker
        final Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                OvulationTrackerActivity.this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Store in Calendar
                    lastPeriodCalendar = Calendar.getInstance();
                    lastPeriodCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth);

                    // Show selected date
                    String formatted = dateFormat.format(lastPeriodCalendar.getTime());
                    tvSelectedDate.setText("Selected: " + formatted);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void calculateOvulationData() {
        // 1) Check if date selected
        if (lastPeriodCalendar == null) {
            Toast.makeText(this, "Please select your last period start date", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2) Get cycle length
        String cycleInput = etCycleLength.getText().toString().trim();
        if (cycleInput.isEmpty()) {
            Toast.makeText(this, "Please enter your average cycle length (e.g. 28)", Toast.LENGTH_SHORT).show();
            return;
        }

        int cycleLength;
        try {
            cycleLength = Integer.parseInt(cycleInput);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid cycle length", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cycleLength < 20 || cycleLength > 40) {
            Toast.makeText(this, "Please enter a realistic cycle length (20–40 days)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Copy the calendar so we don’t change the original
        Calendar nextPeriodCal = (Calendar) lastPeriodCalendar.clone();
        nextPeriodCal.add(Calendar.DAY_OF_MONTH, cycleLength);

        // Ovulation usually ~14 days before next period
        Calendar ovulationCal = (Calendar) nextPeriodCal.clone();
        ovulationCal.add(Calendar.DAY_OF_MONTH, -14);

        // Fertile window: ovulation day -2 to +2
        Calendar fertileStartCal = (Calendar) ovulationCal.clone();
        fertileStartCal.add(Calendar.DAY_OF_MONTH, -2);

        Calendar fertileEndCal = (Calendar) ovulationCal.clone();
        fertileEndCal.add(Calendar.DAY_OF_MONTH, +2);

        // Format dates
        String nextPeriodStr = dateFormat.format(nextPeriodCal.getTime());
        String ovulationStr = dateFormat.format(ovulationCal.getTime());
        String fertileWindowStr = dateFormat.format(fertileStartCal.getTime())
                + " to " + dateFormat.format(fertileEndCal.getTime());

        // Set in TextViews
        tvNextPeriod.setText("Next Period: " + nextPeriodStr);
        tvOvulationDay.setText("Ovulation Day: " + ovulationStr);
        tvFertileWindow.setText("Fertile Window: " + fertileWindowStr);



        Toast.makeText(this, "Ovulation estimate updated", Toast.LENGTH_SHORT).show();
    }
}

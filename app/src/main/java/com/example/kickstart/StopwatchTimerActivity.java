package com.example.kickstart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;


import java.util.ArrayList;

public class StopwatchTimerActivity extends AppCompatActivity {

    // Stopwatch views
    TextView tvTime;
    Button btnStart, btnPause, btnReset, btnLap;
    ListView listViewLaps;

    // Timer views
    EditText etTimerSeconds;
    Button btnStartTimer, btnCancelTimer;
    TextView tvTimerDisplay;

    // Stopwatch variables
    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMillis = 0L;
    private long timeSwapBuff = 0L;
    private long updateTime = 0L;
    private boolean isRunning = false;

    // Laps
    ArrayList<String> lapList = new ArrayList<>();
    ArrayAdapter<String> lapAdapter;
    int lapCount = 0;

    // Timer
    CountDownTimer countDownTimer;
    boolean isTimerRunning = false;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeInMillis = System.currentTimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMillis;

            int seconds = (int) (updateTime / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;

            seconds = seconds % 60;
            minutes = minutes % 60;

            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            tvTime.setText(timeString);

            handler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch_timer);

        // Stopwatch views
        tvTime = findViewById(R.id.tvTime);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        btnLap = findViewById(R.id.btnLap);
        listViewLaps = findViewById(R.id.listViewLaps);

        // Timer views
        etTimerSeconds = findViewById(R.id.etTimerSeconds);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        btnCancelTimer = findViewById(R.id.btnCancelTimer);
        tvTimerDisplay = findViewById(R.id.tvTimerDisplay);

        // Laps adapter
        lapAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lapList);
        listViewLaps.setAdapter(lapAdapter);

        // Stopwatch: Start
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = System.currentTimeMillis();
                    handler.postDelayed(runnable, 0);
                    isRunning = true;
                }
            }
        });

        // Stopwatch: Pause
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    timeSwapBuff += timeInMillis;
                    handler.removeCallbacks(runnable);
                    isRunning = false;
                }
            }
        });

        // Stopwatch: Reset
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeInMillis = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                handler.removeCallbacks(runnable);
                isRunning = false;
                tvTime.setText("00:00:00");

                // Clear laps
                lapList.clear();
                lapAdapter.notifyDataSetChanged();
                lapCount = 0;
            }
        });

        // Stopwatch: Lap
        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    lapCount++;
                    String currentTime = tvTime.getText().toString();
                    String lapText = "Lap " + lapCount + " - " + currentTime;
                    lapList.add(0, lapText);
                    lapAdapter.notifyDataSetChanged();
                }
            }
        });

        // Timer: Start (now takes MINUTES as input)
        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isTimerRunning) {
                    Toast.makeText(StopwatchTimerActivity.this, "Timer already running", Toast.LENGTH_SHORT).show();
                    return;
                }

                String minutesInput = etTimerSeconds.getText().toString().trim();
                if (minutesInput.isEmpty()) {
                    Toast.makeText(StopwatchTimerActivity.this, "Please enter minutes", Toast.LENGTH_SHORT).show();
                    return;
                }

                long totalMinutes;
                try {
                    totalMinutes = Long.parseLong(minutesInput);
                } catch (NumberFormatException e) {
                    Toast.makeText(StopwatchTimerActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (totalMinutes <= 0) {
                    Toast.makeText(StopwatchTimerActivity.this, "Enter a value greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                // convert minutes → milliseconds
                long totalMillis = totalMinutes * 60 * 1000;

                countDownTimer = new CountDownTimer(totalMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        isTimerRunning = true;

                        long secondsLeft = millisUntilFinished / 1000;
                        long minutesLeft = secondsLeft / 60;
                        long remainingSeconds = secondsLeft % 60;

                        String timeLeft = String.format(Locale.getDefault(),
                                "Time left: %02d:%02d (mm:ss)", minutesLeft, remainingSeconds);

                        tvTimerDisplay.setText(timeLeft);
                    }

                    @Override
                    public void onFinish() {
                        isTimerRunning = false;
                        tvTimerDisplay.setText("Time left: 00:00 (mm:ss)");
                        Toast.makeText(StopwatchTimerActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                    }
                }.start();
            }
        });


        // Timer: Cancel
        btnCancelTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    isTimerRunning = false;
                    tvTimerDisplay.setText("Time left: 00:00 min");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop stopwatch updates
        handler.removeCallbacks(runnable);
        isRunning = false;

        // Stop timer if running
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }
}

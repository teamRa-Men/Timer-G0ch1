package teamramen.cs103.yoobeecolleges.timergotchi.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;
import teamramen.cs103.yoobeecolleges.timergotchi.pet.PetActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.record.RecordActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.shop.ShopActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.ListsActivity;

public class TimerActivity extends AppCompatActivity {


    private static long START_TIME_IN_MILLIS = 1000 * 60 * 30;
    private TextView mTextViewCountDown;
    private TextView mTimeLap;
    private TextView mTimeStatus;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonSkip;
    private int breakcount = 1;
    SeekBar seekBar1;
    String lap1 = "", lap2 = "", lap3 = "", laps = "";
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private boolean mBreakRunning = false;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    public TextView pointsView;
    public int points;
    public DatabaseHelper db;

    public Color workColor,breakColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mButtonSkip = findViewById(R.id.button_skip);
        mTimeLap = findViewById(R.id.text_view_lap);
        mTimeStatus = findViewById(R.id.status_view);
        mTimeStatus.setText("Work Time");


        mTextViewCountDown.setTextColor(getResources().getColor(R.color.colorPrimary));



        seekBar1 = findViewById(R.id.seekBar);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (!mTimerRunning) {
                    START_TIME_IN_MILLIS = seekBar1.getProgress() * 36000;
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                }

                updateCountDownText();
                //}

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        updateCountDownText();




        pointsView = findViewById(R.id.pointsview2);
        db = new DatabaseHelper(this);
        points = db.getPoints();
        pointsView.setText(points+" ");

    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                seekBar1.setProgress((int)(mTimeLeftInMillis/36000));
            }
            @Override
            public void onFinish() {
                if (!mBreakRunning)
                {
                    mBreakRunning = true;
                    if (breakcount >= 6)
                    {
                        mTimerRunning = false;
                        mButtonStartPause.setText("Start Long Break");
                        mTimeStatus.setText("Long Break");
                        mTextViewCountDown.setTextColor(getResources().getColor(R.color.colorAccent)); // red
                        mButtonSkip.setVisibility(View.VISIBLE);
                        mTimeLeftInMillis = 1000 * 60 * 25;
                        updateCountDownText();
                        breakcount = 0;
                    }
                    else
                    {
                        mTimerRunning = false;
                        mButtonStartPause.setText("Start Break");
                        mTimeStatus.setText("Short Break " + breakcount);
                        mTextViewCountDown.setTextColor(getResources().getColor(R.color.colorAccent)); // red
                        mButtonSkip.setVisibility(View.VISIBLE);
                        mTimeLeftInMillis = 1000 * 60 * 5;
                        updateCountDownText();
                    }
                    breakcount++;
                }
                /*else
                {
                    View.
                }*/
                mBreakRunning = false;
                playAlarm();
            }
        }.start();
        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
        //System.out.println(START_TIME_IN_MILLIS);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        int milliseconds = (int) (mTimeLeftInMillis / 10) % 100;
        String timeLeftPaused = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        lap3 = lap2;
        lap2 = lap1;
        lap1 = timeLeftPaused;
        laps = lap1 + "\n\n" + lap2 + "\n\n" + lap3;
        mTimeLap.setText(laps);
    }
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mTimerRunning = false;
        mBreakRunning = false;
        lap1 = "";
        lap2 = "";
        lap3 = "";
        laps = "";
        mTimeLap.setText(laps);
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        START_TIME_IN_MILLIS = 1000* 60 * 30;
        breakcount = 1;
        mTimeStatus.setText("Work Time");
        mTextViewCountDown.setTextColor(getResources().getColor(R.color.colorPrimary)); // black
        //System.out.println(START_TIME_IN_MILLIS);
    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        int milliseconds = (int) (mTimeLeftInMillis / 10) % 100;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    public void onSkip(View view) {
        breakcount--;
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mBreakRunning = false;
        mButtonStartPause.setText("Start Lap");
        view.setVisibility(View.INVISIBLE);
        START_TIME_IN_MILLIS = 1000* 60 * 30;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mTimeStatus.setText("Work Time");
        mTextViewCountDown.setTextColor(getResources().getColor(R.color.colorPrimary)); // black
    }

    public void playAlarm() {
        MediaPlayer alarmSound = MediaPlayer.create(this, R.raw.bell);
        alarmSound.start();
    }

    public void toList(View view) {
        Intent i = new Intent(this, ListsActivity.class);
        startActivity(i);
    }
/*
    public void toTimer(View view) {
        Intent i = new Intent(this, TimerActivity.class);
        startActivity(i);
    }*/

    public void toPet(View view) {
        Intent i = new Intent(this, PetActivity.class);
        startActivity(i);
    }

    public void toShop(View view) {
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);
    }

    public void toRecord(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
    }
}

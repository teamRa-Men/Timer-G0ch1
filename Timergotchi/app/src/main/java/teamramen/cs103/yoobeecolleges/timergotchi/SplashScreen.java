package teamramen.cs103.yoobeecolleges.timergotchi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;

import teamramen.cs103.yoobeecolleges.timergotchi.lists.ListsActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.pet.PetActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.timer.TimerActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConstraintLayout back = findViewById(R.id.splash_screen_background);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toApp = new Intent(SplashScreen.this, PetActivity.class);
                startActivity(toApp);
                finish();
            }
        }, SPLASH_TIME);
    }
}

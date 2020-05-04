package teamramen.cs103.yoobeecolleges.timergochi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {
    private static int SPLASH_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toApp = new Intent(Splashscreen.this, Tasks.class);
                startActivity(toApp);
                finish();
            }
        }, SPLASH_TIME);
    }
}

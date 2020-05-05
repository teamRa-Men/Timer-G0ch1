package teamramen.cs103.yoobeecolleges.timergotchi;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.ui.record.RecordFragment;
import teamramen.cs103.yoobeecolleges.timergotchi.ui.tasks.TasksFragment;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_tasks, R.id.navigation_timer, R.id.navigation_pet)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //View decorView = getWindow().getDecorView();
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        NavigationUI.setupWithNavController(navView, navController);




    }





    public void onAddNewTask(View view) {
        TasksFragment.instance.onAddNewTask(view);

    }

    public void onTaskDone(View view) {
        TasksFragment.instance.onTaskDone(view);

    }

    public void onClearRecord(View view) {
        try {
            RecordFragment.instance.onClearRecord();
        }catch (Exception e){

        }
    }
}





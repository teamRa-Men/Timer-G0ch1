package teamramen.cs103.yoobeecolleges.timergotchi;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    float taskMove = 0;
    int taskCount = 0;
    ArrayList<Integer> taskID = new ArrayList<>();
    ArrayList<Integer> taskNameID = new ArrayList<>();
    int newTaskNameID;

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


        initTasks();

    }

    public void initTasks(){

        taskID.add(R.id.task0);
        taskNameID.add(R.id.taskname0);
        taskID.add(R.id.task1);
        taskNameID.add(R.id.taskname1);
        taskID.add(R.id.task2);
        taskNameID.add(R.id.taskname2);
        taskID.add(R.id.task3);
        taskNameID.add(R.id.taskname3);
        taskID.add(R.id.task4);
        taskNameID.add(R.id.taskname4);
        taskID.add(R.id.task5);
        taskNameID.add(R.id.taskname5);

        newTaskNameID = R.id.taskname;

    }


    public void onAddNewTask(View view) {

        if (taskCount < taskID.size()) {
            if(!((EditText)findViewById(newTaskNameID)).getText().toString().isEmpty()) {
                View v = findViewById(taskID.get(taskCount));
                TextView t = findViewById(taskNameID.get(taskCount));

                try {
                    v.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    System.out.println(e);
                }


                taskMove += v.getHeight();

                t.setText(((EditText) findViewById(newTaskNameID)).getText().toString());
                ObjectAnimator animation = ObjectAnimator.ofFloat(v, "translationY", taskMove);
                animation.setDuration(200);
                animation.start();
                taskCount++;
                ((EditText) findViewById(newTaskNameID)).setText("");
                findViewById(R.id.todolist).requestFocus();
            }
            else{
                ((EditText)findViewById(newTaskNameID)).requestFocus();
            }
        }
    }

    public void onTaskDone(View view) {
        View v = (View)view.getParent();
        System.out.println(v.getId());
        int doneIndex = 0;
        for(int i = 0; i < taskID.size();i++){
            if(v.getId() == taskID.get(i)){
                System.out.println("found");
                doneIndex = i;
                break;
            }
        }
        v.setVisibility(View.INVISIBLE);
        taskMove -= v.getHeight();
        for(int i = doneIndex+1; i < taskID.size();i++){
            View w = findViewById(taskID.get(i));
            ObjectAnimator animation = ObjectAnimator.ofFloat(w, "translationY", i*v.getHeight());
            animation.setDuration(200);
            animation.start();
        }
        taskID.remove(doneIndex);
        taskID.add(v.getId());
        taskCount--;
    }
}





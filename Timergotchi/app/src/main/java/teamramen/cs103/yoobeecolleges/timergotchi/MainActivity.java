package teamramen.cs103.yoobeecolleges.timergotchi;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

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

        taskID.add(R.id.task0);
        taskID.add(R.id.task1);
        taskID.add(R.id.task2);
        taskID.add(R.id.task3);
        taskID.add(R.id.task4);
        taskID.add(R.id.task5);

    }




    public void onAddNewTask(View view) {
/*


        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = vi.inflate(R.layout.taskprefab,null);
        v.setId(ViewCompat.generateViewId());


        todoList =  findViewById(R.id.todolist);
        todoList.addView(v,0,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tasks.add(v);
        /*taskMove += 100;
        ObjectAnimator animation = ObjectAnimator.ofFloat(v,"translationY",taskMove);
        animation.setDuration(500);
        animation.start();*/
        if (taskCount < taskID.size()) {
            View v = findViewById(taskID.get(taskCount));
            System.out.println(v);

            try {
                v.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                System.out.println(e);
            }


            taskMove += v.getHeight();
            ObjectAnimator animation = ObjectAnimator.ofFloat(v, "translationY", taskMove);
            animation.setDuration(200);
            animation.start();
            taskCount++;
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





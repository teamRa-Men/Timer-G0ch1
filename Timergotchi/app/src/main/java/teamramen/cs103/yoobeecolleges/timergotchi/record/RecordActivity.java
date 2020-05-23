package teamramen.cs103.yoobeecolleges.timergotchi.record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;
import teamramen.cs103.yoobeecolleges.timergotchi.pet.PetActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.shop.ShopActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.ListsActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.timer.TimerActivity;

public class RecordActivity extends AppCompatActivity {
    TextView finCount, points;
    TextView tasksLeft, timeSpent;
    ArrayList<FinishedTask> finishedTasks;
    ImageView bar0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        finCount = findViewById(R.id.fincount);
        points = findViewById(R.id.recordpoints);
        tasksLeft = findViewById(R.id.leftcount);
        //timeSpent = findViewById(R.id.timespent);

        DatabaseHelper db = new DatabaseHelper(this);
        finishedTasks = db.getFinished();

        for(int i = 0; i< finishedTasks.size();i++){
            System.out.println(finishedTasks.get(i).name);
        }

        finCount.setText(finishedTasks.size()+" tasks done");
        tasksLeft.setText(db.getTasksLeft()+ " tasks left");

        points.setText(db.getPoints() + " g");


    }
/*
    void expandTask(){
        try {
            int taskHeight = ListsActivity.instance.taskHeight;
            ViewGroup.LayoutParams params = bar0.getLayoutParams();

            params.height = (int)(taskHeight * );
            bar0.setLayoutParams(params);
        }
        catch (Exception e){

        }
    }*/

    public void toList(View view) {
        Intent i = new Intent(this, ListsActivity.class);
        startActivity(i);
    }

    public void toTimer(View view) {
        Intent i = new Intent(this, TimerActivity.class);
        startActivity(i);
    }

    public void toPet(View view) {
        Intent i = new Intent(this, PetActivity.class);
        startActivity(i);
    }

    public void toShop(View view) {
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);
    }
/*
    public void toRecord(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
    }*/
}

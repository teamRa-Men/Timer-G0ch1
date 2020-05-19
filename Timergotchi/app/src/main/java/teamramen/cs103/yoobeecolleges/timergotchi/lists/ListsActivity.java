package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;
import teamramen.cs103.yoobeecolleges.timergotchi.pet.PetActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.record.RecordActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.shop.ShopActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.timer.TimerActivity;

public class ListsActivity extends AppCompatActivity {

    ListPagerAdapter listAdapter;
    ViewPager pager;
    TabLayout listTabs;

    public TasksFragment todoList;



    public static ListsActivity instance;
    ArrayList<MediaPlayer> doneSounds = new ArrayList<MediaPlayer>();

    public TextView pointsView;
    public int points;
    DatabaseHelper db;

    View editMenu,deletePopup, calendarPopup;
    CalendarView calendar;
    int dueDay,dueMonth,dueYear;
    TextView editName, dueDate, dueTime;
    int[] repeat = new int[]{0,0,0,0,0,0,0};

    boolean editing, deleting;
    Task taskEditing;
    View[] days;
    public int taskHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        db = new DatabaseHelper(this);

        listAdapter = new ListPagerAdapter(this.getSupportFragmentManager());
        listTabs = findViewById(R.id.listtabs);
        editMenu = findViewById(R.id.edit);
        deletePopup = findViewById(R.id.deletepopup);

        todoList = new TasksFragment( db);
        listAdapter.addList(todoList, "TODO",0);

        pager = findViewById(R.id.listpager);
        pager.setAdapter(listAdapter);
        listTabs.setupWithViewPager(pager);


        MediaPlayer completed = MediaPlayer.create(this, R.raw.completed);
        MediaPlayer itemize = MediaPlayer.create(this, R.raw.itemize);
        MediaPlayer okay =MediaPlayer.create(this, R.raw.okay);
        MediaPlayer tada =MediaPlayer.create(this, R.raw.tada);
        MediaPlayer yeah =MediaPlayer.create(this, R.raw.yeah);
        MediaPlayer bell =MediaPlayer.create(this, R.raw.bell);
        doneSounds.add(completed);
        doneSounds.add(itemize);
        doneSounds.add(okay);
        doneSounds.add(tada);
        doneSounds.add(yeah);
        doneSounds.add(bell);

        points = 0;
        pointsView = findViewById(R.id.pointsview);
        ////system.out.println(pointsView+" points" + pointsView.getText().toString());
        points = db.getPoints();
        pointsView.setText(points+" g");




        days = new View[]{ findViewById(R.id.editsun),
                findViewById(R.id.editmon),
                findViewById(R.id.edittue),
                findViewById(R.id.editwed),
                findViewById(R.id.editthu),
                findViewById(R.id.editfri),
                findViewById(R.id.editsat),
                findViewById(R.id.editsun2),
                findViewById(R.id.editmon2),
                findViewById(R.id.edittue2),
                findViewById(R.id.editwed2),
                findViewById(R.id.editthu2),
                findViewById(R.id.editfri2),
                findViewById(R.id.editsat2)};
        editName = findViewById(R.id.editname);
        calendar = findViewById(R.id.calendarView);
        dueDate = findViewById(R.id.dueDate);
        dueTime = findViewById(R.id.dueTime);


        taskHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, getResources().getDisplayMetrics());

    }



    //add task to list
    public void onAddTask(View view) { todoList.onAddTask(); }

    //pass to timer
    public void onDo(View view) {
        if(!editing) {
            todoList.onDoTask(view);
        }
    }

    /*****************************************************************************************
     * Edit Task
     *****************************************************************************************/
    public void onEdit(View view) {
        editing = true;
        editMenu.setVisibility(View.VISIBLE);
        pager.setVisibility(View.INVISIBLE);
        listTabs.setVisibility(View.INVISIBLE);

        taskEditing = todoList.findByView(view);
        repeat = taskEditing.repeat;
        editName.setText(taskEditing.name);
        for(int i = 0; i < 7;i++){

            if(repeat[i] == 1) {
                days[i].setVisibility(View.INVISIBLE);

            }
            else{
                days[i].setVisibility(View.VISIBLE);

            }
        }
        setupCalendar();
        if(taskEditing.dueDate>0) {
            Date d = new Date((long) taskEditing.dueDate);
            dueDate.setText(d.getDate() + "/" + (d.getMonth()+1)+"/"+(d.getYear()));
            dueDay = d.getDate();
            dueMonth = d.getMonth();
            dueYear = d.getYear();
            calendar.setDate((long)(d.getTime()-5.99582088e13));
        }
        else{
            dueDate.setText("");
        }
    }

    public void onEditCancel(View view){
        closeEditMenu();
    }

    public void onEditOK(View view){

            if (!editName.getText().toString().isEmpty()) {
                taskEditing.setName(editName.getText().toString());
            }

            if (dueDate.getText().toString().isEmpty()) {
                taskEditing.setDueDate(0);

            } else {
                Date d = new Date();
                d.setDate(dueDay);
                d.setMonth(dueMonth);
                d.setYear(dueYear);
                taskEditing.setDueDate(d.getTime());
            }
            taskEditing.setRepeat(repeat);
            taskEditing.showOverdue();
            taskEditing.showFinished();


        System.out.println(taskEditing.status);
        if(!taskEditing.isRepeating() && taskEditing.status!=0){
            System.out.println("deledting");
            todoList.deleteTask(taskEditing);
        }
        closeEditMenu();
    }

    //delete task
    public void onDeletePopup(View view) {
        deleting = true;
        deletePopup.setVisibility(View.VISIBLE);
    }
    public void onDeleteFromMenu(View view) {
       todoList.deleteTask(taskEditing);
        closeDeletePopup();
        closeEditMenu();
    }


    public void onDeleteCancel(View view) {
        closeDeletePopup();
    }

    //close popups
    void closeEditMenu(){
        if(!deleting) {
            editing = false;
            editMenu.setVisibility(View.INVISIBLE);
            pager.setVisibility(View.VISIBLE);
            listTabs.setVisibility(View.VISIBLE);
            taskEditing = null;
        }

    }
    void closeDeletePopup(){
        deleting = false;
        deletePopup.setVisibility(View.INVISIBLE);
    }

    public void onRepeat(View view){
        if (view.getId() == days[0].getId() || view.getId() == days[7].getId()) {
            toggleDay(0);
        }
        else if (view.getId() == days[1].getId() || view.getId() == days[8].getId()) {
            toggleDay(1);
        }
        else if (view.getId() == days[2].getId() || view.getId() == days[9].getId()) {
            toggleDay(2);
        }
        else if (view.getId() == days[3].getId() || view.getId() == days[10].getId()) {
            toggleDay(3);
        }
        else if (view.getId() == days[4].getId() || view.getId() == days[11].getId()) {
            toggleDay(4);
        }
        else if (view.getId() == days[5].getId() || view.getId() == days[12].getId()) {
            toggleDay(5);
        }
        else if (view.getId() == days[6].getId() || view.getId() == days[13].getId()) {
            toggleDay(6);
        }



    }

    void toggleDay(int day) {
        //System.out.println(day);
        if(repeat[day]==1) {
            days[day].setVisibility(View.VISIBLE);
            repeat[day] = 0;

        }
        else{
            days[day].setVisibility(View.INVISIBLE);
            repeat[day] = 1;
            System.out.println("repeat"+day);
        }
    }



    boolean isRepeating(){
        return repeat[0]+repeat[1]+repeat[2]+repeat[3]+repeat[4]+repeat[5]+repeat[6] > 0;
    }

    /*****************************************************************************************/
    //on finished task
    public void onTaskDone(View view) {
        if(!editing) {
            int pick = (int) (Math.random() * doneSounds.size());
            try {
                doneSounds.get(pick).setVolume(0.2f, 0.2f);
                doneSounds.get(pick).start();
                //System.out.println(doneSounds.get(pick)+" " + pick);
            } catch (Exception e) {
                System.out.println("null sound " + pick);
            }
            todoList.onTaskDone(view);
            awardPoints();
        }
    }




    //TODO streaks, time used, deadline met
    public void awardPoints(){
        points += 100 + (int)(Math.random()*50);
        pointsView.setText(points+" g");
        db.setPoints(points);
    }






    void setupCalendar() {

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dueDay = dayOfMonth;
                dueMonth = month;
                dueYear = year;

                dueDate.setText(dueDay + "/" + (dueMonth+1) + "/" + dueYear);
            }
        });
    }

    public void clearDueDate(View view){
        dueDate.setText("");
        taskEditing.setDueDate(0);
    }
    public void clearDueTime(View view){
        dueTime.setText("");
        taskEditing.setDueTime(0);
    }

    public void setDueDate(View view){

    }
    public void setDueTime(View view){

    }

    public void setLabel(View view) {
        taskEditing.setLabel(-1);
    }
    public void setLabel0(View view) {
        taskEditing.setLabel(0);
    }
    public void setLabel1(View view) {
        taskEditing.setLabel(1);
    }
    public void setLabel2(View view) {
        taskEditing.setLabel(2);
    }
    public void setLabel3(View view) {
        taskEditing.setLabel(3);
    }
    public void setLabel4(View view) {
        taskEditing.setLabel(4);
    }




    /*****************************************************************************************
     * Navigation
     *****************************************************************************************/

    public void toTimer(View view) {
        if(!editing) {
            Intent i = new Intent(this, TimerActivity.class);
            startActivity(i);
        }
    }

    public void toPet(View view) {
        if(!editing) {
            Intent i = new Intent(this, PetActivity.class);
            startActivity(i);
        }
    }

    public void toShop(View view){
        if (!editing) {

            Intent i = new Intent(this, ShopActivity.class);
            startActivity(i);
        }
    }

    public void toRecord(View view) {
        if(!editing) {

            Intent i = new Intent(this, RecordActivity.class);
            startActivity(i);
        }
    }



}





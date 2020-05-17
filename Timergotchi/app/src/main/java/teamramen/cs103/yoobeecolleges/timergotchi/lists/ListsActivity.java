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

import java.util.ArrayList;

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

    public ArrayList<TasksFragment> lists = new ArrayList<TasksFragment>();
    FinishedFragment finishedList;
    public static ListsActivity instance;
    ArrayList<MediaPlayer> doneSounds = new ArrayList<MediaPlayer>();

    public TextView pointsView;
    public int points;
    DatabaseHelper db;

    View editMenu,deletePopup, calendarPopup;
    CalendarView calendar;
    TextView editName, dueDate;
    int[] repeat = new int[]{0,0,0,0,0,0,0};
    Task currentEditTask;

    boolean editing, deleting;
    Task taskEditing;
    View[] days;
    public int taskHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);



        listAdapter = new ListPagerAdapter(this.getSupportFragmentManager());
        listTabs = findViewById(R.id.listtabs);
        editMenu = findViewById(R.id.edit);
        deletePopup = findViewById(R.id.deletepopup);



        for(int i = 0; i < 10;i++) {
            lists.add(new TasksFragment(i, db));
            listAdapter.addList(lists.get(i), "Morning",i);
        }

        pager = findViewById(R.id.listpager);
        pager.setAdapter(listAdapter);
        listTabs.setupWithViewPager(pager);

        listTabs.getTabAt(0).setIcon(R.drawable.listicon);
        for(int i = 1; i < 10;i++) {
            listTabs.getTabAt(i).setIcon(R.drawable.listiconoff);
        }




        listTabs.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(pager){
                    @Override
                    public void onTabSelected(@NonNull TabLayout.Tab tab) {
                        super.onTabSelected(tab);

                        for(int i = 0; i < lists.size();i++){
                            if(tab.getPosition() == i){
                                listTabs.getTabAt(i).setIcon(R.drawable.listicon);
                            }
                            else{
                                listTabs.getTabAt(i).setIcon(R.drawable.listiconoff);
                            }
                        }
                    }


                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );



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

        instance = this;


        days = new View[]{ findViewById(R.id.editsun),
                findViewById(R.id.editmon),
                findViewById(R.id.edittue),
                findViewById(R.id.editwed),
                findViewById(R.id.editthu),
                findViewById(R.id.editfri),
                findViewById(R.id.editsat)};
        editName = findViewById(R.id.editname);
        calendar = findViewById(R.id.calendarView);


        taskHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, getResources().getDisplayMetrics());
    }



    //add task to list
    public void onAddTask(View view) { lists.get(pager.getCurrentItem()).onAddTask(); }

    //pass to timer
    public void onDo(View view) { }

    /*****************************************************************************************
     * Edit Task
     *****************************************************************************************/
    public void onEdit(View view) {
        editing = true;
        editMenu.setVisibility(View.VISIBLE);
        pager.setVisibility(View.INVISIBLE);
        listTabs.setVisibility(View.INVISIBLE);

        taskEditing = (lists.get(pager.getCurrentItem()).findByView(view));
        repeat = taskEditing.repeat;
        editName.setText(taskEditing.name+"");
        for(int i = 0; i < 7;i++){
            System.out.println(taskEditing.repeat[i]);
            if(repeat[i] == 1) {
                days[i].setVisibility(View.INVISIBLE);

            }
            else{
                days[i].setVisibility(View.VISIBLE);

            }
        }
        System.out.println("date" + taskEditing.dueDate);
    }
    public void onEditCancel(View view){
        closeEditMenu();
    }
    public void onEditOK(View view){
        if (!editName.getText().toString().isEmpty()) {
            taskEditing.setName(editName.getText().toString());
        }

        if(!isRepeating()){

            if(taskEditing.status==Task.COMPLETED) {
                lists.get(pager.getCurrentItem()).onTaskDone(taskEditing);
            }
        }

        taskEditing.setRepeat(repeat);
        //set task due date
        try {

            taskEditing.dueDate = calendar.getDate();
            System.out.println("date" + taskEditing.dueDate);
        } catch (Exception e) {
            System.out.println("no date" );
        }




        closeEditMenu();
    }

    //delete task
    public void onDeletePopup(View view) {

        deleting = true;
        deletePopup.setVisibility(View.VISIBLE);

    }
    public void onDelete(View view) {
        lists.get(pager.getCurrentItem()).deleteTask(taskEditing);
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
        TextView day = ((TextView)view);
        switch (day.getText().toString()){
            case "Sun":toggleDay(0);break;
            case "Mon":toggleDay(1);break;
            case "Tue":toggleDay(2);break;
            case "Wed":toggleDay(3);break;
            case "Thu":toggleDay(4);break;
            case "Fri":toggleDay(5);break;
            case "Sat":toggleDay(6);break;
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
            lists.get(pager.getCurrentItem()).onTaskDone(view);
            awardPoints();
        }
    }


    //TODO streaks, time used, deadline met
    public void awardPoints(){
        points += 100 + (int)(Math.random()*50);
        pointsView.setText(points+" g");
        db.setPoints(points);
    }



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





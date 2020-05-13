package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    public int currentList = 0;
    public ArrayList<TasksFragment> lists = new ArrayList<TasksFragment>();
    FinishedFragment finishedList;
    public static ListsActivity instance;
    ArrayList<MediaPlayer> doneSounds = new ArrayList<MediaPlayer>();

    public TextView pointsView;
    public int points;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);



        listAdapter = new ListPagerAdapter(this.getSupportFragmentManager());
        listTabs = findViewById(R.id.listtabs);



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
    }



    //add task to list
    public void onAddTask(View view) { lists.get(pager.getCurrentItem()).onAddTask(); }

    //pass to timer
    public void onDo(View view) { }

    //open edit task menu
    public void onEdit(View view) { }

    //on finished task
    public void onTaskDone(View view) {
        int pick =  (int)(Math.random()*doneSounds.size());
        try {
            doneSounds.get(pick).setVolume(0.2f,0.2f);
            doneSounds.get(pick).start();
            //System.out.println(doneSounds.get(pick)+" " + pick);
        }catch (Exception e){
            System.out.println("null sound " + pick);
        }
        lists.get(pager.getCurrentItem()).onTaskDone(view);
        awardPoints();
    }


    //TODO streaks, time used, deadline met
    public void awardPoints(){
        points += 100 + (int)(Math.random()*50);
        pointsView.setText(points+" g");
        db.setPoints(points);
    }


/*
    //editing task
    public void onEditTask(View view) {
        TasksFragment.instance.onEditTask(view);
    }
    public void closeEditTask(View view) {TasksFragment.instance.closeEditTask(view); }
    public void onDeleteTask(View view) {TasksFragment.instance.deleteEditTask(); }

    //set repeat days
    public void onSun(View view) {TasksFragment.instance.repeatSun(); }
    public void onMon(View view) {TasksFragment.instance.repeatMon(); }
    public void onTue(View view) {TasksFragment.instance.repeatTue(); }
    public void onWed(View view) {TasksFragment.instance.repeatWed(); }
    public void onThu(View view) {TasksFragment.instance.repeatThu(); }
    public void onFri(View view) {TasksFragment.instance.repeatFri(); }
    public void onSat(View view) {TasksFragment.instance.repeatSat(); }

*/


    public void onClearList(View view) {
        try {

            switch(pager.getCurrentItem()){
                case 0:lists.get(0).onClearList();break;
                case 1:lists.get(1).onClearList();break;
                case 2:lists.get(2).onClearList();break;
                case 3:lists.get(3).onClearList();break;
                case 4:finishedList.onClearList();break;
            }

        }catch (Exception e){

        }
    }

/*
    public void toList(View view) {
        Intent i = new Intent(this, ListsActivity.class);
        startActivity(i);
    }*/

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

    public void toRecord(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
    }
}





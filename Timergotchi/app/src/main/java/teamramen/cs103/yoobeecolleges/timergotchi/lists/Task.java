package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.app.ListActivity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.Date;
import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;

public class Task{
    public static int CURRENT=0, DOING = 1;
    public long id;

    public int index, list;
    public String name;
    public  TextView nameView,showDueDate,showRepeat;
    public View container,doneshadow,left,right;
    public ImageView dobutton,donebutton,pausebutton, finishedbutton;

    public int colorCode = 0;

    public int[] repeat;
    DatabaseHelper db;

    //TODO

    public float dueDate,status;


    public Task(String name,int index, int list,DatabaseHelper db){
        this.name = name;
        this.index = index;
        this.status = Task.CURRENT;
        this.list = list;
        repeat = new int[]{0,0,0,0,0,0,0};
        dueDate = 0;
        id = db.addTask(name,index,list);
        this.db = db;

    }

    //Database task handle
    public Task(int id, String name,int index, float status,  int list,float dueDate,int[] repeat, DatabaseHelper db){
        this.id = id;
        this.name = name;
        this.index = index;
        this.status = status;
        this.list = list;
        this.repeat = repeat;
        this.dueDate = dueDate;
        this.db = db;




        Date today = new Date((long)(System.currentTimeMillis()+5.99582088e13) );
        Date due = new Date((long)dueDate);
        System.out.println(today.getDate() + " " + today.getMonth() + " " + today.getYear()+ "today");
        System.out.println(due.getDate() + " " + due.getMonth() + " " + due.getYear());

        int dayOfTheWeek = today.getDay();

        //if repeat day allow to be redone
        for(int i =0 ; i < 7; i++){
            if(repeat[i] == 1){
                if(i==dayOfTheWeek && (System.currentTimeMillis() - status) > 9e7){
                    this.status = 0;
                    showFinished();
                }
            }
        }

    }

    public void showOverdue(){
        System.out.println(dueDate + " compare dates " +System.currentTimeMillis());
        if(dueDate<(System.currentTimeMillis()+5.99582088e13) && dueDate > 0){
            //showoverdue
            nameView.setTextColor(Color.RED);
        }
        else{
            nameView.setTextColor(ListsActivity.instance.getResources().getColor(R.color.textColor));
        }
    }

    public void setName(String n){
        name = n;
        nameView.setText(n);
        update();
    }

    public void moveTo(int i){
        index = i;
        update();
    }

    public void done(){
        status=System.currentTimeMillis();
        showFinished();


        System.out.println(status + " currentdate");
        if(!isRepeating()){
            list = -1;
        }


        update();

    }

    public void delete(){
        db. remove(id);
    }


    //repeat task
    public void setRepeat(int[] repeat){
        System.out.println(repeat.toString());
        this.repeat = repeat;
        showTask();

        update();
    }

    public boolean isRepeating() {
        return repeat[0]+repeat[1]+repeat[2]+repeat[3]+repeat[4]+repeat[5]+repeat[6] > 0;
    }


    public void setId(int id){ this.id = id; }

    void update(){
        db.updateData(id, name, index, status, list, dueDate, repeat);
    }


    public void showTask(){
        /*
        if(dueDate==0 && !isRepeating()){
            contractTask();
            showDueDate.setText("");
            showRepeat.setText("");
        }
        else{
            expandTask();
            if(dueDate > 0) {
                Date due = new Date((long) dueDate);
                showDueDate.setText("Due " + due.getDate() + "/" + (due.getMonth() + 1) + "/" + due.getYear());
            }

            String s = "";
            if(repeat[0]==1){s= s+"Sun  ";}
            if(repeat[1]==1){s= s+"Mon  ";}
            if(repeat[2]==1){s= s+"Tue  ";}
            if(repeat[3]==1){s= s+"Wed  ";}
            if(repeat[4]==1){s= s+"Thu  ";}
            if(repeat[5]==1){s= s+"Fri ";}
            if(repeat[6]==1){s= s+"Sat ";}

            showRepeat.setText(s);


        }
        */

    }


    void showFinished(){
        try {
            if(status > 1) {
                donebutton.setVisibility(View.INVISIBLE);
                doneshadow.setVisibility(View.VISIBLE);
                dobutton.setVisibility(View.INVISIBLE);
                pausebutton.setVisibility(View.INVISIBLE);
                finishedbutton.setVisibility(View.VISIBLE);
            }
            else{
                donebutton.setVisibility(View.VISIBLE);
                doneshadow.setVisibility(View.INVISIBLE);
                if(status == 0) {
                    dobutton.setVisibility(View.VISIBLE);
                    pausebutton.setVisibility(View.INVISIBLE);
                }else{
                    dobutton.setVisibility(View.INVISIBLE);
                    pausebutton.setVisibility(View.VISIBLE);
                }
                finishedbutton.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){

        }
    }


    void expandTask(){
        try {
            int taskHeight = ListsActivity.instance.taskHeight;
            ViewGroup.LayoutParams params = nameView.getLayoutParams();
            System.out.println(params.height);
            params.height = (int)(taskHeight * 1.5f);
            nameView.setLayoutParams(params);
            params = left.getLayoutParams();
            params.height = (int)(taskHeight * 1.5f);
            left.setLayoutParams(params);
            params = right.getLayoutParams();
            params.height = (int)(taskHeight * 1.5f);
            right.setLayoutParams(params);
        }
        catch (Exception e){

        }
    }

    void contractTask(){
        try {

            int taskHeight = ListsActivity.instance.taskHeight;
            ViewGroup.LayoutParams params = nameView.getLayoutParams();
            params.height = taskHeight;
            nameView.setLayoutParams(params);
            params = left.getLayoutParams();
            params.height = taskHeight;
            left.setLayoutParams(params);
            params = right.getLayoutParams();
            params.height = taskHeight;
            right.setLayoutParams(params);
        }
        catch (Exception e){

        }
    }

    public void setDueDate(float d){
        System.out.println(d - dueDate+"datechanged");
        this.dueDate = d;
        update();
        Date dd = new Date((long)dueDate);

        int dayOfTheWeek = dd.getDay();

    }

    public void doTask(){




        if(status == 0){
            status =1;
            dobutton.setVisibility(View.INVISIBLE);
            pausebutton.setVisibility(View.VISIBLE);
        }
        else if(status == 1){
            status =0;
            dobutton.setVisibility(View.VISIBLE);
            pausebutton.setVisibility(View.INVISIBLE);
        }
    }

    public void toggleColor(){
        if(colorCode == 1) {
            pausebutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorAccent));
            donebutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorAccent));
            dobutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorAccent));
            finishedbutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorAccent));
            colorCode = 0;
        }
        else{
            pausebutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorPastelRed));
            donebutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorPastelRed));
            dobutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorPastelRed));
            finishedbutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorPastelRed));
            colorCode =1;
        }
    }
}
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

import java.util.Comparator;
import java.util.Date;
import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;

public class Task implements Comparable<Task>{
    public static int CURRENT=0;
    public int order;
    public long id;
    public String name;
    public  TextView nameView,showduedate;
    public View container,left,right;
    public ImageView dobutton,donebutton, finishedbutton, showrepeat;
    public ImageView[] labelViews;


    public int[] repeat,labels;
    DatabaseHelper db;

    //TODO

    public float dueDate, dueTime, timeCreated,status;


    public Task(String name,int order,DatabaseHelper db)

    {
        this.name = name;
        this.order = order;
        this.status = Task.CURRENT;
        repeat = new int[]{0,0,0,0,0,0,0};
        labels = new int[]{0,0,0,0,0,0,0};
        dueDate = 0;
        id = db.addTask(name,order);
        this.db = db;

        timeCreated = (long)(System.currentTimeMillis()+5.99582088e13);
    }
    //id name status created dueTime dueDate smtwtfs 0123456
    //Database task handle
    public Task(int id, String name, float status, float timeCreated, float dueTime,float dueDate,int[] repeat,int[] labels, int order, DatabaseHelper db){
        this.id = id;
        this.name = name;
        this.status = status;
        this.timeCreated = timeCreated;
        this.dueTime = dueTime;
        this.dueDate = dueDate;
        this.repeat = repeat;
        this.labels = labels;
        this.order = order;
        this.db = db;

        Date today = new Date((long)(System.currentTimeMillis()+5.99582088e13) );
        Date due = new Date((long)dueDate);
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


        //check date      todo checktime
        if((dueDate)<(System.currentTimeMillis()+5.99582088e13) && dueDate > 0){
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


    public void done(){
        status=System.currentTimeMillis();
        showFinished();


        System.out.println(status + " currentdate");

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

    //id name status created dueTime dueDate smtwtfs 0123456
    public void setId(int id){ this.id = id; }

    void update(){
        db.updateData(id, name, status, dueTime, dueDate, repeat, labels,order);
    }


    public void showTask(){

        if(isRepeating() && status>0){
            String s = "";
            if(repeat[0]==1){s= s+"Sun ";}
            if(repeat[1]==1){s= s+"Mon ";}
            if(repeat[2]==1){s= s+"Tue ";}
            if(repeat[3]==1){s= s+"Wed ";}
            if(repeat[4]==1){s= s+"Thu ";}
            if(repeat[5]==1){s= s+"Fri ";}
            if(repeat[6]==1){s= s+"Sat";}

            showduedate.setText(s);


        }
        if(dueDate > 0 && status == 0) {
            Date due = new Date((long) dueDate);

            String s = "due "+due.getDate();
            switch (due.getMonth()){
                case 0: s+= " Jan";break;
                case 1: s+= " Feb";break;
                case 2: s+= " Mar";break;
                case 3: s+= " Apr";break;
                case 4: s+= " May";break;
                case 5: s+= " Jun";break;
                case 6: s+= " Jul";break;
                case 7: s+= " Aug";break;
                case 8: s+= " Sep";break;
                case 9: s+= " Oct";break;
                case 10: s+= " Nov";break;
                case 11: s+= " Dec";break;

            }

            showduedate.setText(s);
        }
        else if(status == 0){
            showduedate.setText("");
        }





        if(isLabelled() || dueDate > 0){
            expandTask();

        }
        else{
            contractTask();
        }
        for(int i = 0; i < 7; i++){
            if(labels[i] == 1){

                labelViews[i].setVisibility(View.VISIBLE);

            }
            else{

                labelViews[i].setVisibility(View.INVISIBLE);
                //turn off label
            }

        }

    }


    void showFinished(){
        try {
            if(status > 0) {
                donebutton.setVisibility(View.INVISIBLE);

                dobutton.setVisibility(View.INVISIBLE);
                if(isRepeating()){
                    showrepeat.setVisibility(View.VISIBLE);

                }
                else {
                    showrepeat.setVisibility(View.INVISIBLE);

                }
                finishedbutton.setVisibility(View.VISIBLE);
            }
            else{
                donebutton.setVisibility(View.VISIBLE);

                dobutton.setVisibility(View.VISIBLE);

                showrepeat.setVisibility(View.INVISIBLE);
                finishedbutton.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){

        }
    }


    void expandTask(){
        try {
            int taskHeight = ListsActivity.instance.taskHeight;
            ViewGroup.LayoutParams params = nameView.getLayoutParams();

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


        this.dueDate = d;
        update();
        showTask();

    }

    public void setDueTime(float t){

        this.dueTime = t;
        update();

    }


    public void doTask(){
        //pass to timer
        System.out.println("DOOOOOOOOOOOOOOOOOOOOOO");
    }

public void setLabels(int[] labels){
        labels = labels;
        showTask();
        update();
}



    boolean isLabelled(){
        return labels[0]+labels[1]+labels[2]+labels[3]+labels[4]+labels[5]+labels[6] > 0;
    }

    @Override
    public int compareTo(Task o) {
        if(order > o.order){
            return 1;
        }

        if(order < o.order){
            return -1;
        }
        return 0;
    }
}
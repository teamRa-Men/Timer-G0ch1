package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.view.View;
import android.widget.TextView;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;

public class Task{
    public static int CURRENT=0, COMPLETED=1;
    public long id;

    public int index, status, list;
    public String name;
    public  TextView nameView;
    public View container,dobutton,donebutton,doneshadow;


    public int[] repeat;
    DatabaseHelper db;

    //TODO

    public float dueDate;


    public Task(String name,int index, int list,DatabaseHelper db){
        this.name = name;
        this.index = index;
        this.status = Task.CURRENT;
        this.list = list;
        repeat = new int[]{0,0,0,0,0,0};
        dueDate = -1;
        id = db.addTask(name,index,list);
        this.db = db;
    }

    //Database task handle
    public Task(int id, String name,int index, int status, int list, int[] repeat, DatabaseHelper db){
        this.id = id;
        this.name = name;
        this.index = index;
        this.status = status;
        this.list = list;
        this.repeat = repeat;
        dueDate = -1;
        this.db = db;
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
        showFinished();
        status=COMPLETED;
        update();

    }

    void showFinished(){
        try {
            dobutton.setVisibility(View.INVISIBLE);
            donebutton.setVisibility(View.INVISIBLE);
            doneshadow.setVisibility(View.VISIBLE);
        }catch (Exception e){

        }
    }

    public void delete(){
        db. remove(id);
    }


    //repeat task
    public void setRepeat(int[] repeat){
        System.out.println(repeat.toString());
        this.repeat = repeat;
        update();
    }



    public void setId(int id){ this.id = id; }

    void update(){
        db.updateData(id, name, index, status, list, dueDate, repeat);
    }

}
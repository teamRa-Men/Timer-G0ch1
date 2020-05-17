package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.app.ListActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;

public class Task{
    public static int CURRENT=0, COMPLETED=1;
    public long id;

    public int index, status, list;
    public String name;
    public  TextView nameView;
    public View container,dobutton,donebutton,doneshadow,left,right;


    public int[] repeat;
    DatabaseHelper db;

    //TODO

    public float dueDate;


    public Task(String name,int index, int list,DatabaseHelper db){
        this.name = name;
        this.index = index;
        this.status = Task.CURRENT;
        this.list = list;
        repeat = new int[]{0,0,0,0,0,0,0};
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
        if(isRepeated()){
            list = -1;
        }
        update();

    }

    void showFinished(){
        try {
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
        if(isRepeated()){
            expandTask();
        }
        else{
            contractTask();
        }

        update();
    }

    public boolean isRepeated() {
        return repeat[0]+repeat[1]+repeat[2]+repeat[3]+repeat[4]+repeat[5]+repeat[6] > 0;
    }


    public void setId(int id){ this.id = id; }

    void update(){
        db.updateData(id, name, index, status, list, dueDate, repeat);
    }

    void expandTask(){
        int taskHeight = ListsActivity.instance.taskHeight;
        ViewGroup.LayoutParams params = nameView.getLayoutParams();
        System.out.println(params.height);
        params.height =taskHeight*2;
        nameView.setLayoutParams(params);
        params = left.getLayoutParams();
        params.height = taskHeight*2;
       left.setLayoutParams(params);
        params = right.getLayoutParams();
        params.height = taskHeight*2;
        right.setLayoutParams(params);
    }

    void contractTask(){
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
}
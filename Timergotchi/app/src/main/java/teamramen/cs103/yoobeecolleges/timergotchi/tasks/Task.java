package teamramen.cs103.yoobeecolleges.timergotchi.tasks;

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

    public boolean Sun, Mon, Tue, Wed, Thu, Fri, Sat;


    //TODO

    public long dueDate;
/*
    public Task(Task t){
        this.id = t.id;
        this.name = t.name;
        this.index = t.index;
        this.status = t.status;
        this.list = t.list;

        this.nameView = t.nameView;
        this.container = t.container;
        this.dobutton = t.dobutton;
        this.donebutton = t.donebutton;
        this.doneshadow = t.doneshadow;
    }*/

    public Task(String name,int index, int list,DatabaseHelper db){
        this.name = name;
        this.index = index;
        this.status = Task.CURRENT;
        this.list = list;

        Sun=Mon=Tue=Wed=Thu=Fri=Sat=false;
        dueDate = -1;

        id = db.addTask(name,index,list);
    }


    public Task(int id, String name,int index, int status, int list){
        this.id = id;
        this.name = name;
        this.index = index;
        this.status = status;
        this.list = list;


        Sun=Mon=Tue=Wed=Thu=Fri=Sat=false;
        dueDate = -1;


    }

    public void setName(String n, DatabaseHelper db){
        name = n;
        db. updateData(id, name, index, status,list);
    }

    public void moveTo(int i, DatabaseHelper db){
        index = i;
        db. updateData(id, name, i, CURRENT,list);
    }

    public void done(DatabaseHelper db){
        showFinished();
        status=COMPLETED;
        if(list == 0) {
            db.updateData(id,name,index,COMPLETED,0);
        }
        else {
            db.updateData(id, name, index, COMPLETED, 4);
        }
    }

    void showFinished(){
        try {
            dobutton.setVisibility(View.INVISIBLE);
            donebutton.setVisibility(View.INVISIBLE);
            doneshadow.setVisibility(View.VISIBLE);
        }catch (Exception e){

        }
    }

    public void delete(DatabaseHelper db){
        db. remove(id);;
    }


    //repeat task
    public void setSun(){Sun = !Sun;}
    public void setMon(){Mon = !Mon;}
    public void setTue(){Tue = !Tue;}
    public void setWed(){Wed = !Wed;}
    public void setThu(){Thu = !Thu;}
    public void setFri(){Fri = !Fri;}
    public void setSat(){Sat = !Sat;}

    //for databasehelper use
    public void setName(String n){
        nameView.setText(n);
        name = n;
    }
    public void setId(int id){ this.id = id; }
    public void setIndex(int index){ this.index = index; }
    public void setStatus(int status){ this.status = status; }
}
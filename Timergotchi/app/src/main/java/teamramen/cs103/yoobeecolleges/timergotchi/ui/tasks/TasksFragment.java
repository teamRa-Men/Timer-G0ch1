package teamramen.cs103.yoobeecolleges.timergotchi.ui.tasks;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;

public class TasksFragment extends Fragment {

    float taskMove = 0;
    int taskCount = 0;
    ArrayList<Task> tasks = new ArrayList<>();
    //ArrayList<TextView> taskNames = new ArrayList<>();
    EditText newTaskName;
    static float taskHeight = 154;

    public static TasksFragment instance;

    DatabaseHelper db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        initTasks(root);



        instance = this;


        return root;
    }
    public void initTasks(View root){

        tasks.add(new Task(root.findViewById( R.id.task0),(TextView) (root.findViewById(R.id.taskname0))));
        tasks.add(new Task(root.findViewById( R.id.task1),(TextView) (root.findViewById(R.id.taskname1))));
        tasks.add(new Task(root.findViewById( R.id.task2),(TextView) (root.findViewById(R.id.taskname2))));
        tasks.add(new Task(root.findViewById( R.id.task3),(TextView) (root.findViewById(R.id.taskname3))));
        tasks.add(new Task(root.findViewById( R.id.task4),(TextView) (root.findViewById(R.id.taskname4))));
        tasks.add(new Task(root.findViewById( R.id.task5),(TextView) (root.findViewById(R.id.taskname5))));
        newTaskName = root.findViewById( R.id.taskname);
        db = new DatabaseHelper(getContext());
        fetchTasks();
    }

    public void fetchTasks() {
        taskCount = 0;
        ArrayList<String> names = db.fetchCurrentNames();
        ArrayList<Integer> indices = db.fetchCurrentIndices();
        ArrayList<Integer> id = db.fetchCurrentId();

        for(int i = 0; i < Math.min(tasks.size(),names.size()); i++){
            fetchTask(id.get(i), names.get(i), indices.get(i));
        }
    }

    public void fetchTask(int id, String taskName, int index) {
        Task t = tasks.get(index);


        try {
            t.container.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            System.out.println(e);
        }

        t.setName(taskName);
        t.setId(id);

        ObjectAnimator animation = ObjectAnimator.ofFloat(t.container, "translationY", (index+1)*taskHeight);
        animation.setDuration(200);
        animation.start();
        taskCount++;
    }

    public void onAddNewTask(View view) {

        if (taskCount < tasks.size()) {
            if(!(newTaskName.getText().toString().isEmpty())) {
                Task t = tasks.get(taskCount);

                try {
                    t.container.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    System.out.println(e);
                }




                String s = newTaskName.getText().toString();
                t.add(s,taskCount, db);
                ObjectAnimator animation = ObjectAnimator.ofFloat(t.container, "translationY", (taskCount+1)*taskHeight);
                animation.setDuration(200);
                animation.start();

                //database add
                //String returnValue = db.onInsert(newTaskName,taskCount,0);



                taskCount++;

                newTaskName.setText("");
                getView().findViewById(R.id.todolist).requestFocus();


            }
            else{
                newTaskName.requestFocus();
            }
        }

    }
    public void onTaskDone(View view) {
        View v = (View)view.getParent();
        System.out.println(v.getId());
        int doneIndex = 0;
        for(int i = 0; i < tasks.size();i++){
            if(v == tasks.get(i).container){
                System.out.println("found");
                doneIndex = i;
                break;
            }
        }

        taskMove -= v.getHeight();
        for(int i = doneIndex+1; i < taskCount;i++){
            View w = tasks.get(i).container;
            //db.moveTask(tasks.get(i).name,i-1);
            tasks.get(i).moveTo(i-1,db);
            ObjectAnimator animation = ObjectAnimator.ofFloat(w, "translationY", i*taskHeight);
            animation.setDuration(200);
            animation.start();
        }
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "translationY", 0);
        animation.setDuration(200);
        animation.start();
        v.setVisibility(View.INVISIBLE);

        Task t = tasks.get(doneIndex);
        //database remove
        t.remove(db);

        tasks.remove(doneIndex);

        tasks.add(t);

        taskCount--;
    }

}

class Task{
    public static int CURRENT=0, REMOVED=1;
    public long id;
    public View container;
    public TextView nameView;
    public int status;
    public String name;

    public Task(View container, TextView nameView){
        this.container = container;
        this.nameView = nameView;
        status = -1;
    }

    public void setName(String n){
        nameView.setText(n);
        name = n;
    }
    public void setId(int id){
        this.id = id;
    }

    public void add(String n, int i, DatabaseHelper db){
        //insert
        id = db.addTask(n, i);
        System.out.println(id);
        status = CURRENT;
        setName(n);

    }
    public void moveTo(int i, DatabaseHelper db){
        System.out.println(db. updateData(id, name, i, CURRENT)+"move");
    }
    public void remove(DatabaseHelper db){
        //remove
        System.out.println(db. updateData(id, name, -1, REMOVED)+"remove");
        status = -1;
        setName("");
    }
}
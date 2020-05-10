package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;

public class TasksFragment extends Fragment {

    boolean fetched;
    protected DatabaseHelper db;
    protected View listView;

    protected ArrayList<Task> tasks = new ArrayList<Task>();
    protected EditText addTask;
    protected View root;

    public int listNum;
    protected TasksAdapter adapter;


    public TasksFragment(int listNum, DatabaseHelper db){
        this.db = db;
        this.listNum = listNum;
    }


    //View editTaskView;
    //EditText editTaskName;
    //Task taskEditing;
    //boolean editing;
    TextView newTaskName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_tasks, container, false);



        RecyclerView list = root.findViewById(R.id.list);
        adapter = new TasksAdapter(tasks, listNum, db);


        ItemTouchHelper.Callback callback = new SwipeDragHelper(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(list);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(root.getContext()));


        if(!fetched) {
            fetchTasks();
            fetched = true;
        }
        newTaskName = root.findViewById(R.id.addTask);
        switch (listNum){
            case 0:setPrompt("Add Daily Task");break;
            case 1:setPrompt("Add Morning Task");break;
            case 2:setPrompt("Add Day Task");break;
            case 3:setPrompt("Add Evening Task");break;

        }





        //editTaskView = root.findViewById(R.id.task_edit_menu);
        //editTaskView.setVisibility(View.INVISIBLE);
        //editTaskName = root.findViewById(R.id.task_edit_name);
        //editing = false;

        return root;
    }

    public void onAddTask(View view) {

        if(newTaskName.getText().toString().isEmpty()) {
            newTaskName.requestFocus();

        }else{

            Task newTask = new Task(newTaskName.getText().toString(),tasks.size(),listNum,db);
            tasks.add(newTask);
            //system.out.println(tasks);
            adapter.notifyItemInserted(tasks.size()-1);

        }
        newTaskName.setText("");
    }



    public void onTaskDone(View view){
        Task t = findByView(view);
        t.done(db);

        if(listNum > 0) {
            int index = tasks.indexOf(t);

            tasks.remove(t);

            adapter.notifyItemRemoved(index);
            for (int i = t.index; i < tasks.size(); i++) {
                tasks.get(i).moveTo(i, db);
            }

            ListsActivity.instance.finishedList.onAddTask(t);
        }
    }


    Task findByView(View view){
        View taskView = (View)view.getParent();


        for(int i = 0; i < tasks.size(); i++) {

            if(taskView == tasks.get(i).container){
                return tasks.get(i);
            }
        }
        return null;
    }

    public void fetchTasks(){
        //system.out.println(listNum+"lsit");
        ArrayList<Task> oldTasks = db.fetchTasks(listNum);

        for(int i = 0; i < oldTasks.size();i++){

            if(tasks.indexOf(oldTasks.get(i))<0) {
                tasks.add(oldTasks.get(i));
                adapter.notifyItemInserted(i);


            }
        }

    }

    public void onClearList(){
        for(int i = 0; i < tasks.size();i++){

            adapter.notifyItemRemoved(0);
        }
        tasks.clear();
        db.clearList(listNum);
    }

    public void setPrompt(String n){
        newTaskName.setHint(n);
    }

}

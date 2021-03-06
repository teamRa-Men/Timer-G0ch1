package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;

public class TasksFragment extends Fragment {

    boolean fetched;
    protected DatabaseHelper db;
    protected View listView;

    protected ArrayList<Task> tasks = new ArrayList<Task>();
    protected EditText addTask;
    protected View root;


    protected TasksAdapter adapter;



    public TasksFragment( DatabaseHelper db){
        this.db = db;

    }

    TextView newTaskName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        root = inflater.inflate(R.layout.fragment_tasks, container, false);




        RecyclerView list = root.findViewById(R.id.list);
        adapter = new TasksAdapter(tasks, db);


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
        newTaskName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    newTaskName.setHint("");
                }
                else{
                    newTaskName.setHint("Add New Task");
                }
            }

        });
        newTaskName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                  @Override
                                                  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { if(actionId == EditorInfo.IME_ACTION_DONE){ onAddTask();

                                                  }
                                                      return false;
                                                  }
                                              }
        );
        return root;
    }



    public void onAddTask() {

        if(newTaskName.getText().toString().isEmpty()) {
            newTaskName.requestFocus();

        }
        else {
            Task newTask = new Task(newTaskName.getText().toString(), tasks.size(), db);

            tasks.add(newTask);
            adapter.notifyItemInserted(tasks.size() - 1);
            newTask.order=tasks.size() - 1;
            newTask.update();
            newTaskName.setText("");
        }
    }



    public void onTaskDone(View view){
        Task t = findByView(view);
        onTaskDone(t);
    }

    public void onTaskDone(Task t){
        t.done();
        if(!t.isRepeating()){
            deleteTask(t);
        }
    }

    public void onDoTask(View view){
        Task t = findByView(view);
        t.doTask();
    }

    public void deleteTask(Task t){
            adapter.notifyItemRemoved(tasks.indexOf(t));
            t.delete();
            tasks.remove(t);

    }



    public Task findByView(View view){
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
        ArrayList<Task> oldTasks = db.fetchTasks();
        Task[] old = new Task[oldTasks.size()];

        for(int i = 0; i < oldTasks.size();i++){
                old[i] = oldTasks.get(i);
        }
        Arrays.sort(old);

        for(int i = 0; i < oldTasks.size();i++){
            old[i].order = i;
            System.out.println(old[i].order);
            System.out.println(old[i].id);
            old[i].update();
            tasks.add(old[i]);
            adapter.notifyItemInserted(i);
        }

    }

}

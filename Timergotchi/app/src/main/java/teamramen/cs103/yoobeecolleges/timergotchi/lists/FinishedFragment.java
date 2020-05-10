package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;

public class FinishedFragment extends Fragment {
    //public static FinishedFragment instance;
    protected DatabaseHelper db;
    protected View listView;

    protected ArrayList<Task> tasks = new ArrayList<Task>();
    protected EditText addTask;
    protected View root;

    public int listNum;
    protected TasksAdapter adapter;


    public FinishedFragment(int listNum, DatabaseHelper db){

        this.listNum = listNum;
        this.db = db;
    }
    boolean fetched;

    //View editTaskView;
    //EditText editTaskName;
    //Task taskEditing;
    //boolean editing;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_finished, container, false);



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


        return root;
    }

    public void onAddTask(Task finished) {
        /*
        //System.out.println("finished " + finished + " tasks " + tasks + " adapter " + adapter); {
            tasks.add(finished);

            adapter.notifyItemInserted(tasks.size() - 1);
        }*/
    }

    public void onDelete(View view){
        Task t = findByView(view);
        int index = tasks.indexOf(t);
        tasks.remove(t);
        t.delete(db);
        adapter.notifyItemRemoved(index);
        for(int i = t.index; i < tasks.size();i++){
            tasks.get(i).moveTo(i,db);
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
        ArrayList<Task> oldTasks = db.fetchTasks(listNum);

        for(int i = 0; i < oldTasks.size();i++){

            if(tasks.indexOf(oldTasks.get(i))<0) {
                tasks.add(oldTasks.get(i));
                adapter.notifyItemInserted(i);
            }
        }
    }
    public void onClearAll(){
        db.clearAll();
    }
    public void onClearList(){
        for(int i = 0; i < tasks.size();i++){
            adapter.notifyItemRemoved(0);
        }
        tasks.clear();
        db.clearList(listNum);
    }
}

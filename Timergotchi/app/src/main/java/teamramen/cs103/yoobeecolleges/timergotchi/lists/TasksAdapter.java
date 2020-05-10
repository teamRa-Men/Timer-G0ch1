package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    ArrayList<Task> tasks;
    int list;
    DatabaseHelper db;

    public TasksAdapter(ArrayList<Task> tasks, int list, DatabaseHelper db){


        this.tasks = tasks;
        this.list = list;
        this.db = db;
    }


    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View taskView = inflater.inflate(R.layout.taskprefab,parent,false);

        ViewHolder holder = new ViewHolder(taskView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);

        //System.out.println(task.name);
        holder.nameView.setText(task.name);
        task.nameView = holder.nameView;
        task.container = holder.container;
        task.dobutton = holder.dobutton;
        task.donebutton =holder.donebutton;
        task.doneshadow =holder.doneshadow;
        if(task.status == Task.COMPLETED){
            showFinished(task);
        }else{
            showCurrent(task);
        }
    }


    void showFinished(Task t){
        try {
            t.dobutton.setVisibility(View.INVISIBLE);
            t.donebutton.setVisibility(View.INVISIBLE);
            t.doneshadow.setVisibility(View.VISIBLE);
        }catch (Exception e){

        }
    }
    void showCurrent(Task t){
        try {
            t.dobutton.setVisibility(View.VISIBLE);
            t.donebutton.setVisibility(View.VISIBLE);
            t.doneshadow.setVisibility(View.INVISIBLE);
        }catch (Exception e){

        }
    }
    @Override
    public int getItemCount() {
        return tasks.size();
    }


    //Swipe Drag methods

    public void onViewMoved(int oldPos, int newPos){

        try {
            Task task = tasks.get(oldPos);
            tasks.remove(oldPos);
            task.moveTo(newPos, db);
            tasks.add(newPos,task);
            notifyItemMoved(oldPos, newPos);
        }
        catch (Exception e){}
    }

    public void onViewSwiped(int pos){
        tasks.get(pos).delete(db);
        tasks.remove(pos);

        notifyItemRemoved(pos);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameView;
        View container,dobutton,donebutton,doneshadow;
        public ViewHolder(View itemView){
            super(itemView);
            nameView = itemView.findViewById(R.id.taskname);
            container = itemView.findViewById(R.id.task);
            dobutton=itemView.findViewById(R.id.dobutton);
            donebutton=itemView.findViewById(R.id.donebutton);
            doneshadow=itemView.findViewById(R.id.doneshadow);

        }

    }



}




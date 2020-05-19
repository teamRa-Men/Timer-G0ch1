package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    ArrayList<Task> tasks;

    DatabaseHelper db;

    public TasksAdapter(ArrayList<Task> tasks, DatabaseHelper db){


        this.tasks = tasks;
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
        task.left =holder.left;
        task.right =holder.right;

        task.finishedbutton =holder.finishedbutton ;
        task.showrepeat = holder.showrepeat;



        task.showTask();
        task.showFinished();
        task.showOverdue();


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
            tasks.add(newPos,task);
            notifyItemMoved(oldPos, newPos);
        }
        catch (Exception e){}
    }

    public void onViewSwiped(int pos){
        tasks.get(pos).delete();
        tasks.remove(pos);

        notifyItemRemoved(pos);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameView;
        View container, doneshadow,left,right;
        ImageView dobutton,donebutton,finishedbutton,showrepeat;

        public ViewHolder(View itemView){
            super(itemView);
            nameView = itemView.findViewById(R.id.taskname);
            container = itemView.findViewById(R.id.task);
            dobutton=itemView.findViewById(R.id.dobutton);
            donebutton=itemView.findViewById(R.id.donebutton);
            doneshadow=itemView.findViewById(R.id.doneshadow);
            left = itemView.findViewById(R.id.left);
            right = itemView.findViewById(R.id.right);

            finishedbutton = itemView.findViewById(R.id.finished);
            showrepeat = itemView.findViewById(R.id.showrepeating);

        }
    }
}




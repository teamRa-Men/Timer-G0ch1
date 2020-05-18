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
        task.left =holder.left;
        task.right =holder.right;
        task.pausebutton=holder.pausebutton;
        task.finishedbutton =holder.finishedbutton ;
        task.showDueDate = holder.showDueDate;
        task.showRepeat = holder.showRepeat;

        task.showTask();
        task.showFinished();
        task.showOverdue();

        task.pausebutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorAccent));
        task.donebutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorAccent));
        task.dobutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorAccent));
        task.finishedbutton.setBackgroundTintList(ListsActivity.instance.getResources().getColorStateList(R.color.colorAccent));

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
            task.moveTo(newPos);
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
        TextView nameView,showDueDate,showRepeat;
        View container, doneshadow,left,right;
        ImageView dobutton,donebutton,pausebutton,finishedbutton;

        public ViewHolder(View itemView){
            super(itemView);
            nameView = itemView.findViewById(R.id.taskname);
            container = itemView.findViewById(R.id.task);
            dobutton=itemView.findViewById(R.id.dobutton);
            donebutton=itemView.findViewById(R.id.donebutton);
            doneshadow=itemView.findViewById(R.id.doneshadow);
            left = itemView.findViewById(R.id.left);
            right = itemView.findViewById(R.id.right);
            pausebutton = itemView.findViewById(R.id.pausebutton);
            finishedbutton = itemView.findViewById(R.id.finished);
            showDueDate = itemView.findViewById(R.id.showDueDate);
            showRepeat = itemView.findViewById(R.id.showRepeat);
        }
    }
}




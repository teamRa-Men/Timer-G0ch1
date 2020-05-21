package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import android.content.Context;
import android.media.Image;
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

        task.left =holder.left;
        task.right =holder.right;

        task.finishedbutton =holder.finishedbutton ;
        task.showrepeat = holder.showrepeat;
        task.showduedate = holder.showduedate;
        task.labelViews = holder.labelViews;



        task.showFinished();
        task.showOverdue();
        task.showTask();


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

            for(int i = 0; i < tasks.size();i++){
                tasks.get(i).order = i;
                tasks.get(i).update();

            }
            ListsActivity.instance.todoList.tasks = tasks;

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
        TextView nameView,showduedate;
        View container, doneshadow,left,right;
        ImageView dobutton,donebutton,finishedbutton,showrepeat;
        ImageView[] labelViews;

        public ViewHolder(View itemView){
            super(itemView);
            nameView = itemView.findViewById(R.id.taskname);
            container = itemView.findViewById(R.id.task);
            dobutton=itemView.findViewById(R.id.dobutton);
            donebutton=itemView.findViewById(R.id.donebutton);

            left = itemView.findViewById(R.id.left);
            right = itemView.findViewById(R.id.right);

            finishedbutton = itemView.findViewById(R.id.finished);
            showrepeat = itemView.findViewById(R.id.showrepeating);
            showduedate = itemView.findViewById(R.id.showduedate);
            labelViews = new ImageView[7];

            labelViews[0] = itemView.findViewById(R.id.imageView6);
            labelViews[1] = itemView.findViewById(R.id.imageView7);
            labelViews[2] = itemView.findViewById(R.id.imageView8);
            labelViews[3] = itemView.findViewById(R.id.imageView9);
            labelViews[4] = itemView.findViewById(R.id.imageView10);
            labelViews[5] = itemView.findViewById(R.id.imageView11);
            labelViews[6] = itemView.findViewById(R.id.imageView12);



        }
    }
}




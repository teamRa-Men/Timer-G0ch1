package teamramen.cs103.yoobeecolleges.timergotchi.pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.Petitem;
import teamramen.cs103.yoobeecolleges.timergotchi.R;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.SwipeDragHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.TasksAdapter;
import teamramen.cs103.yoobeecolleges.timergotchi.record.RecordActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.shop.ShopActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.ListsActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.timer.TimerActivity;

public class PetActivity extends AppCompatActivity implements View.OnDragListener{

/* TODO
buy item price, (if "price" >= "your money"), add item (x1) to backpack and "your money" - "price" 'maybe add sounds'?,
can't find amounth of money/points

* petdesigns
* * adding hunger over Time and stored hunger*/

/* DONE
* * adding stats to the food (health and affection) done, consumables works pretty fine */


    public ImageView Pet_def, Backpack;
    TextView display, healthbar;
    public RecyclerView inventory;
    ArrayList<Petitem> petitems = new ArrayList<Petitem>();

    boolean Inv = false;
    boolean AnimationPlaying = false;

    int affection = 50; int health = 100;
    int screenwidth = 0;
    int itemDragged;
    BackpackAdapter adapter;
    DatabaseHelper db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        Pet_def = findViewById(R.id.Pet_default);
        display = findViewById(R.id.display);
        healthbar = findViewById(R.id.healthbar);

        Backpack = findViewById(R.id.Backpack);
        inventory = findViewById(R.id.inventory);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenwidth = displayMetrics.widthPixels;

       db = new DatabaseHelper(this);

        System.out.println(petitems.size());

        adapter = new BackpackAdapter();
        LinearLayoutManager horizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        inventory.setLayoutManager(horizontal);
        inventory.setAdapter(adapter);
        inventory.setVisibility(View.INVISIBLE);
        petitems = db.fetchBackpack();
        System.out.println(petitems.size());
        adapter.notifyDataSetChanged();

        for(int i = 0; i < petitems.size();i++){

        }

        //making objects dragable
        //Mushroom.setOnTouchListener(this);
        //Mushroom.setTag("Mushroom");

        Pet_def.setOnDragListener(this);

        //adapter adapter = new adapter();
        //Foodinventory.setAdapter(adapter);
    }




    @Override
    public boolean onDrag(View view, DragEvent event) {
        int action = event.getAction();

        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:

                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true; }

                return false;

            case DragEvent.ACTION_DRAG_ENTERED:

                view.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                view.invalidate();
                return true;


                //============================//
            case DragEvent.ACTION_DROP:
                if(AnimationPlaying == false) {
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData = item.getText().toString();

                    view.invalidate();
                    View v = (View) event.getLocalState();
                    ViewGroup itemContainer = (ViewGroup) v.getParent().getParent();

                    itemContainer.removeView(v);//remove the dragged view
                    if (petitems.get(itemDragged).type == 0) {
                        adapter.notifyItemRemoved(itemDragged);
                        db.removePetitem(petitems.get(itemDragged).id);
                        petitems.remove(itemDragged);

                        eating(v);
                        affection += petitems.get(itemDragged).affection;
                        health += petitems.get(itemDragged).health;
                    }
                    v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                view.invalidate();

                if (event.getResult())
                    Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();

                return true;

            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }


    public void Petting(View view) {
        if(AnimationPlaying == false) {

            //if() { if petting is < 3 in less then 10 minutes affection does'nt rise
            affection++;
            //}

            Pet_def.setImageResource(R.drawable.pet_happy);

            AnimationPlaying = true;
            new CountDownTimer(2000, 250) {
                boolean Happy = true;

                public void onTick(long millisUntilFinished) {
                    if (Happy) {
                        Pet_def.setImageResource(R.drawable.pet_happy);

                        Happy = false;
                    } else {
                        Pet_def.setImageResource(R.drawable.pet_happy2);
                        Happy = true;
                    }
                }

                public void onFinish() {
                    Pet_def.setImageResource(R.drawable.pet_default);
                    AnimationPlaying = false;
                }

            }.start();
        }
        display.setText(Integer.toString(affection));
    }

    //===========================================================//


    //===========================BACKPACK========================//
    public void Openinv(View view) {

        if(Inv == false){
            ObjectAnimator animation = ObjectAnimator.ofFloat(Backpack, "translationX", (screenwidth/2)-100);
            animation.setDuration(200);
            animation.start();

            Backpack.setImageResource(R.drawable.backpack_open);
            inventory.setVisibility(View.VISIBLE);
            Inv = true;
        }

        else{
            ObjectAnimator animation = ObjectAnimator.ofFloat(Backpack, "translationX", 0);
            animation.setDuration(200);
            animation.start();

            Backpack.setImageResource(R.drawable.backpack_closed);
            inventory.setVisibility(View.INVISIBLE);
            Inv = false;}
    }
    //===========================================================//

    //===========temporary eating test facility==================//
    public void eating(View view){

        if(AnimationPlaying == false){

        AnimationPlaying = true;
        new CountDownTimer(1000, 250) {

            boolean Happy = true;

            public void onTick(long millisUntilFinished) {
                if (Happy) {
                    Pet_def.setImageResource(R.drawable.pet_eat);
                    Happy = false;
                } else {
                    Pet_def.setImageResource(R.drawable.pet_eat2);
                    Happy = true;
                }
            }

            public void onFinish() {
                new CountDownTimer(1000, 250) {
                    boolean Happy = true;
                    public void onTick(long millisUntilFinished) {

                        if (Happy) {
                            Pet_def.setImageResource(R.drawable.pet_eat3);
                            Happy = false;
                        } else {
                            Pet_def.setImageResource(R.drawable.pet_eat4);
                            Happy = true;
                        }
                    }
                    public void onFinish(){
                        Pet_def.setImageResource(R.drawable.pet_default);
                        AnimationPlaying = false; }
                }.start();
                display.setText(Integer.toString(affection));
                healthbar.setText(Integer.toString(health));
            }

        }.start();

    }
    }
    //==============end of temporary test facility===============//


    public class BackpackAdapter extends RecyclerView.Adapter<BackpackAdapter.ItemViewHolder>{



        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_items,parent,false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

            Petitem petitem = petitems.get(position);
            System.out.println(petitem+"item");
            System.out.println(petitem.name);
            holder.nameHolder.setText(petitem.name);
            holder.imageHolder.setImageResource(petitem.image);

            holder.imageHolder.setTag(petitem.name + ""+ position);
            holder.imageHolder.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    itemDragged = holder.getAdapterPosition();
                    ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data//data to be dragged
                            , shadowBuilder //drag shadow
                            , view//local data about the drag and drop operation
                            , 0//no needed flags
                    );

                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return petitems.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder{
            ImageView imageHolder;
            TextView nameHolder;
            public ItemViewHolder(View view) {
                super(view);
                imageHolder = view.findViewById(R.id.Picture);
                nameHolder = view.findViewById(R.id.Name);
            }
        }
    }







    public void toList(View view) {
        Intent i = new Intent(this, ListsActivity.class);
        startActivity(i);
    }

    public void toTimer(View view) {
        Intent i = new Intent(this, TimerActivity.class);
        startActivity(i);
    }
/*
    public void toPet(View view) {
        Intent i = new Intent(this, PetActivity.class);
        startActivity(i);
    }*/

    public void toShop(View view) {
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);
    }

    public void toRecord(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
    }

}

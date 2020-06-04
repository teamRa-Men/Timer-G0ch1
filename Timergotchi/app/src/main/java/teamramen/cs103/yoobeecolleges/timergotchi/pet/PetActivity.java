package teamramen.cs103.yoobeecolleges.timergotchi.pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.Petitem;
import teamramen.cs103.yoobeecolleges.timergotchi.R;
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
     * * adding stats to the food (happiness and affection) done, consumables works pretty fine */


    public ImageView  petExpression, petModFront, petModBack, petWear, Backpack;
    int petHeight;
    public View petBase;
    ImageView display, hungerbar, happybar;
    public RecyclerView inventory;
    ArrayList<Petitem> petitems = new ArrayList<Petitem>();
    TextView showHunger, showHappy;

    boolean Inv = false;
    boolean AnimationPlaying = false;

    float affection = 50, hunger = 100;
    int screenwidth = 0;
    int itemDragged;
    BackpackAdapter adapter;
    public TextView pointsView;
    public int points;
    int maxhungerbarWidth;
    DatabaseHelper db;
    EditText petName;
    int defaultSpriteId;
    int[] mods;
    boolean waiting = false;
    public static PetActivity instance;

    float last;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_pet);
        petBase = findViewById(R.id.pet_base);
        petExpression = findViewById(R.id.pet_expression);
        petModFront = findViewById(R.id.pet_modify_front);
        petModBack = findViewById(R.id.pet_modify_back);
        petWear = findViewById(R.id.pet_wear);
        //display = findViewById(R.id.display);
        hungerbar = findViewById(R.id.hungerbar);
        happybar = findViewById(R.id.happybar);
        showHappy = findViewById(R.id.showhappy);
        showHunger = findViewById(R.id.showhunger);

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

        petExpression.setOnDragListener(this);

        //adapter adapter = new adapter();
        //Foodinventory.setAdapter(adapter);

        pointsView = findViewById(R.id.pointsview3);
        db = new DatabaseHelper(this);
        points = db.getPoints();
        pointsView.setText(points+" ");
        mods = db.getPetMod();
        if(mods[0]!=0) {
            petModBack.setBackgroundResource(mods[0]);

        }
        if(mods[1]!=0) {
            petModFront.setBackgroundResource(mods[1]);

        }

        if(mods[2]!=0) {
            petWear.setBackgroundResource(mods[2]);

        }



        maxhungerbarWidth = (int)(screenwidth*0.49f);

        petName = findViewById(R.id.petname);
        petName.setText(db.getPetName());


        affection=db.getAffection();
        hunger = db.getHealth();


        update();
        ViewGroup.LayoutParams params = petBase.getLayoutParams();
        petHeight = params.height;
        idle();

        instance = this;

    }

    void idle(){
        new CountDownTimer(30001, 100) {
            int time = 0;
            int height = petHeight;

            public void onTick(long millisUntilFinished) {
                //System.out.println("tick");
                time+=100;
                if(!AnimationPlaying){
                    height = (int)(petHeight+3*(1+Math.sin(4*Math.PI*time/3000+Math.PI)));
                    ViewGroup.LayoutParams params = petBase.getLayoutParams();

                    params.height = height;
                    petBase.setLayoutParams(params);
                }
            }

            public void onFinish() {
                idle();
            }

        }.start();
    }
    void update(){

        float lastUpdateTime = db.getLastUpdate();

        //hp 100-> 0 after 1 day for timespeed = 1
        float hungerRate = 2;

        if(lastUpdateTime>0) {

            hunger -= (System.currentTimeMillis() - lastUpdateTime) / 8.64e7  * 100 * hungerRate;
            hunger = Math.max(hunger,0);

        }
        else {
            System.out.println("wtf");
            hunger = 10;
        }

        db.setHealth(hunger);

        float happyRate = (100-hunger)/10;


        if(lastUpdateTime>0) {

            affection -= (System.currentTimeMillis() - lastUpdateTime) / 8.64e7  * 100 * happyRate;
            affection = Math.max(affection,0);

        }
        else {
            System.out.println("wtf");
            affection = 10;
        }
        db.setAffection((int)affection);

        showHappy();
        showhunger();


        System.out.println(happyRate + " happy rate " + affection + " affection " + hunger + " hunger " + System.currentTimeMillis());

        db.setLastUpdate(System.currentTimeMillis());

        new CountDownTimer(2000, 100) {


            public void onTick(long millisUntilFinished) {
                if(!AnimationPlaying && waiting) {

                    petExpression.setBackgroundResource(R.drawable.pet_yawn);
                    AnimationPlaying = true;
                }

            }

            public void onFinish() {
                waiting = true;
                AnimationPlaying = false;
                petExpression.setBackgroundResource(defaultSpriteId);
                System.out.println((System.currentTimeMillis()-last) + "sdfdsf");
                last = System.currentTimeMillis();
                new CountDownTimer(3*60*1000, 100) {


                    public void onTick(long millisUntilFinished) {
                        //System.out.println("tick");
                    }

                    public void onFinish() {
                        update();
                    }

                }.start();
            }

        }.start();



    }

    void showhunger(){
        showHunger.setText((int)(100-hunger)+"");
        ViewGroup.LayoutParams params = hungerbar.getLayoutParams();
        params.width = (int)(maxhungerbarWidth*(float)(hunger/100f));
        hungerbar.setLayoutParams(params);
        if(hunger < 15) {
            hungerbar.setBackgroundColor(getResources().getColor(R.color.colorLabel0));
            //show starving sprite
        }
        else if(hunger < 40){
            hungerbar.setBackgroundColor(getResources().getColor(R.color.colorLabel1));
        }
        else if(hunger < 50){
            hungerbar.setBackgroundColor(getResources().getColor(R.color.colorLabel2));
        }
        else{
            hungerbar.setBackgroundColor(getResources().getColor(R.color.colorLabel4));
        }
        //System.out.println(hunger + " hp "+ params.width + " " + lastUpdateTime);


        //play hungry animation? show thinking of food
    }

    void showHappy(){
        showHappy.setText((int)affection+"");
        ViewGroup.LayoutParams params = happybar.getLayoutParams();
        params.width = (int)(maxhungerbarWidth*(float)affection/100f);
        happybar.setLayoutParams(params);
        if(affection < 25) {
            defaultSpriteId = R.drawable.pet_sad;//sad sprite
        }
        else if(affection < 50){
            defaultSpriteId = R.drawable.pet_meh;//meh sprite
        }
        else{
            defaultSpriteId = R.drawable.pet_default;//happy sprite
        }
        //System.out.println(hunger + " hp "+ params.width + " " + lastUpdateTime);
        petExpression.setBackgroundResource(defaultSpriteId);
    }


    @Override
    public boolean onDrag(View view, DragEvent event) {

        int action = event.getAction();

        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                if(petitems.get(itemDragged).type==1){
                    System.out.println("remove");
                    petModBack.setBackgroundResource(R.color.colorEmpty);
                    mods[0] = 0;
                    db.setPetMod(mods);
                }
                else if(petitems.get(itemDragged).type==2){
                    System.out.println("remove");
                    petModFront.setBackgroundResource(R.color.colorEmpty);
                    mods[1] = 0;
                    db.setPetMod(mods);

                }
                else if(petitems.get(itemDragged).type==3){
                    System.out.println("remove");
                    petWear.setBackgroundResource(R.color.colorEmpty);
                    mods[2] = 0;
                    db.setPetMod(mods);

                }
                waiting = false;


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


                        eating(v);
                        affection += petitems.get(itemDragged).affection;
                        hunger += petitems.get(itemDragged).health;
                        affection=Math.min(affection,100);
                        hunger=Math.min(hunger,100);

                        petitems.remove(itemDragged);


                    }
                    else if (petitems.get(itemDragged).type == 1) {
                        System.out.println("add back mod");
                        mods[0] = petitems.get(itemDragged).image;
                        db.setPetMod(mods);
                        petModBack.setBackgroundResource(petitems.get(itemDragged).image);


                    }
                    else if (petitems.get(itemDragged).type == 2) {
                        System.out.println("add front");
                        mods[1] = petitems.get(itemDragged).image;
                        db.setPetMod(mods);
                        petModFront.setBackgroundResource(petitems.get(itemDragged).image);
                    }
                    else if (petitems.get(itemDragged).type == 3) {
                        System.out.println("add wearing");
                        mods[2] = petitems.get(itemDragged).image;
                        db.setPetMod(mods);
                        petWear.setBackgroundResource(petitems.get(itemDragged).image);
                    }
                    waiting = false;
                    v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                view.invalidate();

                //animation show change

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


            petExpression.setBackgroundResource(R.drawable.pet_petting);

            AnimationPlaying = true;
            new CountDownTimer(2000, 400) {
                boolean Happy = true;

                public void onTick(long millisUntilFinished) {
                    if (Happy) {
                        petExpression.setBackgroundResource(R.drawable.pet_petting1);

                        Happy = false;
                    } else {
                        petExpression.setBackgroundResource(R.drawable.pet_petting);
                        Happy = true;
                    }
                }

                public void onFinish() {
                    petExpression.setBackgroundResource(defaultSpriteId);
                    AnimationPlaying = false;

                    //affection += hunger/10;
                    affection+=10;
                    affection=Math.min(100,affection);
                    waiting = false;
                    update();


                    db.setAffection((int)affection);
                    waiting = false;


                }

            }.start();
        }
        //display.setText(Integer.toString(affection));
    }

    //===========================================================//


    //===========================BACKPACK========================//
    public void Openinv(View view) {

        if(Inv == false){
            ObjectAnimator animation = ObjectAnimator.ofFloat(Backpack, "translationX", (screenwidth/2)-50);
            animation.setDuration(200);
            animation.start();

            Backpack.setBackgroundResource(R.drawable.backpack_open);
            inventory.setVisibility(View.VISIBLE);
            Inv = true;
        }

        else{
            ObjectAnimator animation = ObjectAnimator.ofFloat(Backpack, "translationX", 0);
            animation.setDuration(200);
            animation.start();

            Backpack.setBackgroundResource(R.drawable.backpack_closed);
            inventory.setVisibility(View.INVISIBLE);
            Inv = false;}
    }
    //===========================================================//

    //===========temporary eating test facility==================//
    public void eating(View view){

        if(AnimationPlaying == false){

            AnimationPlaying = true;
            new CountDownTimer(2000, 400) {

                int frame = 0;

                public void onTick(long millisUntilFinished) {
                    if (frame ==0) {
                        petExpression.setBackgroundResource(R.drawable.pet_eat);
                        frame++;
                    } else if(frame==1) {
                        petExpression.setBackgroundResource(R.drawable.pet_eat2);
                        frame++;

                    } else if(frame==2) {
                        petExpression.setBackgroundResource(R.drawable.pet_eat3);
                        frame++;

                    } else if(frame==3) {
                        petExpression.setBackgroundResource(R.drawable.pet_eat4);
                        frame++;
                    }
                }

                public void onFinish() {


                    petExpression.setBackgroundResource(defaultSpriteId);
                    AnimationPlaying = false;
                    db.setHealth(hunger);
                    waiting = false;
                    update();
                }

            }.start();

        }
    }





    public class BackpackAdapter extends RecyclerView.Adapter<BackpackAdapter.ItemViewHolder>{



        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_items,parent,false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

            Petitem petitem = petitems.get(position);
            System.out.println(petitem+"item");
            //System.out.println(petitem.name);
            //holder.nameHolder.setText(petitem.name);
            holder.imageHolder.setBackgroundResource(petitem.image);

            holder.imageHolder.setTag(petitem.name + ""+ position);
            holder.item = petitem;
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

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            ImageView imageHolder;
            public Petitem item;

            //TextView nameHolder;
            public ItemViewHolder(View view) {
                super(view);
                imageHolder = view.findViewById(R.id.Picture);
                this.item = item;
                //nameHolder = view.findViewById(R.id.Name);
            }


        }
    }







    public void toList(View view) {
        Intent i = new Intent(this, ListsActivity.class);
        db.setPetName(petName.getText().toString());
        startActivity(i);
    }

    public void toTimer(View view) {
        Intent i = new Intent(this, TimerActivity.class);
        db.setPetName(petName.getText().toString());
        startActivity(i);
    }
/*
    public void toPet(View view) {
        Intent i = new Intent(this, PetActivity.class);
        startActivity(i);
    }*/

    public void toShop(View view) {
        Intent i = new Intent(this, ShopActivity.class);
        db.setPetName(petName.getText().toString());
        startActivity(i);
    }

    public void toRecord(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        db.setPetName(petName.getText().toString());
        startActivity(i);
    }

}

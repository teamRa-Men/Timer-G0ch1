package teamramen.cs103.yoobeecolleges.timergotchi.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import teamramen.cs103.yoobeecolleges.timergotchi.R;
import teamramen.cs103.yoobeecolleges.timergotchi.record.RecordActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.shop.ShopActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.ListsActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.timer.TimerActivity;

public class PetActivity extends AppCompatActivity {


    ImageView Pet_def, Backpack, Mushroom;
    TextView display;
    LinearLayout Foodinventory;

    boolean Inv = false;
    boolean AnimationPlaying = false;
    int affection = 0; int health = 100;
    int screenwidth = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        Pet_def = findViewById(R.id.Pet_default);
        display = findViewById(R.id.display);
        Backpack = findViewById(R.id.Backpack);
        Foodinventory = findViewById(R.id.Foodinventory);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenwidth = displayMetrics.widthPixels;
        //foods
        Mushroom = findViewById(R.id.Mushroom);
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
            ObjectAnimator animation = ObjectAnimator.ofFloat(Backpack, "translationX", screenwidth/2);
            animation.setDuration(200);
            animation.start();

            Backpack.setImageResource(R.drawable.backpack_open);
            Foodinventory.setVisibility(View.VISIBLE);
            Inv = true;
        }

        else{
            ObjectAnimator animation = ObjectAnimator.ofFloat(Backpack, "translationX", 0);
            animation.setDuration(200);
            animation.start();

            Backpack.setImageResource(R.drawable.backpack_closed);
            Foodinventory.setVisibility(View.GONE);
            Inv = false;}
    }
    //===========================================================//






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

package com.example.pet_try;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView Pet_def, Backpack, Mushroom;
    TextView display;
    LinearLayout Foodinventory;

    boolean Inv = false;
    boolean AnimationPlaying = false;
    int affection = 0; int health = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pet_def = findViewById(R.id.Pet_default);
        display = findViewById(R.id.display);
        Backpack = findViewById(R.id.Backpack);
        Foodinventory = findViewById(R.id.Foodinventory);

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
            ObjectAnimator animation = ObjectAnimator.ofFloat(Backpack, "translationX", 400);
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


}

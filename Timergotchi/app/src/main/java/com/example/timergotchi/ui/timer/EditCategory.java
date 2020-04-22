package com.example.timergotchi.ui.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.timergotchi.MainActivity;
import com.example.bcs101mobileapp.R;

public class EditCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category
        );

    }

    public void onDone(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

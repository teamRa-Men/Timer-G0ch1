package com.example.timergotchi.ui.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.timergotchi.MainActivity;
import com.example.bcs101mobileapp.R;

public class EditTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

    }
    public void onDelete(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onDone(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

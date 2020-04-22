package com.example.timergotchi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bcs101mobileapp.R;
import com.example.timergotchi.ui.timer.EditCategory;
import com.example.timergotchi.ui.timer.EditTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.todo, R.id.timer, R.id.rewards, R.id.pet)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void onAddTask(View view) {
        Intent intent = new Intent(this, EditTask.class);
        startActivity(intent);
    }

    public void onDo(View view) {
        navController.navigate(R.id.timer);
    }

    public void onEditTask(View view) {
        Intent intent = new Intent(this, EditTask.class);
        startActivity(intent);
    }

    public void onEditCategories(View view) {
        Intent intent = new Intent(this, EditCategory.class);
        startActivity(intent);
    }

    public void onDone(View view) {
    }
}

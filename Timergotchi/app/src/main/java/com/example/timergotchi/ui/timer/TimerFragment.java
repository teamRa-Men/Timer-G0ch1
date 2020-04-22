package com.example.timergotchi.ui.timer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bcs101mobileapp.R;

public class TimerFragment extends Fragment {

    private TimerViewModel timerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        timerViewModel =
                ViewModelProviders.of(this).get(TimerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_timer, container, false);

        return root;
    }
}
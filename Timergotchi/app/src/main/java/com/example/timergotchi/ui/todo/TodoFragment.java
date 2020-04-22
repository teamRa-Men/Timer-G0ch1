package com.example.timergotchi.ui.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bcs101mobileapp.R;

public class TodoFragment extends Fragment {

    private TodoViewModel todoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todoViewModel =
                ViewModelProviders.of(this).get(TodoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        return root;
    }
}
package com.example.timergotchi.ui.pet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bcs101mobileapp.R;

public class Pet extends Fragment {
    private PetViewModel petViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        petViewModel =
                ViewModelProviders.of(this).get(PetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pet, container, false);


        return root;
    }
}

package com.example.timergotchi.ui.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bcs101mobileapp.R;

public class RewardsFragment extends Fragment {

    private RewardsViewModel rewardsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rewardsViewModel =
                ViewModelProviders.of(this).get(RewardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);


        return root;
    }
}
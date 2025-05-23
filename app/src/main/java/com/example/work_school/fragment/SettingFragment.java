package com.example.work_school.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.work_school.R;
import com.example.work_school.databinding.FragmentHomeBinding;
import com.example.work_school.databinding.FragmentSettingBinding;


public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;



    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
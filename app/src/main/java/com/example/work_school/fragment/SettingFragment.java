package com.example.work_school.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.work_school.R;
import com.example.work_school.databinding.FragmentHomeBinding;
import com.example.work_school.databinding.FragmentSettingBinding;
import com.example.work_school.repository.ExpenseRepository;


public class SettingFragment extends PreferenceFragmentCompat {

    private FragmentSettingBinding binding;

    ExpenseRepository repository;


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);




    }

}
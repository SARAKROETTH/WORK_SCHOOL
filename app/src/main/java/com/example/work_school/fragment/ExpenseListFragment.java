package com.example.work_school.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.work_school.R;
import com.example.work_school.databinding.FragmentExpenseListBinding;
import com.example.work_school.databinding.FragmentHomeBinding;


public class ExpenseListFragment extends Fragment {

    FragmentExpenseListBinding binding;


    public ExpenseListFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenseListBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}
package com.example.work_school.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.work_school.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        // Get arguments safely
        Bundle args = getArguments();
        if (args != null) {
            String amount = args.getString("Amount", "N/A");
            String remark = args.getString("Remark", "N/A");
            String categories = args.getString("Categories", "N/A");

            binding.showAmount.setText(amount);
            binding.showRemark.setText(remark);
            binding.showCategory.setText(categories);
        } else {
            // Optionally set default values or log a warning
            binding.showAmount.setText("No Amount");
            binding.showRemark.setText("No Remark");
            binding.showCategory.setText("No Category");
        }

        return binding.getRoot();
    }
}

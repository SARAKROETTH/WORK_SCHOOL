package com.example.work_school.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.work_school.R;
import com.example.work_school.adapter.ExpenseAdapter;
import com.example.work_school.databinding.FragmentExpenseListBinding;
import com.example.work_school.databinding.FragmentHomeBinding;
import com.example.work_school.model.Expense;
import com.example.work_school.repository.ExpenseRepository;
import com.example.work_school.repository.IApiCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class ExpenseListFragment extends Fragment {

    FragmentExpenseListBinding binding;

    private ExpenseRepository repository;

    private int currentPage = 1;

    private boolean isLoading = false;

    private static final int PRE_LOAD_ITEMS = 1;

    private FirebaseAuth mAuth;

    private ExpenseAdapter expenseAdapter;

    public ExpenseListFragment() {
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
        currentPage = 1;
        loadTasks(true);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenseListBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.itemContainer.setLayoutManager(layoutManager);
        repository = new ExpenseRepository();
        expenseAdapter = new ExpenseAdapter();
        binding.itemContainer.setAdapter(expenseAdapter);


        mAuth = FirebaseAuth.getInstance();

        binding.itemContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx,int  dy) {
                super.onScrolled(recyclerView, dx,dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + PRE_LOAD_ITEMS)) {
                    loadTasks(false);
                }
            }
        });
        return binding.getRoot();
    }

    private void loadTasks(boolean reset) {
        isLoading = true;
        showProgressBar();
        String currentUserId = "1249588e-aea4-4a9e-930d-0778c8669364";
//        String currentUserId = mAuth.getCurrentUser().getUid();
        repository.getExpenses(currentPage, currentUserId, new IApiCallback<List<Expense>>() {

            @Override
            public void onSuccess(List<Expense> expenses) {
                if(!expenses.isEmpty()){
                    if (reset) {
                        expenseAdapter.setExpenses(expenses);
                    }else {
                        expenseAdapter.addExpenses(expenses);
                    }
                    currentPage++;
                }
                isLoading = false;
                hideProgressBar();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                isLoading = false;
                hideProgressBar();
            }
        });
    }

    private void hideProgressBar() {
        binding.expenseProgressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        binding.expenseProgressBar.setVisibility(View.VISIBLE);
    }

}
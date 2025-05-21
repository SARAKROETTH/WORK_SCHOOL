package com.example.work_school.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.work_school.DetailExpenseActivity;
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
        setSwipeToDelete();
        return binding.getRoot();
    }

    private void setSwipeToDelete() {
        expenseAdapter.setOnTaskDeleteListener(new ExpenseAdapter.OnExpenseDeleteListener() {
            @Override
            public void onExpenseDelete(Expense expense) {
                deleteExpense(expense.getId());
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();


                new androidx.appcompat.app.AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // User confirmed - delete the task
                            expenseAdapter.removeExpense(position);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // User canceled - restore item
                            expenseAdapter.notifyItemChanged(position);
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .show();

            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.RED);

                if (dX < 0) {
                    RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom());
                    c.drawRect(background, paint);

                    // Draw icon
                    Drawable icon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_delete); // Your trash icon
                    int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int iconTop = itemView.getTop() + iconMargin;
                    int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    int iconBottom = iconTop + icon.getIntrinsicHeight();
                    if (icon != null) {
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    }

                    // Draw text
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(40);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    float textX = itemView.getRight() - icon.getIntrinsicWidth() - 200;
                    float textY = itemView.getTop() + (itemView.getHeight() / 2f) + 15;
                    c.drawText("Delete", textX, textY, paint);

                }

            }
        }).attachToRecyclerView(binding.itemContainer);



    }
    private void deleteExpense(String id) {
        showProgressBar();
        repository.deleteExpense(id , new IApiCallback<String>() {

            @Override
            public void onSuccess(String result) {
                hideProgressBar();
            }

            @Override
            public void onError(String errorMessage) {
                hideProgressBar();
                Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTasks(boolean reset) {
        isLoading = true;
        showProgressBar();
        String currentUserId = mAuth.getCurrentUser().getUid();
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
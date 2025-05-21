package com.example.work_school.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.work_school.databinding.ItemExpenseBinding;
import com.example.work_school.model.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses;


    public ExpenseAdapter(){
        this.expenses = new ArrayList<>();
    }


    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemExpenseBinding binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        String formattedDate = formatDate(expense.getCreatedDate());
        holder.binding.showCategory.setText(expense.getCategory());
        holder.binding.showAmount.setText("- "+String.valueOf(expense.getAmount()));
        holder.binding.showCurrency.setText(expense.getCurrency());
        holder.binding.showRemark.setText(expense.getRemark());
        holder.binding.showDate.setText(formattedDate);
    }

    private String formatDate(Date createdDate) {
       SimpleDateFormat desiredFormat = new SimpleDateFormat("MM/yy/dd");
       return desiredFormat.format(createdDate);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expense> newExpenses){
        expenses.clear();
        expenses.addAll(newExpenses);
        notifyDataSetChanged();
    }

    public void addExpenses(List<Expense> newExpenses){
        int startPosition = expenses.size();
        expenses.addAll(newExpenses);
        notifyItemRangeInserted(startPosition,newExpenses.size());
    }

    public int getItemId(Expense expense){
        int position = expenses.indexOf(expense);
        if (position != -1){
            return position;
        }else {
            return -1;
        }
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {

        protected ItemExpenseBinding binding;

        public ExpenseViewHolder(@NonNull ItemExpenseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}

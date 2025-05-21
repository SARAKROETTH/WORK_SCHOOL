package com.example.work_school.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.work_school.DetailExpenseActivity;
import com.example.work_school.databinding.ItemExpenseBinding;
import com.example.work_school.model.Expense;
import com.example.work_school.repository.ExpenseRepository;
import com.example.work_school.repository.IApiCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses;

    private OnExpenseDeleteListener deleteListener;


    private ExpenseRepository expenseRepository;

    public interface OnExpenseDeleteListener {
        void onExpenseDelete(Expense expense);
    }

    public void setOnTaskDeleteListener(OnExpenseDeleteListener listener) {
        this.deleteListener = listener;
    }
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

        holder.binding.itemContainer.setOnClickListener(
                v -> {
                    Intent intent = new Intent(holder.itemView.getContext(), DetailExpenseActivity.class);
                    intent.putExtra("expenseId", expense.getId());
                    intent.putExtra("Amounts", Integer.toString(expense.getAmount()));
                    intent.putExtra("Currencys", expense.getCurrency());
                    intent.putExtra("expenseCategory", expense.getCategory());
                    intent.putExtra("Remark", expense.getRemark());
                    intent.putExtra("CreatedDate", formattedDate);
                    holder.itemView.getContext().startActivity(intent);
                }
        );

    }

    private String formatDate(Date createdDate) {
       SimpleDateFormat desiredFormat = new SimpleDateFormat("MM/yy/dd");
       return desiredFormat.format(createdDate);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void removeExpense(int position){

        if (position >= 0 && position < expenses.size()){
           Expense removedExpense = expenses.remove(position);
            notifyItemRemoved(position);
            if (deleteListener != null) {
                deleteListener.onExpenseDelete(removedExpense);
            }
        }
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

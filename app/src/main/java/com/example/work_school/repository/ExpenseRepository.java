package com.example.work_school.repository;

import com.example.work_school.model.Expense;
import com.example.work_school.service.ExpenseService;
import com.example.work_school.util.RetroClient;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRepository {

    private ExpenseService expenseService;

    private static final int PAGE_SIZE = 6;

    public ExpenseRepository(){
        expenseService = RetroClient.getClient().create(ExpenseService.class);
    }

    public void getExpenses(int page , String createBy , final IApiCallback<List<Expense>> callback){
        Call<List<Expense>> call = expenseService.getExpenses(page,PAGE_SIZE,createBy);

        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void createExpense(Expense expense ,final IApiCallback<Expense> callback){
        expenseService.createExpense(expense).enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    public void deleteExpense(String expenseId , final IApiCallback<String> callback){
        expenseService.deleteExpense(expenseId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Expense deleted successfully");
                } else {
                    callback.onError(getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                callback.onError("Network error: " + throwable.getMessage());
            }
        });


    }

    private String getErrorMessage(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                return "Error: " + response.code() + " - " + response.errorBody().string();
            }
        } catch (IOException e) {
            return "Error: " + response.code() + " (failed to read error body)";
        }
        return "Unknown error: " + response.code();
    }


}

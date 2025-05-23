package com.example.work_school.repository;

import android.content.Context;

import com.example.work_school.Dao.AppDatabase;
import com.example.work_school.Dao.MyroomDao;
import com.example.work_school.model.Categories;
import com.example.work_school.model.Expense;
import com.example.work_school.service.ExpenseService;
import com.example.work_school.util.NetworkUtil;
import com.example.work_school.util.RetroClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRepository {


    private ExpenseService expenseService;
    private Context context;

    MyroomDao myroomDao;


    private static final int PAGE_SIZE = 6;

    public List<Categories> getAllCategoriesLocal() {
        return myroomDao.getAllCategories();
    }

    public void saveCategoriesLocal(Categories categories){
        SaveCategoriesLocal(categories);
    }

    private void SaveCategoriesLocal(Categories categories) {

            Executors.newSingleThreadExecutor().execute(() -> {
                myroomDao.insertCategories(categories);
            });
    }

    public void syncTasks(final IApiCallback<String> callback) {
        // Background thread to avoid UI freeze
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Expense> unsyncedTasks = myroomDao.getUnsyncedExpense();

            if (unsyncedTasks.isEmpty()) {
                callback.onSuccess("All tasks are already synced.");
                return;
            }

            // Atomic counters must be used since network callbacks run on different threads
            AtomicInteger remainingTasks = new AtomicInteger(unsyncedTasks.size());
            AtomicBoolean hasError = new AtomicBoolean(false);

            for (Expense expense : unsyncedTasks) {
                expenseService.createExpense(expense).enqueue(new Callback<Expense>() {
                    @Override
                    public void onResponse(Call<Expense> call, Response<Expense> response) {
                        // Only mark as synced if server confirms success

                        if (response.isSuccessful()) {

                            markTaskAsSynced(expense);

                        } else {
                            hasError.set(true);  // Server responded but with error (e.g. 400)
                        }
                        checkSyncCompletion(callback, remainingTasks, hasError);
                    }

                    @Override
                    public void onFailure(Call<Expense> call, Throwable t) {
                        hasError.set(true);  // Network failure (no connection, timeout)
                        checkSyncCompletion(callback, remainingTasks, hasError);
                    }
                });

                // API server can't accept burst request, therefore sleep for 1 second per request
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

    }

    private void checkSyncCompletion(IApiCallback<String> callback, AtomicInteger remainingTasks, AtomicBoolean hasError) {
        if (remainingTasks.decrementAndGet() == 0) {
            if (hasError.get()) {
                callback.onError("Some tasks failed to sync.");
            } else {
                callback.onSuccess("All tasks synced successfully.");
            }
        }
    }

    private void markTaskAsSynced(Expense expense) {
        Executors.newSingleThreadExecutor().execute(() -> {
            myroomDao.markExpenseAsSynced(expense.getId());// Run on a background thread
        });
    }


    public ExpenseRepository(Context context){
        this.context = context;
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        this.myroomDao = appDatabase.myroomDao();
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
        if(NetworkUtil.isNetworkAvailable(context)){
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
        }else {
            saveExpenseToLocal(expense);
            callback.onError("No internet connection");
        }

    }

    private void saveExpenseToLocal(Expense expense) {
        expense.setIsSynced(false);
        Executors.newSingleThreadExecutor().execute(() -> {
            myroomDao.insertExpense(expense); // Run on a background thread
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

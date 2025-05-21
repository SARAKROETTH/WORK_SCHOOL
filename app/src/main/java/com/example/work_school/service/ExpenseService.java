package com.example.work_school.service;

import com.example.work_school.model.Expense;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ExpenseService {

    @GET("expenses")
    Call<List<Expense>> getExpenses(@Query("_page") int page, @Query("_limit") int limit,@Query("createdBy") String createdBy);

    @GET("expenses/{id}")
    Call<Expense> getExpenseById(@Query("id") String id);

    @POST("expenses")
    Call<Expense> createExpense(@Body Expense expense);

    @DELETE("expenses/{id}")
    Call<Void> deleteExpense(@Path("id") String id);

}

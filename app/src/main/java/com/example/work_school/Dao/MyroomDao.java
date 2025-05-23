package com.example.work_school.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.work_school.model.Categories;
import com.example.work_school.model.Expense;

import java.util.List;


@Dao
public interface MyroomDao {

    @Query("SELECT * FROM expenses WHERE is_synced = 0")
    List<Expense> getUnsyncedExpense();

    @Insert
    void insertExpense(Expense expense);


    @Insert
    void insertCategories(Categories categories);

    @Query("SELECT * FROM categories")
    List<Categories> getAllCategories();

    @Query("SELECT * FROM expenses WHERE id = :id")
    Expense getExpenseById(String id);

    @Query("UPDATE expenses SET is_synced = 1 WHERE id = :id")
    void markExpenseAsSynced(String id);

}

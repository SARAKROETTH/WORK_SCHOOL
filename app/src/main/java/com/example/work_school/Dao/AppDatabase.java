package com.example.work_school.Dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.work_school.model.Categories;
import com.example.work_school.model.Expense;


@Database(entities = {Expense.class, Categories.class }, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract MyroomDao myroomDao();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "my_room_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
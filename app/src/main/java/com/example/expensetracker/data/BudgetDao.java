package com.example.expensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expensetracker.models.Budget;

@Dao
public interface BudgetDao {
    @Insert
    void insert(Budget budget);

    @Delete
    void delete(Budget budget);

    @Update
    void update(Budget budget);

    @Query("SELECT * FROM budget_table")
    LiveData<Budget> getBudget();
}

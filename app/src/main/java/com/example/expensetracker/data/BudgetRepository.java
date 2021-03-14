package com.example.expensetracker.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.expensetracker.models.Budget;
import com.example.expensetracker.utils.ExpensesRoomDatabase;

public class BudgetRepository {

    private LiveData<Budget> currentBudget;
    private BudgetDao budgetDao;

    public BudgetRepository(Application application) {
        ExpensesRoomDatabase roomDatabase = ExpensesRoomDatabase.getInstance(application);
        budgetDao = roomDatabase.budgetDao();
        currentBudget = budgetDao.getBudget();
    }

    public LiveData<Budget> getCurrentBudget() {
        return currentBudget;
    }

    public void insert(Budget budget) {
        ExpensesRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                budgetDao.insert(budget);
            }
        });
    }

    public void delete(Budget budget) {
        ExpensesRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                budgetDao.delete(budget);
            }
        });
    }

    public void update(Budget budget) {
        ExpensesRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                budgetDao.update(budget);
            }
        });
    }
}

package com.example.expensetracker.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.expensetracker.models.Expense;
import com.example.expensetracker.utils.ExpensesRoomDatabase;

import java.util.List;

public class ExpenseRepository {

    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;

    public ExpenseRepository(Application application) {
        ExpensesRoomDatabase expensesRoomDatabase = ExpensesRoomDatabase.getInstance(application);
        expenseDao = expensesRoomDatabase.expenseDao();
        allExpenses = expenseDao.getExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public void insert(Expense expense) {
        ExpensesRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                expenseDao.insert(expense);
            }
        });
    }

    public void update(Expense expense) {
        ExpensesRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                expenseDao.update(expense);
            }
        });
    }

    public void delete(Expense expense) {
        ExpensesRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                expenseDao.delete(expense);
            }
        });
    }

    public void deleteAll() {
        ExpensesRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                expenseDao.deleteAll();
            }
        });
    }
}

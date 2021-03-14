package com.example.expensetracker.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.expensetracker.data.BudgetRepository;
import com.example.expensetracker.data.ExpenseRepository;

import java.util.List;

public class ApplicationViewModel extends AndroidViewModel {

    private static ExpenseRepository expenseRepository;
    private static BudgetRepository budgetRepository;
    private LiveData<List<Expense>> allExpenses;
    private static LiveData<Budget> budgetLiveData;

    public ApplicationViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        budgetRepository = new BudgetRepository(application);
        allExpenses = expenseRepository.getAllExpenses();
        budgetLiveData = budgetRepository.getCurrentBudget();
    }

    // Expenses CRUD
    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public static void insert(Expense expense) {
        expenseRepository.insert(expense);
    }

    public static void update(Expense expense) {
        expenseRepository.update(expense);
    }

    public static void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

    public static void deleteAllExpenses() {
        expenseRepository.deleteAll();
    }

    // Budget CRUD
    public static LiveData<Budget> getBudgetLiveData() {
        return budgetLiveData;
    }

    public static void insert(Budget budget) {
        budgetRepository.insert(budget);
    }

    public static void update(Budget budget) {
        budgetRepository.update(budget);
    }

    public static void delete(Budget budget) {
        budgetRepository.delete(budget);
    }
}

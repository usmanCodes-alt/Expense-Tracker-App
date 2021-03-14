package com.example.expensetracker.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.expensetracker.data.BudgetDao;
import com.example.expensetracker.data.ExpenseDao;
import com.example.expensetracker.models.Budget;
import com.example.expensetracker.models.Expense;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class, Budget.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class ExpensesRoomDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    private static ExpensesRoomDatabase instance;
    private static final String DATABASE_NAME = "expenses_database";
    public static final ExecutorService databaseWriterExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract ExpenseDao expenseDao();
    public abstract BudgetDao budgetDao();

    public static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriterExecutor.execute(() -> {
                        ExpenseDao expenseDao = instance.expenseDao();
                        expenseDao.deleteAll();
                    });
                }
            };

    public static synchronized ExpensesRoomDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application.getApplicationContext(),
                    ExpensesRoomDatabase.class,
                    DATABASE_NAME)
                    .addCallback(sRoomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

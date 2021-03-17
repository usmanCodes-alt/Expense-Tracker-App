package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetracker.adapter.RecyclerViewAdapter;
import com.example.expensetracker.data.OnNoteClickedListener;
import com.example.expensetracker.models.ApplicationViewModel;
import com.example.expensetracker.models.Budget;
import com.example.expensetracker.models.Expense;
import com.example.expensetracker.utils.Categories;
import com.example.expensetracker.utils.Converter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.expensetracker.AddExpense.EXTRA_AMOUNT;
import static com.example.expensetracker.AddExpense.EXTRA_CATEGORY;
import static com.example.expensetracker.AddExpense.EXTRA_ID;
import static com.example.expensetracker.AddExpense.EXTRA_TITLE;

public class MainActivity extends AppCompatActivity implements OnNoteClickedListener {

    public static final int ADD_EXPENSE_REQUEST = 1;
    public static final int UPDATE_EXPENSE_REQUEST = 2;
    public static final String UPDATE_EXTRA_TITLE = "Expense Title";
    public static final String UPDATE_EXTRA_AMOUNT = "Expense Amount";
    public static final String UPDATE_EXTRA_CATEGORY = "Expense Category";
    public static final String UPDATE_EXTRA_ID = "Expense Id";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ApplicationViewModel applicationViewModel;
    private FloatingActionButton addNewExpenseButton;
    private TextView userBudgetTextView;
    private TextView totalExpensesTextView;
    private Button addNewBudgetButton;
    private Button deleteBudgetButton;
    private Budget budgetObject;
    private AddBudgetBottomSheet budgetBottomSheet;
    private int userBudget = 0;
    private int totalExpenses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationViewModel =
                new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                        .create(ApplicationViewModel.class);

        addNewExpenseButton = findViewById(R.id.add_new_fab);
        userBudgetTextView = findViewById(R.id.user_budget_amount);
        totalExpensesTextView = findViewById(R.id.expense_amount);
        addNewBudgetButton = findViewById(R.id.add_new_budget);
        deleteBudgetButton = findViewById(R.id.delete_budget);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter(MainActivity.this, this);

        //set adapter when you have live data coming
        applicationViewModel.getAllExpenses().observe(this, expenses -> {
            adapter.setExpensesList(expenses);
            recyclerView.setAdapter(adapter);
            totalExpenses = 0;
            for (Expense expense: expenses) {
                totalExpenses = totalExpenses + expense.getAmount();
            }
            String totalExpensesString = String.format(getResources().getString(R.string.total_expenses_text), totalExpenses);
            totalExpensesTextView.setText(totalExpensesString);
            decideTextColor();
        });

        ApplicationViewModel.getBudgetLiveData().observe(this, budget -> {
            if (budget != null) {
                userBudget = budget.getAmount();
                budgetObject = budget;
            }
            else {
                userBudget = 0;
            }
            String text = String.format(getResources().getString(R.string.budget_text), userBudget);
            userBudgetTextView.setText(text);
            decideTextColor();
        });

        addNewExpenseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpense.class);
            intent.putExtra("request code", ADD_EXPENSE_REQUEST);
            startActivityForResult(intent, ADD_EXPENSE_REQUEST);
        });

        addNewBudgetButton.setOnClickListener(v -> {
            budgetBottomSheet = new AddBudgetBottomSheet();
            budgetBottomSheet.show(getSupportFragmentManager(), "Budget bottom sheet");
        });

        deleteBudgetButton.setOnClickListener(v -> {
            if (budgetObject == null) {
                Toast.makeText(MainActivity.this, "You do not have any budget set", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            ApplicationViewModel.delete(budgetObject);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String expenseTitle = data.getStringExtra(EXTRA_TITLE);
            int expenseAmount = data.getIntExtra(EXTRA_AMOUNT, 0);
            String expenseCategory = data.getStringExtra(EXTRA_CATEGORY);

            Categories category = Converter.toCategory(expenseCategory);
            Expense expense = new Expense(expenseTitle, expenseAmount, category);
            if (requestCode == ADD_EXPENSE_REQUEST) {
                ApplicationViewModel.insert(expense);
            }
            if (requestCode == UPDATE_EXPENSE_REQUEST) {
                int id = data.getIntExtra(EXTRA_ID, -1);
                if (id == -1) {
                    Toast.makeText(this, "Can not update expense", Toast.LENGTH_SHORT).show();
                }
                expense.setId(id);
                ApplicationViewModel.update(expense);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ApplicationViewModel.deleteAllExpenses();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteClicked(Expense expenseClicked) {
        Intent intent = new Intent(this, AddExpense.class);
        intent.putExtra(UPDATE_EXTRA_TITLE, expenseClicked.getTitle());
        intent.putExtra(UPDATE_EXTRA_AMOUNT, expenseClicked.getAmount());
        intent.putExtra(UPDATE_EXTRA_CATEGORY, expenseClicked.getCategory().name());
        intent.putExtra(UPDATE_EXTRA_ID, expenseClicked.getId());
        intent.putExtra("request code", UPDATE_EXPENSE_REQUEST);
        startActivityForResult(intent, UPDATE_EXPENSE_REQUEST);
    }

    private void decideTextColor() {
        if (totalExpenses > userBudget) {
            totalExpensesTextView.setTextColor(ContextCompat
                    .getColor(MainActivity.this, R.color.dark_red));
            userBudgetTextView.setTextColor(ContextCompat
                    .getColor(MainActivity.this, R.color.dark_red));
        }
        else {
            totalExpensesTextView.setTextColor(ContextCompat
                    .getColor(MainActivity.this, R.color.secondary_color));
            userBudgetTextView.setTextColor(ContextCompat
                    .getColor(MainActivity.this, R.color.black));
        }
    }
}
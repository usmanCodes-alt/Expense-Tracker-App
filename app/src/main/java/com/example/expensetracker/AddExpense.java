package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.expensetracker.utils.Categories;
import static com.example.expensetracker.MainActivity.UPDATE_EXPENSE_REQUEST;
import static com.example.expensetracker.MainActivity.UPDATE_EXTRA_AMOUNT;
import static com.example.expensetracker.MainActivity.UPDATE_EXTRA_CATEGORY;
import static com.example.expensetracker.MainActivity.UPDATE_EXTRA_ID;
import static com.example.expensetracker.MainActivity.UPDATE_EXTRA_TITLE;

public class AddExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_AMOUNT = "amount";
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_ID = "id";
    private EditText expenseTitleEditText;
    private EditText expenseAmountEditText;
    private Spinner expenseCategorySpinner;
    private Button saveButton;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseTitleEditText = findViewById(R.id.expense_title);
        expenseAmountEditText = findViewById(R.id.expense_amount);
        expenseCategorySpinner = findViewById(R.id.category_spinner);
        saveButton = findViewById(R.id.save_button);

        Intent intent = getIntent();

        // Array adapter to fill our spinner with text in strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.categories,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseCategorySpinner.setAdapter(adapter);
        expenseCategorySpinner.setOnItemSelectedListener(this);

        if (intent.getIntExtra("request code", 0) == UPDATE_EXPENSE_REQUEST) {
            expenseTitleEditText.setText(intent.getStringExtra(UPDATE_EXTRA_TITLE));
            expenseAmountEditText.setText(String.valueOf(intent.getIntExtra(UPDATE_EXTRA_AMOUNT, 0)));
            String expenseCategory = intent.getStringExtra(UPDATE_EXTRA_CATEGORY);
            expenseCategory = expenseCategory.replace("_", " ");
            expenseCategory = capitalizeFirst(expenseCategory);
            expenseCategorySpinner.setSelection(adapter.getPosition(expenseCategory));
        }

        saveButton.setOnClickListener(v -> {
            String expenseTitle = expenseTitleEditText.getText().toString().trim();
            String expenseAmount = expenseAmountEditText.getText().toString();
            if (expenseTitle.isEmpty() || expenseAmount.isEmpty()) {
                Toast.makeText(AddExpense.this, "Please provide expense title and amount.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int amount = Integer.parseInt(expenseAmount);
            if (category == null) {
                category = Categories.MUST_HAVE.toString();
            }
            int id = intent.getIntExtra(UPDATE_EXTRA_ID, -1);
            if (id != -1) {
                intent.putExtra(EXTRA_ID, id);
            }
            intent.putExtra(EXTRA_TITLE, expenseTitle);
            intent.putExtra(EXTRA_AMOUNT, amount);
            intent.putExtra(EXTRA_CATEGORY, category);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                category = Categories.MUST_HAVE.toString();
                break;
            case 1:
                category = Categories.NICE_TO_HAVE.toString();
                break;
            case 2:
                category = Categories.NOT_IMPORTANT.toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private String capitalizeFirst(String word) {
        word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        return word;
    }
}
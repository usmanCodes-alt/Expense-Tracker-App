package com.example.expensetracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.expensetracker.models.ApplicationViewModel;
import com.example.expensetracker.models.Budget;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddBudgetBottomSheet extends BottomSheetDialogFragment {

    private EditText budgetAmountEditText;
    private Button saveBudgetButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        budgetAmountEditText = view.findViewById(R.id.budget_amount);
        saveBudgetButton = view.findViewById(R.id.save_budget_button);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveBudgetButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(budgetAmountEditText.getText().toString())) {
                Toast.makeText(getContext(), "Please provide budget", Toast.LENGTH_SHORT).show();
                return;
            }
            int budgetAmount = Integer.parseInt(budgetAmountEditText.getText().toString());
            if (budgetAmount < 0) {
                Toast.makeText(getContext(), "Please provide value greater than 0.", Toast.LENGTH_SHORT).show();
            }
            Budget budget = new Budget(budgetAmount);
            if (ApplicationViewModel.getBudgetLiveData().getValue() != null) {
                // budget already set, update it
                budget.setId(ApplicationViewModel
                        .getBudgetLiveData().getValue().getId());
                ApplicationViewModel.update(budget);
            }
            else {
                ApplicationViewModel.insert(budget);
            }
            budgetAmountEditText.setText("");
            AddBudgetBottomSheet.this.dismiss();
        });
    }
}

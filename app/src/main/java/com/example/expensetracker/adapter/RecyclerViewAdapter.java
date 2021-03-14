package com.example.expensetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.data.OnNoteClickedListener;
import com.example.expensetracker.models.ApplicationViewModel;
import com.example.expensetracker.models.Expense;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Expense> expensesList;
    private Context context;
    private OnNoteClickedListener onNoteClickedListener;

    public RecyclerViewAdapter(Context context, OnNoteClickedListener onNoteClickedListener) {
        this.context = context;
        this.onNoteClickedListener = onNoteClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ViewHolder(view, onNoteClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense currentExpense = expensesList.get(position);
        holder.expenseHeadingTextView.setText(currentExpense.getTitle());
        holder.expenseAmountTextView.setText(String.valueOf(currentExpense.getAmount()));

        holder.expenseItemMenuOptions.setOnClickListener(v -> {
            // show pop up menu
            PopupMenu menu = new PopupMenu(context, holder.expenseItemMenuOptions);
            menu.getMenuInflater().inflate(R.menu.expense_menu, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                int position1 = holder.getAdapterPosition();
                Expense expenseToDelete = expensesList.get(position1);
                ApplicationViewModel.delete(expenseToDelete);
                notifyDataSetChanged();
                return true;
            });
            menu.show();
        });
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public void setExpensesList(List<Expense> expensesList) {
        this.expensesList = expensesList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView expenseHeadingTextView;
        public TextView expenseAmountTextView;
        public ImageButton expenseItemMenuOptions;
        OnNoteClickedListener onNoteClickedListener;

        public ViewHolder(@NonNull View itemView, OnNoteClickedListener onNoteClickedListener) {
            super(itemView);
            expenseHeadingTextView = itemView.findViewById(R.id.expense_heading);
            expenseAmountTextView = itemView.findViewById(R.id.expense_amount);
            expenseItemMenuOptions = itemView.findViewById(R.id.menu_options);
            this.onNoteClickedListener = onNoteClickedListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // here call the interface method
            Expense expenseClicked = expensesList.get(getAdapterPosition());
            onNoteClickedListener.onNoteClicked(expenseClicked);
        }
    }
}

package com.example.zohaibsiddique.expensecalculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;

    private final int EXPENSE = 0;
    Context context;

    ComplexRecyclerViewAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Expense) {
            return EXPENSE;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case EXPENSE:
                View v1 = inflater.inflate(R.layout.view_items_expense, viewGroup, false);
                viewHolder = new ViewHolderExpense(v1);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case EXPENSE:
                ViewHolderExpense viewExpense = (ViewHolderExpense) viewHolder;
                configureViewHolderExpense(viewExpense, position);
                break;
        }
    }

    private void configureViewHolderExpense(ViewHolderExpense viewExpense, int position) {
        Expense expense = (Expense) items.get(position);
        if (expense != null) {
            viewExpense.getName().setText(expense.getTitle());
            viewExpense.getDate().setText(expense.getDate());
            viewExpense.getType().setText(expense.getType());
            String value = context.getString(R.string.sum, expense.getValue());
            viewExpense.getValue().setText(value);
        }
    }
}

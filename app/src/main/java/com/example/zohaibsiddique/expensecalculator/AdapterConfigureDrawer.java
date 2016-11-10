package com.example.zohaibsiddique.expensecalculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

class AdapterConfigureDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;

    private final int LEDGER = 0;
    Context context;

    AdapterConfigureDrawer(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Ledger) {
            return LEDGER;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case LEDGER:
                View v1 = inflater.inflate(R.layout.view_configure_drawer_items, viewGroup, false);
                viewHolder = new ViewHolderConfigureDrawer(v1);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case LEDGER:
                ViewHolderConfigureDrawer view = (ViewHolderConfigureDrawer) viewHolder;
                configureViewHolder(view, position);
                break;
        }
    }

    private void configureViewHolder(ViewHolderConfigureDrawer viewHolder, int position) {
        Ledger expense = (Ledger) items.get(position);
        if (expense != null) {
            viewHolder.getName().setText(expense.getTitle());
            viewHolder.getValue().setText(expense.getValue());
        }
    }
}

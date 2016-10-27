package com.example.zohaibsiddique.expensecalculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class AdapterViewItems extends RecyclerView.Adapter<AdapterViewItems.MainViewHolder> {
    private LayoutInflater inflater;
    List<HashMap<String, Object>> list;
    Context context;


    public AdapterViewItems(Context context, List<HashMap<String, Object>> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_items, null);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        try {
            final String NAME = "name";
            final String VALUE = "value";
            final String DATE = "date";
            holder.name.setText(list.get(position).get(NAME).toString());
            holder.value.setText(list.get(position).get(VALUE).toString());
            holder.date.setText(list.get(position).get(DATE).toString());
            holder.type.setText(list.get(position).get("type").toString());
        } catch (NullPointerException e) {
            Log.d("onBindViewHolder", " error is" + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView name,value,date,type;

        private MainViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_expense);
            value = (TextView) itemView.findViewById(R.id.value_expense);
            date = (TextView) itemView.findViewById(R.id.date_expense);
            type = (TextView) itemView.findViewById(R.id.type_expense);
        }
    }
}
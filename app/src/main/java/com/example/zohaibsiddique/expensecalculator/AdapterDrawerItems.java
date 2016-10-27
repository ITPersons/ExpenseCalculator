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

public class AdapterDrawerItems extends RecyclerView.Adapter<AdapterDrawerItems.MainViewHolder> {
    LayoutInflater inflater;
    List<HashMap<String, Object>> list;
    Context context;


    public AdapterDrawerItems(Context context, List<HashMap<String, Object>> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_items, null);
        view.setSelected(true);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        try {
            final String NAME_TYPE = "name";
            holder.name.setText(list.get(position).get(NAME_TYPE).toString());
        } catch (NullPointerException e) {
            Log.d("onBindViewHolder", " error is" + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        private MainViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_type);
        }
    }
}
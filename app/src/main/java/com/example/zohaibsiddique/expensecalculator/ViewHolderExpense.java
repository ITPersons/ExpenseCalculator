package com.example.zohaibsiddique.expensecalculator;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class ViewHolderExpense extends RecyclerView.ViewHolder {

    private TextView name, value, date, type;

    ViewHolderExpense(View v) {
        super(v);
        name = (TextView) itemView.findViewById(R.id.name_expense);
        value = (TextView) itemView.findViewById(R.id.value_expense);
        date = (TextView) itemView.findViewById(R.id.date_expense);
        type = (TextView) itemView.findViewById(R.id.type_expense);
    }

    TextView getName() {
        return name;
    }

    TextView getValue() {
        return value;
    }

    TextView getDate() {
        return date;
    }

    TextView getType() {
        return type;
    }
}

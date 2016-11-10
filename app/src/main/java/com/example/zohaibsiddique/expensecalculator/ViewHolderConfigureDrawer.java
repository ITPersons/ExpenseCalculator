package com.example.zohaibsiddique.expensecalculator;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class ViewHolderConfigureDrawer extends RecyclerView.ViewHolder {

    private TextView name, value;

    ViewHolderConfigureDrawer(View v) {
        super(v);
        name = (TextView) itemView.findViewById(R.id.name_configure_drawer);
        value = (TextView) itemView.findViewById(R.id.value_configure_drawer);
    }

    TextView getName() {
        return name;
    }
    TextView getValue() {
        return value;
    }
}

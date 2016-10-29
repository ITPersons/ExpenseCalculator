package com.example.zohaibsiddique.expensecalculator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

class CustomListAdapter extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    private int selectedItemPosition;
    TextView name;
    List<String> list;
    private boolean boldCheck;
    private int count;

    CustomListAdapter(Context context, List<String> list) {
        super(context, R.layout.single_list_item_view, list);
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_list_item_view, parent, false);
            name = (TextView) convertView.findViewById(R.id.name_single_list_item_view);

        }

        name.setText(list.get(position));

        if(boldCheck) {
            if(position == 0) {
                setTypeFace(convertView, Typeface.BOLD);
                showCounts(convertView, "("+String.valueOf(count)+")");
            }
        } else {
            if(position == 0) {
                setTypeFace(convertView, Typeface.NORMAL);
                showCounts(convertView, "");
            }
        }

        if (position == selectedItemPosition) {
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
    }

    void makeStyleBold(boolean check) {
        boldCheck = check;
    }

    void countSelection(int count) {
        this.count = count;
    }

    private void setTypeFace(View convertView, int style) {
        TextView name = (TextView) convertView.findViewById(R.id.name_single_list_item_view);
        name.setTypeface(null, style);
    }

    private void showCounts(View convertView, String count) {
        TextView counts = (TextView) convertView.findViewById(R.id.counts_single_list_item_view);
        counts.setText(count);
        counts.setTypeface(null, Typeface.BOLD);
    }
}

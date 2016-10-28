package com.example.zohaibsiddique.expensecalculator;

import android.content.Context;
import android.graphics.Color;
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
//    int boldPosition;


    CustomListAdapter(Context context, List<String> list) {
        super(context, R.layout.single_list_item_view, list);
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_list_item_view, parent, false);
            name = (TextView) convertView.findViewById(R.id.list_item_left_filter_contents);
        }

        name.setText(list.get(position));

//        if(position == boldPosition) {
//            setTypeFace(convertView, Typeface.BOLD);
//        }

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

//    void boldPosition(int position) {
//        boldPosition = position;
//    }
//
//    void setTypeFace(View convertView, int style) {
//        TextView name = (TextView) convertView.findViewById(R.id.list_item_left_filter_contents);
//        name.setTypeface(null, style);
//    }

//    void makeBold(View convertView, int style) {
//        final String PREFERENCES_FILTER = "filter";
//        final String KEY_PREFERENCES = "arrayList";
//        List<String> stateList;
//        SessionManager sessionManager = new SessionManager();
//        SharedPreferences editor = getContext().getSharedPreferences(PREFERENCES_FILTER, Context.MODE_PRIVATE);
//
//        if(editor.contains(KEY_PREFERENCES)) {
//            if(sessionManager.getPreferences(getContext(), PREFERENCES_FILTER, KEY_PREFERENCES).isEmpty()) {
//
//                Utility.shortToast(getContext(), String.valueOf("state empty"));
//
//            } else {
//                stateList = sessionManager.getPreferences(getContext(), PREFERENCES_FILTER, KEY_PREFERENCES);
//                if(stateList.isEmpty()) {
//                    Utility.shortToast(getContext(), "empty");
//                } else {
//                    setTypeFace(convertView, style);
//                }
//            }
//        }
//    }
}

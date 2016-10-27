package com.example.zohaibsiddique.expensecalculator;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateFragment extends Fragment{
    String keyDate = null;
    getDateFromDateFragment getDate;
    DatePicker datePicker;
    SessionManager sessionManager;
    final String PREFERENCES_FILTER = "filter";
    final String DATE_KEY_PREFERENCES = "date_key_date";
    final String MONTH_KEY_PREFERENCES = "month_key_date";
    final String YEAR_KEY_PREFERENCES = "year_key_date";
    List<String> list = new ArrayList<>();


    public interface getDateFromDateFragment{
        void getDateFromDateFragment(String date);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            getDate = (getDateFromDateFragment) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.date_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionManager = new SessionManager();

        datePicker = (DatePicker) getActivity().findViewById(R.id.date_picker_filter);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        keyDate = String.valueOf(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        getDate.getDateFromDateFragment(keyDate);

        SharedPreferences editor = getActivity().getSharedPreferences(PREFERENCES_FILTER, Context.MODE_PRIVATE);
        if(editor.contains(DATE_KEY_PREFERENCES) && editor.contains(MONTH_KEY_PREFERENCES) && editor.contains(YEAR_KEY_PREFERENCES)) {
            String d = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, DATE_KEY_PREFERENCES);
            String m = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, MONTH_KEY_PREFERENCES);
            String y = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, YEAR_KEY_PREFERENCES);
            datePicker.init(Integer.valueOf(y), Integer.valueOf(m), Integer.valueOf(d), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
                    onDateChange();
                }
            });
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
                    onDateChange();
                }
            });
        }
    }

    private void onDateChange() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        keyDate = String.valueOf(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        getDate.getDateFromDateFragment(keyDate);

        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, DATE_KEY_PREFERENCES, String.valueOf(day));
        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, MONTH_KEY_PREFERENCES, String.valueOf(month));
        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, YEAR_KEY_PREFERENCES, String.valueOf(year));
    }
}


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

import java.util.Calendar;

public class FromToDateFragment extends Fragment{
    String fromKeyDate, toKeyDate = null;
    getFromToDateFromFromToDateFragment getDate;
    SessionManager sessionManager;
    DatePicker fromDatePicker;
    DatePicker toDatePicker;
    final String PREFERENCES_FILTER = "filter";
    final String DATE_KEY_PREFERENCES_FROM = "date_key_from";
    final String MONTH_KEY_PREFERENCES_FROM = "month_key_from";
    final String YEAR_KEY_PREFERENCES_FROM = "year_key_from";
    final String DATE_KEY_PREFERENCES_TO = "date_key_to";
    final String MONTH_KEY_PREFERENCES_TO = "month_key_to";
    final String YEAR_KEY_PREFERENCES_TO = "year_key_to";

    public interface getFromToDateFromFromToDateFragment{
        void getFromDateFromDateFragment(String fromDate);
        void getToDateFromDateFragment(String toDate);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            getDate = (getFromToDateFromFromToDateFragment) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.from_to_date_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionManager = new SessionManager();

        fromDatePicker = (DatePicker) getActivity().findViewById(R.id.from_date_picker_filter);
        toDatePicker = (DatePicker) getActivity().findViewById(R.id.to_date_picker_filter);
        int fromDay = fromDatePicker.getDayOfMonth();
        int fromMonth = fromDatePicker.getMonth() + 1;
        int fromYear = fromDatePicker.getYear();
        int toDay = toDatePicker.getDayOfMonth();
        int toMonth = toDatePicker.getMonth() + 1;
        int toYear = toDatePicker.getYear();

        fromKeyDate = String.valueOf(new StringBuilder().append(fromDay).append("/").append(fromMonth).append("/").append(fromYear));
        toKeyDate = String.valueOf(new StringBuilder().append(toDay).append("/").append(toMonth).append("/").append(toYear));

        getDate.getFromDateFromDateFragment(fromKeyDate);
        getDate.getToDateFromDateFragment(toKeyDate);

        SharedPreferences editor = getActivity().getSharedPreferences(PREFERENCES_FILTER, Context.MODE_PRIVATE);

        if(editor.contains(DATE_KEY_PREFERENCES_FROM) && editor.contains(MONTH_KEY_PREFERENCES_FROM) && editor.contains(YEAR_KEY_PREFERENCES_FROM)) {
            String d = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, DATE_KEY_PREFERENCES_FROM);
            String m = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, MONTH_KEY_PREFERENCES_FROM);
            String y = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, YEAR_KEY_PREFERENCES_FROM);
            fromDatePicker.init(Integer.valueOf(y), Integer.valueOf(m), Integer.valueOf(d), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
                    fromOnDateChange();
                }
            });
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            fromDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
                    fromOnDateChange();
                }
            });
        }

        if(editor.contains(DATE_KEY_PREFERENCES_TO) && editor.contains(MONTH_KEY_PREFERENCES_TO) && editor.contains(YEAR_KEY_PREFERENCES_TO)) {
            String d = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, DATE_KEY_PREFERENCES_TO);
            String m = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, MONTH_KEY_PREFERENCES_TO);
            String y = sessionManager.getDatePreferences(getActivity(), PREFERENCES_FILTER, YEAR_KEY_PREFERENCES_TO);
            toDatePicker.init(Integer.valueOf(y), Integer.valueOf(m), Integer.valueOf(d), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
                    toOnDateChange();
                }
            });
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            toDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
                    toOnDateChange();
                }
            });
        }
    }

    private void fromOnDateChange() {
        int day = fromDatePicker.getDayOfMonth();
        int month = fromDatePicker.getMonth();
        int year = fromDatePicker.getYear();

        fromKeyDate = String.valueOf(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        getDate.getFromDateFromDateFragment(fromKeyDate);

        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, DATE_KEY_PREFERENCES_FROM, String.valueOf(day));
        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, MONTH_KEY_PREFERENCES_FROM, String.valueOf(month));
        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, YEAR_KEY_PREFERENCES_FROM, String.valueOf(year));
    }

    private void toOnDateChange() {
        int day = toDatePicker.getDayOfMonth();
        int month = toDatePicker.getMonth();
        int year = toDatePicker.getYear();

        toKeyDate = String.valueOf(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        getDate.getToDateFromDateFragment(toKeyDate);

        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, DATE_KEY_PREFERENCES_TO, String.valueOf(day));
        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, MONTH_KEY_PREFERENCES_TO, String.valueOf(month));
        sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, YEAR_KEY_PREFERENCES_TO, String.valueOf(year));
    }
}



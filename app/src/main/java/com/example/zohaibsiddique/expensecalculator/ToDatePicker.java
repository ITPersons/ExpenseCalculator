package com.example.zohaibsiddique.expensecalculator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class ToDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    String keyDate;
    getToDate getDate;

    public interface getToDate{
        void toDate(String toDate);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            getDate = (getToDate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(view.isShown()) {
            keyDate = String.valueOf(new StringBuilder().append(day).append("/").append(month+1).append("/").append(year));
            getDate.toDate(keyDate);
            TextView toDate = (TextView) getActivity().findViewById(R.id.to_date);
            toDate.setText(keyDate);

            SessionManager sessionManager = new SessionManager();
            final String PREFERENCES_FILTER = "filter";
            final String DATE_KEY_PREFERENCES_TO = "key_date_to";
            final String MONTH_KEY_PREFERENCES_TO = "key_month_to";
            final String YEAR_KEY_PREFERENCES_TO = "key_year_to";
            sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, DATE_KEY_PREFERENCES_TO, String.valueOf(day));
            sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, MONTH_KEY_PREFERENCES_TO, String.valueOf(month+1));
            sessionManager.setDatePreferences(getActivity(), PREFERENCES_FILTER, YEAR_KEY_PREFERENCES_TO, String.valueOf(year));
        }
    }
}
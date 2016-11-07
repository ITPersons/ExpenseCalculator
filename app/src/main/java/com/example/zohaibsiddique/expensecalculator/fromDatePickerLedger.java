package com.example.zohaibsiddique.expensecalculator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class fromDatePickerLedger extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    String keyDate;
//    getFromDate getDate;


//    public interface getFromDate{
//        void fromDate(String fromDate);
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            getDate = (getFromDate) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString());
//        }
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(view.isShown()) {
            EditText fromDate = (EditText) getActivity().findViewById(R.id.from_date_edit_text_ledger);
            String date = String.valueOf(new StringBuilder().append(day<10?"0"+day:day).append("/").append(month<10?"0"+month:month).append("/").append(year));
            fromDate.setText(date);
        }

    }
}
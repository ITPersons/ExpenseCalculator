package com.example.zohaibsiddique.expensecalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddLedger extends AppCompatActivity {

    TextInputLayout layoutLedgerTitle, layoutStartingBalance, layoutFromDate, layoutToDate;
    Button fromDateButton, toDateButton;
    EditText addTitleEditText, addStartingBalanceEditText, fromDateEditText, toDateEditText;
    String fromDate, toDate;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ledger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        db = new DB(AddLedger.this);
        getLayoutReferences();

        List<Ledger> ledgerList = new ArrayList<>();
        Cursor cursor = db.selectLedger();
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Utility.shortToast(AddLedger.this, "Empty list");
        } else {
            for (int i = 0; i < cursor.getCount(); i++) {
                final String ID = "id";final String TITLE = "title";final String STARTING_BALANCE = "starting_balance";final String DATE = "date";final String FROM_DATE = "from_date";final String TO_DATE = "to_date";
                String id = cursor.getString(cursor.getColumnIndex(ID));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                String startingBalance = cursor.getString(cursor.getColumnIndex(STARTING_BALANCE));
                String date = cursor.getString(cursor.getColumnIndex(DATE));
                String fromDate = cursor.getString(cursor.getColumnIndex(FROM_DATE));
                String toDate = cursor.getString(cursor.getColumnIndex(TO_DATE));

                Ledger ledger = new Ledger(id, title, startingBalance, date, fromDate, toDate);
                ledgerList.add(ledger);
                cursor.moveToNext();
            }
            cursor.close();
            }
        }

    private void getLayoutReferences() {
        layoutLedgerTitle = (TextInputLayout) findViewById(R.id.text_input_layout_ledger_title);
        layoutStartingBalance = (TextInputLayout) findViewById(R.id.text_input_layout_starting_balance);
        layoutFromDate = (TextInputLayout) findViewById(R.id.text_input_layout_from_date_ledger);
        layoutToDate = (TextInputLayout) findViewById(R.id.text_input_layout_to_date_ledger);
        fromDateButton = (Button) findViewById(R.id.from_date_ledger);
        fromDateButton.setOnClickListener(new OnClickListener());
        toDateButton = (Button) findViewById(R.id.to_date_ledger);
        toDateButton.setOnClickListener(new OnClickListener());
        addTitleEditText = (EditText) findViewById(R.id.title_ledger);
        addTitleEditText.addTextChangedListener(new addNewItemTextWatcher(addTitleEditText));
        addStartingBalanceEditText = (EditText) findViewById(R.id.starting_balance_ledger);
        addStartingBalanceEditText.addTextChangedListener(new addNewItemTextWatcher(addStartingBalanceEditText));
        fromDateEditText = (EditText) findViewById(R.id.from_date_edit_text_ledger);
        fromDateEditText.addTextChangedListener(new addNewItemTextWatcher(fromDateEditText));
        toDateEditText = (EditText) findViewById(R.id.to_date_edit_text_ledger);
        toDateEditText.addTextChangedListener(new addNewItemTextWatcher(toDateEditText));
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.from_date_ledger) {
                showDialog(1);
            }
            if(view.getId() == R.id.to_date_ledger) {
                showDialog(2);
            }
        }

    }

    private class addNewItemTextWatcher implements TextWatcher {
        private View view;

        private addNewItemTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.title_ledger:
                    Utility.validateEditText(addTitleEditText, layoutLedgerTitle, "Enter valid title");
                    break;
                case R.id.starting_balance_ledger:
                    Utility.validateEditText(addStartingBalanceEditText, layoutStartingBalance, "Enter valid starting balance");
                    break;
                case R.id.from_date_edit_text_ledger:
                    Utility.validateEditText(fromDateEditText, layoutFromDate, "Please choose date");
                    break;
                case R.id.to_date_edit_text_ledger:
                    Utility.validateEditText(toDateEditText, layoutToDate, "Please choose date");
                    break;
            }

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (id == 1) {
            return new DatePickerDialog(this, fromDatePicker, year, month, day);
        }
        if (id == 2) {
            return new DatePickerDialog(this, toDatePicker, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener fromDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int months = month + 1;
            if (view.isShown()) {
                fromDate = String.valueOf(new StringBuilder().append(day).append("/").append(months).append("/").append(year));
                fromDateEditText.setText(fromDate);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener toDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int months = month + 1;
            if (view.isShown()) {
                toDate = String.valueOf(new StringBuilder().append(day).append("/").append(months).append("/").append(year));
                toDateEditText.setText(toDate);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ledger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.save) {
            if(validateEditText()) {
                if(db.addLedger(addTitleEditText.getText().toString(), addStartingBalanceEditText.getText().toString(),
                        Utility.currentTimeInMillis(), fromDateEditText.getText().toString(), toDateEditText.getText().toString())) {
                    Utility.successSnackBar(layoutFromDate, "Ledger saved", AddLedger.this);
                } else {
                    Utility.failSnackBar(layoutFromDate, "Error, try again", AddLedger.this);
                }
                return true;
            } else if(!validateEditText()) {
                Utility.failSnackBar(layoutFromDate, "Error, please remove error", AddLedger.this);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateEditText() {
        return Utility.validateEditText(addTitleEditText, layoutLedgerTitle, "Enter valid title") &&
                Utility.validateEditText(addStartingBalanceEditText, layoutStartingBalance, "Enter valid starting balance")
                && Utility.validateEditText(fromDateEditText, layoutFromDate, "Please choose date") &&
                Utility.validateEditText(toDateEditText, layoutToDate, "Please choose date");
    }
}

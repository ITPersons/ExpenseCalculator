package com.example.zohaibsiddique.expensecalculator;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
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
import android.widget.EditText;

public class AddLedger extends AppCompatActivity {

    TextInputLayout layoutLedgerTitle, layoutStartingBalance, layoutFromDate, layoutToDate;
    Button fromDateButton, toDateButton;
    EditText titleEditText, startingBalanceEditText, fromDateEditText, toDateEditText;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ledger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }

        db = new DB(AddLedger.this);
        getLayoutReferences();

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
        titleEditText = (EditText) findViewById(R.id.title_ledger);
        titleEditText.addTextChangedListener(new addNewItemTextWatcher(titleEditText));
        startingBalanceEditText = (EditText) findViewById(R.id.starting_balance_ledger);
        startingBalanceEditText.addTextChangedListener(new addNewItemTextWatcher(startingBalanceEditText));
        fromDateEditText = (EditText) findViewById(R.id.from_date_edit_text_ledger);
        fromDateEditText.addTextChangedListener(new addNewItemTextWatcher(fromDateEditText));
        toDateEditText = (EditText) findViewById(R.id.to_date_edit_text_ledger);
        toDateEditText.addTextChangedListener(new addNewItemTextWatcher(toDateEditText));
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FragmentManager manager = getFragmentManager();
            if(view.getId() == R.id.from_date_ledger) {
                DialogFragment dialog = new fromDatePickerLedger();
                dialog.show(manager, "fromDatePickerLedger");
            }
            if(view.getId() == R.id.to_date_ledger) {
                DialogFragment dialog = new toDatePickerLedger();
                dialog.show(manager, "toDatePickerLedger");
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
                    Utility.validateEditText(titleEditText, layoutLedgerTitle, "Enter valid title");
                    break;
                case R.id.starting_balance_ledger:
                    Utility.validateEditText(startingBalanceEditText, layoutStartingBalance, "Enter valid starting balance");
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

                if(db.isLedgerExist(titleEditText.getText().toString())) {
                    Utility.failSnackBar(layoutFromDate, "Error, ledger already existed", AddLedger.this);

                } else if (db.addLedger(titleEditText.getText().toString(),
                        startingBalanceEditText.getText().toString(),
                        Utility.currentTimeInMillis(),
                        Utility.simpleDateFormat(Utility.dateInMilliSecond(fromDateEditText.getText().toString())),
                        Utility.simpleDateFormat(Utility.dateInMilliSecond(toDateEditText.getText().toString())))) {

                    Utility.successSnackBar(layoutFromDate, "Ledger saved", AddLedger.this);
                } else {
                    Utility.failSnackBar(layoutFromDate, "Error, try again", AddLedger.this);
                }

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            } else if(!validateEditText()) {
                Utility.failSnackBar(layoutFromDate, "Error, please remove error", AddLedger.this);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateEditText() {
        return Utility.validateEditText(titleEditText, layoutLedgerTitle, "Enter valid title") &&
                Utility.validateEditText(startingBalanceEditText, layoutStartingBalance, "Enter valid starting balance")
                && Utility.validateEditText(fromDateEditText, layoutFromDate, "Please choose date") &&
                Utility.validateEditText(toDateEditText, layoutToDate, "Please choose date");
    }
}

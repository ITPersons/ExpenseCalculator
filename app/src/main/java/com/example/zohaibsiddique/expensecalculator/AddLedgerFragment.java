package com.example.zohaibsiddique.expensecalculator;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddLedgerFragment extends Fragment {

    TextInputLayout layoutLedgerTitle, layoutStartingBalance, layoutFromDate, layoutToDate;
    Button fromDateButton, toDateButton, saveButton;
    EditText titleEditText, startingBalanceEditText, fromDateEditText, toDateEditText;
    DB db;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_ledger_fragment, container, false);
        db = new DB(getActivity());
        getLayoutReferences();
        return view;

    }

    private void getLayoutReferences() {
        layoutLedgerTitle = (TextInputLayout) view.findViewById(R.id.text_input_layout_ledger_title);
        layoutStartingBalance = (TextInputLayout) view.findViewById(R.id.text_input_layout_starting_balance);
        layoutFromDate = (TextInputLayout) view.findViewById(R.id.text_input_layout_from_date_ledger);
        layoutToDate = (TextInputLayout) view.findViewById(R.id.text_input_layout_to_date_ledger);
        fromDateButton = (Button) view.findViewById(R.id.from_date_ledger);
        fromDateButton.setOnClickListener(new OnClickListener());
        toDateButton = (Button) view.findViewById(R.id.to_date_ledger);
        toDateButton.setOnClickListener(new OnClickListener());
        titleEditText = (EditText) view.findViewById(R.id.title_ledger);
        titleEditText.addTextChangedListener(new addNewItemTextWatcher(titleEditText));
        startingBalanceEditText = (EditText) view.findViewById(R.id.starting_balance_ledger);
        startingBalanceEditText.addTextChangedListener(new addNewItemTextWatcher(startingBalanceEditText));
        fromDateEditText = (EditText) view.findViewById(R.id.from_date_edit_text_ledger);
        fromDateEditText.addTextChangedListener(new addNewItemTextWatcher(fromDateEditText));
        toDateEditText = (EditText) view.findViewById(R.id.to_date_edit_text_ledger);
        toDateEditText.addTextChangedListener(new addNewItemTextWatcher(toDateEditText));
        saveButton = (Button) view.findViewById(R.id.save_ledger);
        saveButton.setOnClickListener(new OnClickListener());
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FragmentManager manager = getActivity().getFragmentManager();
            if(view.getId() == R.id.from_date_ledger) {
                DialogFragment dialog = new fromDatePickerLedger();
                dialog.show(manager, "fromDatePickerLedger");
            }
            if(view.getId() == R.id.to_date_ledger) {
                DialogFragment dialog = new toDatePickerLedger();
                dialog.show(manager, "toDatePickerLedger");
            }
            if(view.getId() == R.id.save_ledger) {
                if(validateEditText()) {

                    if(db.isLedgerExist(titleEditText.getText().toString())) {
                        Utility.failSnackBar(layoutFromDate, "Error, ledger already existed", getActivity());

                    } else if (db.addLedger(titleEditText.getText().toString(),
                            startingBalanceEditText.getText().toString(),
                            Utility.currentTimeInMillis(),
                            Utility.simpleDateFormat(Utility.dateInMilliSecond(fromDateEditText.getText().toString())),
                            Utility.simpleDateFormat(Utility.dateInMilliSecond(toDateEditText.getText().toString())))) {

                        Utility.successSnackBar(layoutFromDate, "Ledger saved", getActivity());
                        titleEditText.setText("");
                        startingBalanceEditText.setText("");
                        fromDateEditText.setText("");
                        toDateEditText.setText("");
                        Utility.requestFocus(titleEditText, getActivity());
                        Utility.hintDisable(titleEditText, layoutLedgerTitle);
                        Utility.hintDisable(startingBalanceEditText, layoutStartingBalance);
                        Utility.hintDisable(fromDateEditText, layoutFromDate);
                        Utility.hintDisable(toDateEditText, layoutToDate);
                    } else {
                        Utility.failSnackBar(layoutFromDate, "Error, try again", getActivity());
                    }
                } else if(!validateEditText()) {
                    Utility.failSnackBar(layoutFromDate, "Error, please remove error", getActivity());
                }
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

    private boolean validateEditText() {
        return Utility.validateEditText(titleEditText, layoutLedgerTitle, "Enter valid title") &&
                Utility.validateEditText(startingBalanceEditText, layoutStartingBalance, "Enter valid starting balance")
                && Utility.validateEditText(fromDateEditText, layoutFromDate, "Please choose date") &&
                Utility.validateEditText(toDateEditText, layoutToDate, "Please choose date");
    }
}

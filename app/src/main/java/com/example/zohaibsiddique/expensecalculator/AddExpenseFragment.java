package com.example.zohaibsiddique.expensecalculator;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextInputLayout layoutExpenseName, layoutExpenseValue;
    private EditText addNameEditText, addValueEditText;
    private DB db;
    private List<String> arrayListType;
    View view;
    private String idType;
    private TextView dateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_expense, container, false);
        db = new DB(getActivity());
        getLayoutReferences();
        return view;
    }

    private void getLayoutReferences() {
        layoutExpenseName = (TextInputLayout) view.findViewById(R.id.text_input_layout_add_expense_name);
        addNameEditText = (EditText) view.findViewById(R.id.name_expense);
        addNameEditText.addTextChangedListener(new addNewItemTextWatcher(addNameEditText));

        layoutExpenseValue = (TextInputLayout) view.findViewById(R.id.text_input_layout_add_expense_value);
        addValueEditText = (EditText) view.findViewById(R.id.value_expense);
        addValueEditText.addTextChangedListener(new addNewItemTextWatcher(addValueEditText));

        dateTextView = (TextView) view.findViewById(R.id.date_expense);
        dateTextView.setOnClickListener(new OnClickListener());

        arrayListType = new ArrayList<>();
        arrayListType.clear();
        getTypes();
        Spinner typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
        typeSpinner.setOnItemSelectedListener(this);
        Utility.setSpinnerAdapterByArrayList(typeSpinner, getActivity(), arrayListType);

        Button saveButton = (Button) view.findViewById(R.id.save_add_expense);
        saveButton.setOnClickListener(new OnClickListener());
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.date_expense) {
                showDatePicker();
            }

            String nameExpense = addNameEditText.getText().toString().trim();
            String valueExpense = addValueEditText.getText().toString().trim();
            String dateExpense = Utility.simpleDateFormat(Utility.dateInMilliSecond(dateTextView.getText().toString()));

            if (view.getId() == R.id.save_add_expense) {
                if (validateInput()) {
                    if (db.isExpenseExist(nameExpense)) {
                        Utility.failSnackBar(layoutExpenseName, "Error, name already existed", getActivity());
                    } else {
                        if (db.addExpense(nameExpense, valueExpense, Utility.currentTimeInMillis(), dateExpense, idType)) {
                            Utility.successSnackBar(layoutExpenseName, "Save", getActivity());
                            addNameEditText.setText("");
                            addValueEditText.setText("");
                            dateTextView.setText(dateTextView.getText().toString());
                            Utility.requestFocus(addNameEditText, getActivity());
                            Utility.hintDisable(addNameEditText, layoutExpenseName);
                            Utility.hintDisable(addValueEditText, layoutExpenseValue);
                        } else {
                            Utility.failSnackBar(layoutExpenseName, "Error, try again", getActivity());
                        }
                    }

                } else if (!validateInput()) {
                    Utility.failSnackBar(layoutExpenseName, "Error, fields cannot be empty", getActivity());
                }
            }
        }
    }

    private void showDatePicker() {
        FragmentManager manager = getActivity().getFragmentManager();
        DialogFragment dialog = new ExpenseDatePicker();
        dialog.show(manager, "ExpenseDatePicker");
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
                case R.id.name_expense:
                    Utility.validateEditText(addNameEditText, layoutExpenseName, "Enter valid name");
                    break;
                case R.id.value_expense:
                    Utility.validateEditText(addValueEditText, layoutExpenseValue, "Enter valid value");
                    break;
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;

        if (spinner.getId() == R.id.type_spinner) {
            String type = String.valueOf(adapterView.getItemAtPosition(i));
            idType = db.getIdByType(type);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getTypes() {
        Cursor cursor = db.selectMainType();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            arrayListType.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        cursor.close();
    }

    private boolean validateInput() {
        return Utility.validateEditText(addNameEditText, layoutExpenseName, "Enter valid name") &&
                Utility.validateEditText(addValueEditText, layoutExpenseValue, "Enter valid value");
    }
}

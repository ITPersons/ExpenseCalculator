package com.example.zohaibsiddique.expensecalculator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private View addExpenseView;
    private TextInputLayout layoutAddMainType, layoutExpenseName, layoutExpenseValue;
    private EditText addMainType, addExpenseName, addExpenseValue = null;
    private RecyclerView recyclerView, recyclerViewDrawer;
    private ArrayList<HashMap<String, Object>> arrayList, arrayListDrawer;
    private DB db;
    private RecyclerTouchListener onTouchListener;
    private List<String> arrayListType;
    private String idType, nameMainType, typeId;
    private static long sum = 0;
    private TextView showValueExpense;
    private EditText fromDatePickerEditText, toDatePickerEditText;
    private final int CONFIGURE_DRAWER_REQUEST_CODE = 1;
    private final int TYPE_FILTER_REQUEST_CODE = 2;
    final String ID_EXPENSE = "id";
    final String NAME_EXPENSE = "name";
    final String VALUE_EXPENSE = "value";
    final String DATE_EXPENSE = "date";
    final String TYPE_ID_EXPENSE = "type_id";
//    private final int DATE_FILTER_REQUEST_CODE = 3;
//    private final int FROM_DATE_FILTER_REQUEST_CODE = 4;
//    private final int TO_DATE_FILTER_REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utility.startAnActivity(MainActivity.this, Filter.class);

        db = new DB(MainActivity.this);
        showValueExpense = (TextView) findViewById(R.id.show_value_expense);
        initializeValueOfExpense();

        viewDrawerItems();
        viewItems();

        DrawerClickListener();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();
            }
        });

        drawer(toolbar);
    }

    // Show Block list from database
    public void viewItems() {
        try {
            getReferencesForViewItemsRecyclerView();

            Cursor cursor = db.selectExpense();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < cursor.getCount(); i++) {
                    addValuesToArrayList(cursor);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            enableSwipe();

        } catch (Exception e) {
            Log.d("showItems", " failed " + e.getMessage());
        }
    }


    // Show Block list from database
    public void viewDrawerItems() {
        try {
            getReferencesForDrawerItemsRecyclerView();

            Cursor cursor = db.selectMainType();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < cursor.getCount(); i++) {
                    addValueToDrawerArrayList(cursor);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.d("viewDrawerItems", " failed " + e.getMessage());
        }
    }

    private void addValueToDrawerArrayList(Cursor cursor) {
        HashMap<String, Object> hm = new HashMap<>();
        final String ID_TYPE = "id", NAME_TYPE = "name";
        hm.put(ID_TYPE, cursor.getString(cursor.getColumnIndex(ID_TYPE)));
        hm.put(NAME_TYPE, cursor.getString(cursor.getColumnIndex(NAME_TYPE)));
        arrayListDrawer.add(hm);
    }

    public void viewItemsByType() {
        try {
            getReferencesForViewItemsRecyclerView();
            initializeValueOfExpense();

            Cursor cursor = db.selectExpenseByType(typeId);
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < cursor.getCount(); i++) {
                    addValuesToArrayList(cursor);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            enableSwipe();

        } catch (Exception e) {
            Log.d("viewItemsByType", " failed " + e.getMessage());
        }
    }

    private void chooseFromToDate() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_from_to_date_picker);
        View fromToDatePickerView = layoutInflater.inflate(R.layout.from_to_date_picker, linearLayout);
        Button fromDatePickerButton = (Button) fromToDatePickerView.findViewById(R.id.from_date_picker);
        fromDatePickerEditText = (EditText) fromToDatePickerView.findViewById(R.id.from_date_picker_editText);
        fromDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(2);
            }
        });

        Button toDatePickerButton = (Button) fromToDatePickerView.findViewById(R.id.to_date_picker);
        toDatePickerEditText = (EditText) fromToDatePickerView.findViewById(R.id.to_date_picker_editText);
        toDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(3);
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Choose date");
        alertDialogBuilder.setView(fromToDatePickerView);

        alertDialogBuilder.setCancelable(true)
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("SUBMIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String to = toDatePickerEditText.getText().toString();
                                String from = fromDatePickerEditText.getText().toString();

                                String fromDate = Utility.simpleDateFormat(Utility.milliseconds(from));
                                String toDate = Utility.simpleDateFormat(Utility.milliseconds(to));

                                Cursor cursor = db.selectFromToDate(fromDate, toDate);
                                initializeValueOfExpense();
                                getReferencesForViewItemsRecyclerView();
                                cursor.moveToFirst();
                                if (cursor.getCount() == 0) {
                                    Toast.makeText(MainActivity.this, "Empty list", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < cursor.getCount(); i++) {
                                        addValuesToArrayList(cursor);
                                        cursor.moveToNext();
                                    }
                                    cursor.close();
                                }
                                enableSwipe();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void addType() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_add_main_type) ;
        View addMainTypeView = layoutInflater.inflate(R.layout.add_main_type, linearLayout);
        layoutAddMainType = (TextInputLayout) addMainTypeView.findViewById(R.id.text_input_layout_add_main_type);
        addMainType = (EditText) addMainTypeView.findViewById(R.id.add_main_type);
        addMainType.addTextChangedListener(new addNewItemTextWatcher(addMainType));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Add type");
        alertDialogBuilder.setView(addMainTypeView);

        alertDialogBuilder.setCancelable(true)
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (Utility.validateEditText(addMainType, layoutAddMainType, "Enter valid type")) {
                                    String mainType = addMainType.getText().toString().trim();

                                    if (db.isMainTypeExisted(mainType)) {
                                        Utility.failSnackBar(recyclerView, "Error, type already existed. try another", MainActivity.this);
                                    } else {
                                        db.addMainType(mainType);
                                        viewDrawerItems();
                                        Utility.successSnackBar(recyclerView, "Type added", MainActivity.this);
                                    }
                                } else if (!Utility.validateEditText(addMainType, layoutAddMainType, "Enter valid type")) {
                                    addType();
                                    Utility.failSnackBar(recyclerView, "Error, field cannot be empty, try again", MainActivity.this);
                                }
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void addExpense() {

        getReferenceOfExpenseDialog();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Add Expense");
        alertDialogBuilder.setView(addExpenseView);

        alertDialogBuilder.setCancelable(true)
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                initializeValueOfExpense();
                                viewItems();

                            }
                        })
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String expenseName = addExpenseName.getText().toString().trim();
                                String expenseValue = addExpenseValue.getText().toString().trim();
                                if (validateInput()) {
                                    if (db.addExpense(expenseName, expenseValue, Utility.currentTimeInMillis()
                                            , Utility.simpleDateFormat(Long.valueOf(Utility.currentTimeInMillis())), idType)) {
                                        Utility.successSnackBar(recyclerView, "Expense added", MainActivity.this);
                                        addExpense();
                                    } else {
                                        Utility.failSnackBar(recyclerView, "Error, try again", MainActivity.this);
                                    }

                                } else if (!validateInput()) {
                                    Utility.failSnackBar(recyclerView, "Error, fields cannot be empty", MainActivity.this);
                                    addExpense();
                                }


                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



    private void showExpense(long expense) {
        sum = sum + expense;
        String messageSum = getString(R.string.sum, String.valueOf(sum));
        showValueExpense.setText(messageSum);
    }

    private void getReferencesForViewItemsRecyclerView() {
        arrayList = new ArrayList<>();
        AdapterViewItems adapter = new AdapterViewItems(MainActivity.this, arrayList);
        recyclerView = (RecyclerView) findViewById(R.id.view_item_recycle_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void addValuesToArrayList(Cursor cursor) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(ID_EXPENSE, cursor.getString(cursor.getColumnIndex(ID_EXPENSE)));
        hm.put(NAME_EXPENSE, cursor.getString(cursor.getColumnIndex(NAME_EXPENSE)));

        long valueExpense = cursor.getLong(cursor.getColumnIndex(VALUE_EXPENSE));
        showExpense(valueExpense);

        hm.put(VALUE_EXPENSE, String.valueOf(valueExpense));
        hm.put(DATE_EXPENSE, Utility.dateFormat(cursor.getLong(cursor.getColumnIndex(DATE_EXPENSE))));
        String id = cursor.getString(cursor.getColumnIndex(TYPE_ID_EXPENSE));
        hm.put("type", db.selectTypeById(id));
        arrayList.add(hm);
    }

    private void getReferencesForDrawerItemsRecyclerView() {
        arrayListDrawer = new ArrayList<>();
        recyclerViewDrawer = (RecyclerView) findViewById(R.id.drawer_items_recycle_view);
        AdapterDrawerItems adapterDrawer = new AdapterDrawerItems(MainActivity.this, arrayListDrawer);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewDrawer.setLayoutManager(mLayoutManager);
        recyclerViewDrawer.setAdapter(adapterDrawer);

        RecyclerTouchListener touchListener = new RecyclerTouchListener(this, recyclerViewDrawer);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        recyclerViewDrawer.setClickable(true);
                        recyclerViewDrawer.setSelected(true);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                    }
                });

        recyclerViewDrawer.addOnItemTouchListener(touchListener);
    }

    private void enableSwipe() {
        onTouchListener = new RecyclerTouchListener(this, recyclerView);
        onTouchListener
                .setSwipeOptionViews(R.id.edit, R.id.delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.delete) {
                            final String idExpense = arrayList.get(position).get(ID_EXPENSE).toString();
                            deleteExpense(idExpense);

                        } else if (viewID == R.id.edit) {
                            String id = arrayList.get(position).get(ID_EXPENSE).toString();
                            String name = arrayList.get(position).get(NAME_EXPENSE).toString();
                            String value = arrayList.get(position).get(VALUE_EXPENSE).toString();
                            updateExpense(id, name, value);
                        }
                    }
                });
        recyclerView.addOnItemTouchListener(onTouchListener);
    }

    private void deleteExpense(final String idExpense) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Confirmation");
        alertDialogBuilder.setMessage("Do you want to delete?");
        alertDialogBuilder.setCancelable(true)
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (db.deleteExpense(idExpense)) {
                                    Utility.successSnackBar(recyclerView, "Expense deleted", MainActivity.this);
                                    viewItems();
                                } else if (!db.deleteExpense(idExpense)) {
                                    Utility.failSnackBar(recyclerView, "Error, Expense not deleted, try again", MainActivity.this);
                                }
                            }

                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void DrawerClickListener() {
        final String NAME_TYPE = "name";
        onTouchListener = new RecyclerTouchListener(this, recyclerViewDrawer);
        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        nameMainType = arrayListDrawer.get(position).get(NAME_TYPE).toString();
                        if (nameMainType.contains("all") || nameMainType.equals("all")) {
                            initializeValueOfExpense();
                            viewItems();
                            closeDrawer();
                        } else {
                            typeId = db.selectIdByMainTypeName(nameMainType);
                            initializeValueOfExpense();
                            viewItemsByType();
                            closeDrawer();
                        }

                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        Utility.shortToast(getApplicationContext(), "Button in row " + (position + 1) + " clicked!");
                    }
                });
        recyclerViewDrawer.addOnItemTouchListener(onTouchListener);
    }

    private void getTypes() {
        final String NAME_TYPE = "name";
        Cursor cursor = db.selectMainType();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            arrayListType.add(cursor.getString(cursor.getColumnIndex(NAME_TYPE)));
            cursor.moveToNext();
        }
        cursor.close();
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
                    Utility.validateEditText(addExpenseName, layoutExpenseName, "Enter valid name");
                    break;
                case R.id.value_expense:
                    Utility.validateEditText(addExpenseValue, layoutExpenseValue, "Enter valid value");
                    break;
                case R.id.add_main_type:
                    Utility.validateEditText(addMainType, layoutAddMainType, "Enter valid type");
                    break;
            }

        }
    }

    private boolean validateInput() {
        return Utility.validateEditText(addExpenseName, layoutExpenseName, "Enter valid name") &&
                Utility.validateEditText(addExpenseValue, layoutExpenseValue, "Enter valid value");
    }

    private void drawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateExpense(final String idExpense, String name, String value) {

        getReferenceOfExpenseDialog();

        addExpenseName.setText(name);
        addExpenseValue.setText(value);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Update Expense");
        alertDialogBuilder.setView(addExpenseView);

        alertDialogBuilder.setCancelable(true)
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("UPDATE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String expenseName = addExpenseName.getText().toString().trim();
                                String expenseValue = addExpenseValue.getText().toString().trim();

                                if (db.updateExpense(Long.valueOf(idExpense), expenseName, expenseValue, idType)) {
                                    Utility.successSnackBar(recyclerView, "updated", MainActivity.this);
                                    viewItems();
                                } else if (!db.updateExpense(Long.valueOf(idExpense), expenseName, expenseValue, idType)) {
                                    Utility.failSnackBar(recyclerView, "not updated", MainActivity.this);
                                }
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void getReferenceOfExpenseDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_add_expense);
        addExpenseView = layoutInflater.inflate(R.layout.add_expense, linearLayout);

        layoutExpenseName = (TextInputLayout) addExpenseView.findViewById(R.id.text_input_layout_add_expense_name);
        addExpenseName = (EditText) addExpenseView.findViewById(R.id.name_expense);
        addExpenseName.addTextChangedListener(new addNewItemTextWatcher(addExpenseName));

        layoutExpenseValue = (TextInputLayout) addExpenseView.findViewById(R.id.text_input_layout_add_expense_value);
        addExpenseValue = (EditText) addExpenseView.findViewById(R.id.value_expense);
        addExpenseValue.addTextChangedListener(new addNewItemTextWatcher(addExpenseValue));

        arrayListType = new ArrayList<>();
        arrayListType.clear();
        getTypes();
        Spinner typeSpinner = (Spinner) addExpenseView.findViewById(R.id.type_spinner);
        typeSpinner.setOnItemSelectedListener(this);
        Utility.setSpinnerAdapterByArrayList(typeSpinner, MainActivity.this, arrayListType);
    }

    private void initializeValueOfExpense() {
        sum = 0;
        String messageSum = getString(R.string.sum, String.valueOf(sum));
        showValueExpense.setText(messageSum);
    }


    @Override
    protected Dialog onCreateDialog(int id) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (id == 1) {
            return new DatePickerDialog(this, singleDatePicker, year, month, day);
        }
        if (id == 2) {
            return new DatePickerDialog(this, fromDatePicker, year, month, day);
        }
        if (id == 3) {
            return new DatePickerDialog(this, toDatePicker, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener singleDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int months = month + 1;
            if (view.isShown()) {
                String keyDate = String.valueOf(new StringBuilder().append(day).append("/").append(months).append("/").append(year));
                initializeValueOfExpense();
                Cursor cursor = db.selectDateOfExpense(Utility.simpleDateFormat(Utility.milliseconds(keyDate)));
                getReferencesForViewItemsRecyclerView();
                cursor.moveToFirst();
                if (cursor.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Empty list", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        addValuesToArrayList(cursor);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                enableSwipe();
            }
        }
        };

    private DatePickerDialog.OnDateSetListener fromDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int months = month + 1;

            if (view.isShown()) {
                String fromDateKey = String.valueOf(new StringBuilder().append(day).append("/").append(months).append("/").append(year));
                fromDatePickerEditText.setText(fromDateKey);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener toDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int months = month + 1;
            if (view.isShown()) {
                String toDateKey = String.valueOf(new StringBuilder().append(day).append("/").append(months).append("/").append(year));
                toDatePickerEditText.setText(toDateKey);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_main_type_menu) {
            addType();
            return true;
        }
        if (id == R.id.configure_drawer) {
            Intent intent = new Intent(MainActivity.this, ConfigureDrawer.class);
            startActivityForResult(intent, CONFIGURE_DRAWER_REQUEST_CODE);
            return true;
        }
        if (id == R.id.date_picker) {
            showDialog(1);
            return true;
        }
        if (id == R.id.from_to_date_picker) {
            chooseFromToDate();
            return true;
        }
        if (id == R.id.add_ledger) {
            Utility.startAnActivity(MainActivity.this, AddLedger.class);
            return true;
        }
        if (id == R.id.filter) {
            Utility.startAnActivityForResult(MainActivity.this, MainActivity.this, Filter.class, TYPE_FILTER_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case CONFIGURE_DRAWER_REQUEST_CODE:
                    viewDrawerItems();
                    break;
                case TYPE_FILTER_REQUEST_CODE:
                        @SuppressWarnings("unchecked")
                        ArrayList<String> stringArrayList = (ArrayList<String>) data.getSerializableExtra("arrayListOfFilter");
//                    String a = data.getExtras().getString("date");
                        Utility.shortToast(MainActivity.this, stringArrayList.get(0));
                        break;

//                case DATE_FILTER_REQUEST_CODE:
////                    stringArrayList = (ArrayList<String>) data.getSerializableExtra("arrayListOfFilter");
//                    Utility.shortToast(MainActivity.this, "date");
//                    break;
//                case FROM_DATE_FILTER_REQUEST_CODE:
////                    stringArrayList = (ArrayList<String>) data.getSerializableExtra("arrayListOfFilter");
//                    Utility.shortToast(MainActivity.this, "from date");
//                    break;
//                case TO_DATE_FILTER_REQUEST_CODE:
////                    stringArrayList = (ArrayList<String>) data.getSerializableExtra("arrayListOfFilter");
//                    Utility.shortToast(MainActivity.this, "to date");
//                    break;
            }

        }
    }
}

package com.example.zohaibsiddique.expensecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private View addExpenseView;
    private TextInputLayout layoutAddMainType, layoutExpenseName, layoutExpenseValue;
    private EditText addMainType, addExpenseName, addExpenseValue;
    private RecyclerView recyclerView, recyclerViewDrawer;
    private ArrayList<Object> arrayListExpense;
    private ArrayList<Ledger> arrayListDrawer;
    private DB db;
    private RecyclerTouchListener onTouchListener;
    private List<String> arrayListType;
    private String idType, nameLedger, ledgerId;
    private static long sum, sumIncome = 0;
    private TextView showValueExpense, showValueIncome;
    private final int CONFIGURE_DRAWER_REQUEST_CODE = 1;
    private final int FILTER_REQUEST_CODE = 2;
    private final int ADD_LEDGER_REQUEST_CODE = 3;
    private final int ADD_NEW_REQUEST_CODE = 4;
    final String ID = "id";
    final String NAME = "name";
    final String VALUE = "value";
    final String DATE = "date";
    final String TYPE_ID = "type_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DB(MainActivity.this);
        showValueExpense = (TextView) findViewById(R.id.show_value_expense);
        showValueIncome = (TextView) findViewById(R.id.show_value_income);
        initializeSumValue();

        viewDrawerItems();
        viewItems();

        DrawerClickListener();

        Button addNewButton = (Button) findViewById(R.id.add_new_button);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.startAnActivityForResult(MainActivity.this, MainActivity.this, AddNew.class, ADD_NEW_REQUEST_CODE);
            }
        });

        drawer(toolbar);
    }

    private void viewItems() {
        try {
            getReferencesForViewItemsRecyclerView();
            initializeSumValue();
            Cursor cursorExpense = db.selectExpense();
            addValuesToArrayList(cursorExpense);
        } catch (Exception e) {
            Log.d("showItems", " failed " + e.getMessage());
        }
    }

    private void viewDrawerItems() {
        try {
            getReferencesForDrawerItemsRecyclerView();
            Cursor cursor = db.selectLedger();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Utility.shortToast(MainActivity.this, "Empty list");
            } else {
                for (int i = 0; i < cursor.getCount(); i++) {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    Ledger ledger = new Ledger(title);
                    arrayListDrawer.add(ledger);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.d("viewDrawerItems", " failed " + e.getMessage());
        }
    }

    private void viewLedger() {
//        try {
//            getReferencesForViewItemsRecyclerView();
//            initializeSumValue();
//
//            Cursor cursor = db.selectLedgerByName(ledgerId);
//            cursor.moveToFirst();
//        } catch (Exception e) {
//            Log.d("viewLedger", " failed " + e.getMessage());
//        }
    }

    private void addType() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_add_main_type) ;
        View addMainTypeView = layoutInflater.inflate(R.layout.add_type, linearLayout);
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

    private void showExpense(long expense) {
        sum = sum + expense;
        String messageSum = getString(R.string.sum, String.valueOf(sum));
        showValueExpense.setText(messageSum);
    }

    private void showIncome(long income) {
        sumIncome = sumIncome + income;
        String messageSum = getString(R.string.sum, String.valueOf(sumIncome));
        showValueIncome.setText(messageSum);
    }

    private void getReferencesForViewItemsRecyclerView() {
        arrayListExpense = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.view_item_recycle_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(MainActivity.this, arrayListExpense));
    }

    private void addValuesToArrayList(Cursor cursor) {
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < cursor.getCount(); i++) {
                String id = cursor.getString(cursor.getColumnIndex(ID));
                String name = cursor.getString(cursor.getColumnIndex(NAME));

                long valueExpense = cursor.getLong(cursor.getColumnIndex(VALUE));
                showExpense(valueExpense);

                String value = String.valueOf(valueExpense);
                String date = Utility.dateFormat(cursor.getLong(cursor.getColumnIndex(DATE)));
                String type = db.selectTypeById(cursor.getString(cursor.getColumnIndex(TYPE_ID)));

                arrayListExpense.add(new Expense(id, name, value, date, type));

                cursor.moveToNext();
            }
            cursor.close();
        }
        enableSwipeExpense();
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

    private void enableSwipeExpense() {
        RecyclerTouchListener onTouchListener = new RecyclerTouchListener(this, recyclerView);
        onTouchListener
                .setSwipeOptionViews(R.id.edit, R.id.delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.delete) {
                            Expense expense = (Expense) arrayListExpense.get(position);
                            final String idExpense = expense.getId();
                            deleteExpense(idExpense);

                        } else if (viewID == R.id.edit) {
                            Expense expense = (Expense) arrayListExpense.get(position);
                            String id = expense.getId();
                            String name = expense.getTitle();
                            String value = expense.getValue();
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
                                    initializeSumValue();
                                    viewItems();
                                } else if (!db.deleteExpense(idExpense)) {
                                    Utility.failSnackBar(recyclerView, "Error, Expense not deleted, try again", MainActivity.this);
                                }
                            }

                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void updateExpense(final String idExpense, String name, String value) {

//        getReferenceOfExpenseDialog();

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

    private void initializeSumValue() {
        sum = 0;
        sumIncome = 0;
        String expense = getString(R.string.sum, String.valueOf(sum));
        showValueExpense.setText(expense);
        String income = getString(R.string.sum, String.valueOf(sumIncome));
        showValueIncome.setText(income);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_type_menu) {
            addType();
            return true;
        }
        if (id == R.id.configure_drawer) {
            Intent intent = new Intent(MainActivity.this, ConfigureDrawer.class);
            startActivityForResult(intent, CONFIGURE_DRAWER_REQUEST_CODE);
            return true;
        }
        if (id == R.id.filter) {
            Utility.startAnActivityForResult(MainActivity.this, MainActivity.this, Filter.class, FILTER_REQUEST_CODE);
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
                case ADD_LEDGER_REQUEST_CODE:
                    viewDrawerItems();
                    break;
                case ADD_NEW_REQUEST_CODE:
                    viewItems();
                    break;
                case FILTER_REQUEST_CODE:
                    @SuppressWarnings("unchecked")
                    ArrayList<String> typeArrayList = (ArrayList<String>) data.getSerializableExtra("arrayListOfFilter");
                    String date = data.getStringExtra("date");
                    String toDate = data.getStringExtra("toDate");
                    String fromDate = data.getStringExtra("fromDate");

                    try {
                        getReferencesForViewItemsRecyclerView();
                        initializeSumValue();

                        if(date==null && toDate==null && fromDate==null) {
                            String allKeyWord = typeArrayList.get(0);
                            if(typeArrayList.size() == 1 && allKeyWord.equals("all")) {
                                    Cursor cursor = db.selectExpense();
                                    addValuesToArrayList(cursor);
                            } else {
                                for (int j = 0; j<typeArrayList.size(); j++) {
                                    Cursor cursor = db.selectExpenseByType(db.getIdByType(typeArrayList.get(j)));
                                    addValuesToArrayList(cursor);
                                }
                            }
                        }

                        if(toDate==null && fromDate==null && typeArrayList.isEmpty()) {
                            Cursor cursor = db.selectExpenseByDate(date);
                            addValuesToArrayList(cursor);
                        }

                        if(date==null && typeArrayList.isEmpty()) {
                            Cursor cursor = db.selectFromToDate(fromDate, toDate);
                            addValuesToArrayList(cursor);
                        }

                        if(!typeArrayList.isEmpty() && date!=null) {
                            for (int i = 0; i<typeArrayList.size(); i++) {
                                Cursor cursor = db.selectExpenseByTypeAndDate(db.getIdByType(typeArrayList.get(i)), date);
                                addValuesToArrayList(cursor);
                            }
                        }

                        if(!typeArrayList.isEmpty() && toDate!=null && fromDate!=null) {
                            for (int i = 0; i<typeArrayList.size(); i++) {
                                Cursor cursor = db.selectExpenseByTypeAndFromToDate(db.getIdByType(typeArrayList.get(i)), fromDate, toDate);
                                addValuesToArrayList(cursor);
                            }
                        }

                    } catch (Exception e) {
                        Log.d("showItemsByFilter", " failed " + e.getMessage());
                    }

                    break;
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

    private void DrawerClickListener() {
        onTouchListener = new RecyclerTouchListener(this, recyclerViewDrawer);
        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        nameLedger = arrayListDrawer.get(position).getTitle();
                        ledgerId = db.selectIdByLedgerName(nameLedger);
                        initializeSumValue();
                        viewItems();
                        viewLedger();
                        closeDrawer();
                    }
                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        Utility.shortToast(getApplicationContext(), "Button in row " + (position + 1) + " clicked!");
                    }
                });
        recyclerViewDrawer.addOnItemTouchListener(onTouchListener);
    }

    private void drawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
}

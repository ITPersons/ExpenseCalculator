package com.example.zohaibsiddique.expensecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
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
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigureDrawer extends AppCompatActivity {

    DB db;
    private ArrayList<Object> arrayListLedger;
    RecyclerView recyclerView;
    private final int EDIT_LEDGER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        db = new DB(ConfigureDrawer.this);

        viewDrawerItems();
    }

    private void viewDrawerItems() {
        try {
            getReferencesForViewItemsRecyclerView();
            Cursor cursor = db.selectLedger();
            addValuesToArrayListExpense(cursor);
        } catch (Exception e) {
            Log.d("viewDrawerItems", " failed " + e.getMessage());
        }
    }

    private void getReferencesForViewItemsRecyclerView() {
        arrayListLedger = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.configure_drawer_recycle_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new AdapterConfigureDrawer(ConfigureDrawer.this, arrayListLedger));
    }

    private void addValuesToArrayListExpense(Cursor cursor) {
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < cursor.getCount(); i++) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String value = cursor.getString(cursor.getColumnIndex("starting_balance"));
                arrayListLedger.add(new Ledger(id, title, value));
                cursor.moveToNext();
            }
            cursor.close();
        }
        enableSwipeExpense();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Utility.setResultActivity(ConfigureDrawer.this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableSwipeExpense() {
        RecyclerTouchListener onTouchListener = new RecyclerTouchListener(this, recyclerView);
        onTouchListener
                .setSwipeOptionViews(R.id.edit, R.id.delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        Ledger ledger = (Ledger) arrayListLedger.get(position);
                        if (viewID == R.id.delete) {
                            final String id = ledger.getId();
                            delete(id);

                        } else if (viewID == R.id.edit) {
                            SessionManager preference = new SessionManager();
                            preference.setDatePreferences(ConfigureDrawer.this, "edit_ledger", "id", ledger.getId());
                            preference.setDatePreferences(ConfigureDrawer.this, "edit_ledger", "name", ledger.getTitle());
                            preference.setDatePreferences(ConfigureDrawer.this, "edit_ledger", "value", ledger.getValue());
                            Utility.startAnActivityForResult(ConfigureDrawer.this, ConfigureDrawer.this, AddNew.class, EDIT_LEDGER_REQUEST_CODE);
                        }
                    }
                });
        recyclerView.addOnItemTouchListener(onTouchListener);
    }

    private void delete(final String idExpense) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConfigureDrawer.this);
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
                                if (db.deleteLedger(idExpense)) {
                                    Utility.successSnackBar(recyclerView, "Ledger deleted", ConfigureDrawer.this);
                                } else if (!db.deleteExpense(idExpense)) {
                                    Utility.failSnackBar(recyclerView, "Error, Expense not deleted, try again", ConfigureDrawer.this);
                                }
                            }

                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case EDIT_LEDGER_REQUEST_CODE:
                    viewDrawerItems();
                    break;
            }
        }
    }

    public void onBackPressed(){
        Utility.setIntentResultCode(ConfigureDrawer.this);
        finish();
    }
}

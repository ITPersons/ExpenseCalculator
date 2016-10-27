package com.example.zohaibsiddique.expensecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
    private ArrayList<HashMap<String, Object>> arrayList;
    RecyclerView recyclerView;
    View addMainTypeView;
    TextInputLayout layoutAddMainType;
    EditText addMainType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DB(ConfigureDrawer.this);

        viewItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Utility.setIntentResultCode(ConfigureDrawer.this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Show Block list from database
    public void viewItems() {
        try {
            getReferencesForViewItemsRecyclerView();

            Cursor cursor = db.selectMainType();
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

    private void getReferencesForViewItemsRecyclerView() {
        arrayList = new ArrayList<>();
        AdapterConfigureDrawer adapter = new AdapterConfigureDrawer(ConfigureDrawer.this, arrayList);
        recyclerView = (RecyclerView) findViewById(R.id.configure_drawer_recycle_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void addValuesToArrayList(Cursor cursor) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", cursor.getString(cursor.getColumnIndex("id")));
        hm.put("name", cursor.getString(cursor.getColumnIndex("name")));
        arrayList.add(hm);
    }

    private void enableSwipe() {
        RecyclerTouchListener onTouchListener = new RecyclerTouchListener(this, recyclerView);
        onTouchListener
                .setSwipeOptionViews(R.id.edit, R.id.delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.delete) {
                            final String idExpense = arrayList.get(position).get("name").toString();
                            deleteType(idExpense);

                        } else if(viewID == R.id.edit) {
                            String id = arrayList.get(position).get("id").toString();
                            String name = arrayList.get(position).get("name").toString();
                            updateType(id, name);
                        }
                    }
                });
        recyclerView.addOnItemTouchListener(onTouchListener);
    }

    private void deleteType(final String idExpense) {
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
                                if (db.deleteType(idExpense)) {
                                    Utility.successSnackBar(recyclerView, "Expense deleted", ConfigureDrawer.this);
                                    viewItems();
                                } else if (!db.deleteType(idExpense)) {
                                    Utility.failSnackBar(recyclerView, "Error, Expense not deleted, try again", ConfigureDrawer.this);
                                }
                            }

                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void updateType(final String idType, String name) {
        LayoutInflater layoutInflater = LayoutInflater.from(ConfigureDrawer.this);
        addMainTypeView = layoutInflater.inflate(R.layout.add_main_type, null);
        layoutAddMainType = (TextInputLayout) addMainTypeView.findViewById(R.id.text_input_layout_add_main_type);
        addMainType = (EditText) addMainTypeView.findViewById(R.id.add_main_type);
        addMainType.addTextChangedListener(new addNewItemTextWatcher(addMainType));

        addMainType.setText(name);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConfigureDrawer.this);
        alertDialogBuilder.setTitle("Update type");
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
                                String typeName = addMainType.getText().toString().trim();

                                if(db.updateType(Long.valueOf(idType), typeName)) {
                                    Utility.successSnackBar(recyclerView, "updated", ConfigureDrawer.this);
                                    viewItems();
                                } else if(!db.updateType(Long.valueOf(idType), typeName)) {
                                    Utility.failSnackBar(recyclerView, "not updated", ConfigureDrawer.this);
                                }
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void onBackPressed(){
        Utility.setIntentResultCode(ConfigureDrawer.this);
        finish();
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
                case R.id.add_main_type:
                    Utility.validateEditText(addMainType, layoutAddMainType, "Enter valid name");
                    break;
            }

        }
    }
}

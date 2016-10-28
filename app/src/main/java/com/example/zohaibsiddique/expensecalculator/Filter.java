package com.example.zohaibsiddique.expensecalculator;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

public class Filter extends AppCompatActivity implements LeftFragmentFilter.Get,
        TypeFragment.getDataFromTypeFragment, DateFragment.getDateFromDateFragment,
        FromToDateFragment.getFromToDateFromFromToDateFragment, View.OnClickListener{

    DB db;
    ArrayList<String> arrayList;
    String date, fromDate, toDate;
    Button applyButton, clearAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DB(Filter.this);
        arrayList = new ArrayList<>();
        applyButton = (Button) findViewById(R.id.apply_button);
        applyButton.setOnClickListener(this);
        clearAllButton = (Button) findViewById(R.id.clear_all_button);
        clearAllButton.setOnClickListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TypeFragment fragment = new TypeFragment();
        fragmentTransaction.replace(R.id.right_fragment_filter, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.apply_button:
                Utility.shortToast(Filter.this, "apply btn");
                break;
            case R.id.clear_all_button:
                clearAllValues();
                Utility.shortToast(Filter.this, "clear btn");
                break;
        }
    }

    private void clearAllValues() {
        arrayList.clear();
        date = null;
        fromDate = null;
        toDate = null;
    }

    @Override
    public void getData(int s) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(s == 0) {
            TypeFragment fragment = new TypeFragment();
            fragmentTransaction.replace(R.id.right_fragment_filter, fragment);
            fragmentTransaction.commit();
        }
        if(s == 1) {
            DateFragment fragment = new DateFragment();
            fragmentTransaction.replace(R.id.right_fragment_filter, fragment);
            fragmentTransaction.commit();
        }
        if(s == 2) {
            FromToDateFragment fragment = new FromToDateFragment();
            fragmentTransaction.replace(R.id.right_fragment_filter, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void getTypes(ArrayList<String> data) {
        arrayList = data;
    }

    @Override
    public void getDate(String date) {
        this.date = date;
//        Utility.shortToast(Filter.this, date);
    }

    @Override
    public void getFromDate(String fromDate) {
        this.fromDate = fromDate;
//        Utility.shortToast(Filter.this, fromDate);
    }

    @Override
    public void getToDate(String toDate) {
        this.toDate = toDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            deleteFilterStates();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteFilterStates() {
        final String PREFERENCES_FILTER = "filter";
        final String KEY_PREFERENCES = "arrayList";
        final String DATE_KEY_PREFERENCES_DATE = "date_key_date";
        final String MONTH_KEY_PREFERENCES_DATE = "month_key_date";
        final String YEAR_KEY_PREFERENCES_DATE = "year_key_date";
        final String DATE_KEY_PREFERENCES_FROM = "date_key_from";
        final String MONTH_KEY_PREFERENCES_FROM = "month_key_from";
        final String YEAR_KEY_PREFERENCES_FROM = "year_key_from";
        final String DATE_KEY_PREFERENCES_TO = "date_key_to";
        final String MONTH_KEY_PREFERENCES_TO = "month_key_to";
        final String YEAR_KEY_PREFERENCES_TO = "year_key_to";

        SharedPreferences.Editor preferences = getSharedPreferences(PREFERENCES_FILTER, Context.MODE_PRIVATE).edit();
        preferences.remove(KEY_PREFERENCES);

        preferences.remove(DATE_KEY_PREFERENCES_DATE);
        preferences.remove(MONTH_KEY_PREFERENCES_DATE);
        preferences.remove(YEAR_KEY_PREFERENCES_DATE);

        preferences.remove(DATE_KEY_PREFERENCES_FROM);
        preferences.remove(MONTH_KEY_PREFERENCES_FROM);
        preferences.remove(YEAR_KEY_PREFERENCES_FROM);

        preferences.remove(DATE_KEY_PREFERENCES_TO);
        preferences.remove(MONTH_KEY_PREFERENCES_TO);
        preferences.remove(YEAR_KEY_PREFERENCES_TO);

        preferences.apply();
    }

    @Override
    public void onBackPressed() {
        deleteFilterStates();
        finish();
    }


}

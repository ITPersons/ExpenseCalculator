package com.example.zohaibsiddique.expensecalculator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Filter extends AppCompatActivity implements LeftFragmentFilter.Get,
        TypeFragment.getDataFromTypeFragment, DateFragment.getDateFromDateFragment,
        FromToDateFragment.getFromToDateFromFromToDateFragment, View.OnClickListener{

    DB db;
    Button applyButton, clearAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        db = new DB(Filter.this);
        applyButton = (Button) findViewById(R.id.apply_button);
        applyButton.setOnClickListener(this);
        clearAllButton = (Button) findViewById(R.id.clear_all_button);
        clearAllButton.setOnClickListener(this);

        removeSharedPreferences();

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
                clearStates();
                break;
        }
    }

    private void clearStates() {
        Fragment mFragment = getFragmentManager().findFragmentById(R.id.right_fragment_filter);
        if (mFragment instanceof TypeFragment) {
            TypeFragment typeFragment = (TypeFragment) getFragmentManager().findFragmentById(R.id.right_fragment_filter);
            typeFragment.retainStateListView(false);
        }
        if(mFragment instanceof DateFragment) {
            DateFragment dateFragment = (DateFragment) getFragmentManager().findFragmentById(R.id.right_fragment_filter);
            dateFragment.resetState();
        }
        if(mFragment instanceof FromToDateFragment) {
            FromToDateFragment fromToDateFragment = (FromToDateFragment) getFragmentManager().findFragmentById(R.id.right_fragment_filter);
            fromToDateFragment.resetStateOfToDate();
            fromToDateFragment.resetStateOfFromDate();
        }

        LeftFragmentFilter leftFragment = (LeftFragmentFilter) getFragmentManager().findFragmentById(R.id.left_fragment_filter);
        leftFragment.makeStyleBoldAtFirstPosition(false);
        leftFragment.makeStyleBoldAtSecondPosition(false);
        leftFragment.makeStyleBoldAtThirdPosition(false);

        removeSharedPreferences();
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
        LeftFragmentFilter fragment = (LeftFragmentFilter) getFragmentManager().findFragmentById(R.id.left_fragment_filter);
        if(data.isEmpty()) {
            fragment.makeStyleBoldAtFirstPosition(false);
        } else {
            fragment.makeStyleBoldAtFirstPosition(true);
            fragment.countSelection(data.size());
        }
    }

    @Override
    public void getDate(String date) {
        LeftFragmentFilter fragment = (LeftFragmentFilter) getFragmentManager().findFragmentById(R.id.left_fragment_filter);
        if(date.equals("")) {
            fragment.makeStyleBoldAtSecondPosition(false);
        } else {
            fragment.makeStyleBoldAtSecondPosition(true);
        }
    }

    @Override
    public void getFromDate(String fromDate) {
        LeftFragmentFilter fragment = (LeftFragmentFilter) getFragmentManager().findFragmentById(R.id.left_fragment_filter);
        if(fromDate.equals("")) {
            fragment.makeStyleBoldAtThirdPosition(false);
        } else {
            fragment.makeStyleBoldAtThirdPosition(true);
        }
    }

    @Override
    public void getToDate(String toDate) {
        LeftFragmentFilter fragment = (LeftFragmentFilter) getFragmentManager().findFragmentById(R.id.left_fragment_filter);
        if(toDate.equals("")) {
            fragment.makeStyleBoldAtThirdPosition(false);
        } else {
            fragment.makeStyleBoldAtThirdPosition(true);
        }
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
            removeSharedPreferences();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void removeSharedPreferences() {
        final String PREFERENCES_FILTER = "filter";
        final String KEY_PREFERENCES = "arrayList";
        final String KEY_DATE_DATE = "date_key_date";
        final String KEY_MONTH_DATE = "month_key_date";
        final String KEY_YEAR_DATE = "year_key_date";
        final String KEY_DATE_FROM = "date_key_from";
        final String KEY_MONTH_FROM = "month_key_from";
        final String KEY_YEAR_FROM = "year_key_from";
        final String KEY_DATE_TO = "date_key_to";
        final String KEY_MONTH_TO = "month_key_to";
        final String KEY_YEAR_TO = "year_key_to";

        SharedPreferences.Editor preferences = getSharedPreferences(PREFERENCES_FILTER, Context.MODE_PRIVATE).edit();
        preferences.remove(KEY_PREFERENCES);

        preferences.remove(KEY_DATE_DATE);
        preferences.remove(KEY_MONTH_DATE);
        preferences.remove(KEY_YEAR_DATE);

        preferences.remove(KEY_DATE_FROM);
        preferences.remove(KEY_MONTH_FROM);
        preferences.remove(KEY_YEAR_FROM);

        preferences.remove(KEY_DATE_TO);
        preferences.remove(KEY_MONTH_TO);
        preferences.remove(KEY_YEAR_TO);

        preferences.apply();
    }

    @Override
    public void onBackPressed() {
        removeSharedPreferences();
        finish();
    }
}

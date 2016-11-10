package com.example.zohaibsiddique.expensecalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class AddNew extends AppCompatActivity{

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Add Expense"));
        tabLayout.addTab(tabLayout.newTab().setText("Add Ledger"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager2);
        final TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        final String PREFERENCE_LEDGER = "edit_ledger";
        final String PREFERENCE_EXPENSE = "edit_expense";
        final String KEY_ID = "id";
        final String KEY_NAME = "name";
        final String KEY_VALUE = "value";

        SharedPreferences editorExpense;
        editorExpense = getSharedPreferences(PREFERENCE_EXPENSE, Context.MODE_PRIVATE);
        if (editorExpense.contains(KEY_ID) && editorExpense.contains(KEY_NAME) && editorExpense.contains(KEY_VALUE)) {
            if (actionBar != null) {
                actionBar.setTitle("Edit");
            }
        }

        SharedPreferences editor;
        editor = getSharedPreferences(PREFERENCE_LEDGER, Context.MODE_PRIVATE);
        if (editor.contains(KEY_ID) && editor.contains(KEY_NAME)) {
            if (actionBar != null) {
                actionBar.setTitle("Edit");
            }
            viewPager.setCurrentItem(1);

        }

        tabLayout.addOnTabSelectedListener (new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Utility.setResultActivity(AddNew.this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

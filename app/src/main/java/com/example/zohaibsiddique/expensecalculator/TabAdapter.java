package com.example.zohaibsiddique.expensecalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class TabAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    TabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new AddExpenseFragment();

            case 1:
                return new AddLedgerFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

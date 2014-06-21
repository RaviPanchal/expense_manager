package com.arstron.am.Adapter;

import com.arstron.am.Fragment.ExpenseFragment;
import com.arstron.am.Fragment.IncomeFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new IncomeFragment();
		case 1:
			return new ExpenseFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}

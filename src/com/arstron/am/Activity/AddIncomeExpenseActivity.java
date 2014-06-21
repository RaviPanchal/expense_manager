package com.arstron.am.Activity;

import com.arstron.am.Adapter.TabsPagerAdapter;
import com.example.accountmanager.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AddIncomeExpenseActivity extends FragmentActivity implements
		TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter tabAdapter;
	private ActionBar actionBar;
	private Tab[] tab = new Tab[2];
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ie);

		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		tabAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(tabAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		view = new View(this);
		view.setBackgroundColor(Color.GREEN);

		tab[0] = actionBar.newTab().setText("Income")
				.setIcon(R.drawable.income).setTabListener(this);
		tab[1] = actionBar.newTab().setText("Expense")
				.setIcon(R.drawable.expense).setTabListener(this);

		for (int index = 0; index < 2; index++) {
			actionBar.addTab(tab[index]);
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}

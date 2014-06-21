package com.arstron.am.Activity;

import com.example.accountmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	private Button add, history, reminders, settings;
	private TextView accountDetail;
	private String dateFormat, currency, accountName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		dateFormat = intent.getExtras().getString("DateFormat");
		currency = intent.getExtras().getString("Currency");
		accountName = intent.getExtras().getString("AccountName");

		initVars();
		add.setOnClickListener(this);
		history.setOnClickListener(this);
		reminders.setOnClickListener(this);
		settings.setOnClickListener(this);
	}

	private void initVars() {
		accountDetail = (TextView) findViewById(R.id.tvAccountDetail);
		accountDetail.setText("Name : " + accountName + "\nDate Format : "
				+ dateFormat + "\nCurrency : " + currency);
		add = (Button) findViewById(R.id.btnAdd);
		history = (Button) findViewById(R.id.btnHistory);
		reminders = (Button) findViewById(R.id.btnReminder);
		settings = (Button) findViewById(R.id.btnSetting);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.btnAdd:
			Intent intentAdd = new Intent(MainActivity.this,
					AddIncomeExpenseActivity.class);
			startActivity(intentAdd);
			break;
		case R.id.btnHistory:
			Intent intentHistory = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivity(intentHistory);
			break;
		case R.id.btnReminder:
			Intent intentReminders = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivity(intentReminders);
			break;
		case R.id.btnSetting:
			Intent intentSettings = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivity(intentSettings);
			break;
		default:
			break;
		}
	}
}

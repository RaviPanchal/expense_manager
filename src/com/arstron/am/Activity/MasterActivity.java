package com.arstron.am.Activity;

import java.util.ArrayList;
import java.util.List;
import com.example.accountmanager.R;
import com.arstron.am.DAO.AccountDataSource;
import com.arstron.am.DAO.SQLiteHelper;
import com.arstron.am.Shared.AccountInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MasterActivity extends Activity implements OnClickListener {

	private EditText accontName;
	private RadioGroup dateFormat;
	private RadioButton dateSelected;
	private Spinner currency;
	private Button createAccount;
	private AccountDataSource database;
	boolean createMode = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitvity_master);
		initVars();
		createAccount.setOnClickListener(this);
	}

	private void initVars() {
		createAccount = (Button) findViewById(R.id.btnCreateAcc);
		accontName = (EditText) findViewById(R.id.etAccount);
		currency = (Spinner) findViewById(R.id.spCurrency);
		dateFormat = (RadioGroup) findViewById(R.id.rdgDate);
		List<String> currencyList = new ArrayList<String>();
		currencyList.add(SQLiteHelper.RUPEES);
		currencyList.add(SQLiteHelper.DOLLAR);
		currencyList.add(SQLiteHelper.DEHRAM);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, currencyList);
		currency.setAdapter(adapter);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.btnCreateAcc:

			int selectedID = dateFormat.getCheckedRadioButtonId();
			dateSelected = (RadioButton) findViewById(selectedID);

			String accountName = accontName.getText().toString();
			String accountDateFormat = dateSelected.getText().toString();
			String accountCurrency = String.valueOf(currency.getSelectedItem());
			try {
				AccountInfo accountInformation = new AccountInfo();
				accountInformation.setAccName(accountName);
				accountInformation.setDateFormat(accountDateFormat);
				accountInformation.setAccCurrency(accountCurrency);

				database = new AccountDataSource(this);
				database.openWriteMode();

				if (createMode) {
					accountInformation = database
							.createAccountInfo(accountInformation);
					Intent intent = new Intent(MasterActivity.this,
							MainActivity.class);
					intent.putExtra("DateFormat", accountDateFormat);
					intent.putExtra("Currency", accountCurrency);
					intent.putExtra("AccountName", accountName);
					intent.putExtra("name", R.string.app_name + " "
							+ accountName);
					startActivity(intent);
				} else {
					database.updateAccountInfo(accountInformation);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				database.close();
			}
			break;
		default:
			break;
		}
	}
}

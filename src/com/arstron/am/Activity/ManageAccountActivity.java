package com.arstron.am.Activity;

import java.util.List;
import com.example.accountmanager.R;
import com.arstron.am.DAO.AccountDataSource;
import com.arstron.am.Shared.AccountInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ManageAccountActivity extends Activity implements OnClickListener {

	private Spinner spinnerAccount;
	private Button next, create, delete;
	private AccountDataSource database;
	private ArrayAdapter<AccountInfo> adapter;
	private AccountInfo accountInfo;
	private String dateFormat, currency, accountName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		initVars();
		refreshSpinnerData();
		create.setOnClickListener(this);
		next.setOnClickListener(this);
		delete.setOnClickListener(this);
	}

	private void refreshSpinnerData() {
		try {
			database = new AccountDataSource(this);
			database.openReadMode();
			List<AccountInfo> accList = database.getAllAccounts();
			AccountInfo[] accountInfoArr = new AccountInfo[accList.size()];
			accList.toArray(accountInfoArr);
			if (accList != null && accList.size() > 0) {
				adapter = new ArrayAdapter<AccountInfo>(this,
						android.R.layout.simple_spinner_item, accountInfoArr);
				spinnerAccount.setAdapter(adapter);
			} else {
				Toast.makeText(this, "No List Found", Toast.LENGTH_SHORT)
						.show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
	}

	private void initVars() {
		spinnerAccount = (Spinner) findViewById(R.id.spAccount);
		next = (Button) findViewById(R.id.btnContinue);
		create = (Button) findViewById(R.id.btnCreate);
		delete = (Button) findViewById(R.id.btnDelete);
	}

	@Override
	public void onClick(View view) {

		accountInfo = (AccountInfo) spinnerAccount.getSelectedItem();

		switch (view.getId()) {

		// create account
		case R.id.btnCreate:
			Intent intentMaster = new Intent(ManageAccountActivity.this,
					MasterActivity.class);
			startActivity(intentMaster);
			break;

		// continue account
		case R.id.btnContinue:
			if (accountInfo == null) {
				Toast.makeText(this, "Please select account or create account",
						Toast.LENGTH_LONG).show();
				next.setEnabled(false);
			} else {
				next.setEnabled(true);
				setAccountInfo();
				Intent intentMain = new Intent(ManageAccountActivity.this,
						MainActivity.class);
				intentMain.putExtra("DateFormat", dateFormat);
				intentMain.putExtra("Currency", currency);
				intentMain.putExtra("AccountName", accountName);
				startActivity(intentMain);
			}
			break;

		// delete account
		case R.id.btnDelete:
			if (accountInfo != null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ManageAccountActivity.this);
				builder.setTitle("Delete Account");
				builder.setMessage("Do You Want to Delete Account?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											database.openWriteMode();
											database.deleteAccounts(accountInfo);
											adapter.notifyDataSetChanged();
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											database.close();
											refreshSpinnerData();											
											dialog.cancel();
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			} else {
				Toast.makeText(ManageAccountActivity.this,
						"Account does not exists....", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		default:
			break;
		}
	}

	private void setAccountInfo() {

		try {
			accountName = spinnerAccount.getSelectedItem().toString();
			accountInfo = new AccountInfo();
			database = new AccountDataSource(this);
			database.openReadMode();
			accountInfo = database.getAccountByName(accountName);
			dateFormat = accountInfo.getDateFormat().toString();
			currency = accountInfo.getAccCurrency().toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
	}

	@Override
	protected void onResume() {
		refreshSpinnerData();
		super.onResume();
	}
}

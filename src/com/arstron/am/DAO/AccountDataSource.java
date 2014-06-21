package com.arstron.am.DAO;

import java.util.ArrayList;
import java.util.List;

import com.arstron.am.Shared.AccountInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AccountDataSource {

	private SQLiteHelper dbHelper;
	private SQLiteDatabase database;
	public String[] allColumns = { SQLiteHelper.ACCOUNT_ID,
			SQLiteHelper.ACCOUNT_NAME, SQLiteHelper.ACCOUNT_CURRENCY,
			SQLiteHelper.ACCOUNT_DATE_FORMAT };

	public AccountDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void openWriteMode() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void openReadMode() throws SQLException {
		database = dbHelper.getReadableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public AccountInfo createAccountInfo(AccountInfo accountInfo) {

		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.ACCOUNT_NAME, accountInfo.getAccName());
		values.put(SQLiteHelper.ACCOUNT_CURRENCY, accountInfo.getAccCurrency());
		values.put(SQLiteHelper.ACCOUNT_DATE_FORMAT,
				accountInfo.getDateFormat());

		long insertID = database.insert(SQLiteHelper.TABLE_ACCOUNT_MASTER,
				null, values);

		Cursor cursor = database.query(SQLiteHelper.TABLE_ACCOUNT_MASTER,
				allColumns, SQLiteHelper.ACCOUNT_ID + "=" + insertID, null,
				null, null, null);
		cursor.moveToFirst();

		AccountInfo newAccountInfo = cursorToAccountInfo(cursor);
		return newAccountInfo;
	}

	private AccountInfo cursorToAccountInfo(Cursor cursor) {
		AccountInfo newAccountInfo = new AccountInfo();
		newAccountInfo.setId(cursor.getLong(0));
		newAccountInfo.setAccName(cursor.getString(1));
		newAccountInfo.setAccCurrency(cursor.getString(2));
		newAccountInfo.setDateFormat(cursor.getString(3));
		return newAccountInfo;
	}

	public void updateAccountInfo(AccountInfo accountInformation) {

	}

	public List<AccountInfo> getAllAccounts() {
		List<AccountInfo> accountList = new ArrayList<AccountInfo>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_ACCOUNT_MASTER,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AccountInfo newAccounts = cursorToAccountInfo(cursor);
			accountList.add(newAccounts);
			cursor.moveToNext();
		}
		cursor.close();
		return accountList;
	}

	public int deleteAccounts(AccountInfo accountInfo) {
		int deleted = database.delete(SQLiteHelper.TABLE_ACCOUNT_MASTER,
				SQLiteHelper.ACCOUNT_ID + "=" + accountInfo.getId(), null);
		return deleted;
	}

	public AccountInfo getAccountByName(String accountName) {
		AccountInfo accountInfo = new AccountInfo();

		Cursor cursor = database.query(SQLiteHelper.TABLE_ACCOUNT_MASTER,
				allColumns, SQLiteHelper.ACCOUNT_NAME + "='" + accountName
						+ "'", null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			accountInfo = cursorToAccountInfo(cursor);
			cursor.moveToNext();
		}

		cursor.close();
		return accountInfo;
	}
}

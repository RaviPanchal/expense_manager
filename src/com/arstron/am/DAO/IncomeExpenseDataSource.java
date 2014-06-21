package com.arstron.am.DAO;

import java.util.ArrayList;
import java.util.List;

import com.arstron.am.Shared.IncomeExpenseInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;

public class IncomeExpenseDataSource {

	private SQLiteHelper dbHelper;
	private SQLiteDatabase databse;
	public String[] allColumns = { SQLiteHelper.IE_ID, SQLiteHelper.IE_FLAG,
			SQLiteHelper.IE_PRICE, SQLiteHelper.IE_CATEGORY,
			SQLiteHelper.IE_DATE, SQLiteHelper.IE_DESCRIPTION,
			SQLiteHelper.IE_PAYMENTMODE };

	public IncomeExpenseDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public IncomeExpenseDataSource(Fragment fragment) {
		dbHelper = new SQLiteHelper(fragment.getActivity());
	}

	public void openWriteMode() throws SQLException {
		databse = dbHelper.getWritableDatabase();
	}

	public void openReadMode() throws SQLException {
		databse = dbHelper.getReadableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public IncomeExpenseInfo createIncomeExpenseInfo(
			IncomeExpenseInfo incomeExpenseInfo) {

		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.IE_FLAG, incomeExpenseInfo.getFlag());
		values.put(SQLiteHelper.IE_PRICE, incomeExpenseInfo.getPrice());
		values.put(SQLiteHelper.IE_CATEGORY, incomeExpenseInfo.getCategoryId());
		values.put(SQLiteHelper.IE_DATE, incomeExpenseInfo.getDate());
		values.put(SQLiteHelper.IE_DESCRIPTION,
				incomeExpenseInfo.getDescription());
		values.put(SQLiteHelper.IE_PAYMENTMODE,
				incomeExpenseInfo.getPaymentmodeId());

		long insertID = databse.insert(SQLiteHelper.TABLE_INCOME_EXPENSE, null,
				values);

		Cursor cursor = databse.query(SQLiteHelper.TABLE_INCOME_EXPENSE,
				allColumns, SQLiteHelper.IE_ID + "=" + insertID, null, null,
				null, null);

		cursor.moveToFirst();
		IncomeExpenseInfo newIncomeExpenseInfo = cursorToIncomeExpenseInfo(cursor);
		return newIncomeExpenseInfo;
	}

	private IncomeExpenseInfo cursorToIncomeExpenseInfo(Cursor cursor) {
		IncomeExpenseInfo incomeExpenseInfo = new IncomeExpenseInfo();
		incomeExpenseInfo.setId(cursor.getLong(0));
		incomeExpenseInfo.setFlag(cursor.getString(1));
		incomeExpenseInfo.setPrice(cursor.getString(2));
		incomeExpenseInfo.setCategoryId(cursor.getLong(3));
		incomeExpenseInfo.setDate(cursor.getString(4));
		incomeExpenseInfo.setDescription(cursor.getString(5));
		incomeExpenseInfo.setPaymentmodeId(cursor.getLong(6));
		return incomeExpenseInfo;
	}

	public List<IncomeExpenseInfo> getAllIncomeExpenseInfo() {

		List<IncomeExpenseInfo> listIncomeExpenseInfo = new ArrayList<IncomeExpenseInfo>();

		Cursor cursor = databse.query(SQLiteHelper.TABLE_INCOME_EXPENSE,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			IncomeExpenseInfo newIncomeExpenseInfo = cursorToIncomeExpenseInfo(cursor);
			listIncomeExpenseInfo.add(newIncomeExpenseInfo);
			cursor.moveToNext();
		}
		cursor.close();

		return listIncomeExpenseInfo;
	}

	public List<IncomeExpenseInfo> getAmountfromIncomeExpenseInfo(
			StringBuilder currentDate, long catID) {
		List<IncomeExpenseInfo> incomeExpenseInfoList = new ArrayList<IncomeExpenseInfo>();

		Cursor cursor = databse.query(SQLiteHelper.TABLE_INCOME_EXPENSE,
				allColumns, SQLiteHelper.IE_DATE + "='" + currentDate
						+ "' AND " + SQLiteHelper.IE_CATEGORY + "=" + catID,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			IncomeExpenseInfo incomeExpenseInfo = cursorToIncomeExpenseInfo(cursor);
			incomeExpenseInfoList.add(incomeExpenseInfo);
			cursor.moveToNext();
		}
		cursor.close();
		return incomeExpenseInfoList;
	}

	public int deleteIncomeExpenseInfo() {
		int deleted = databse.delete(SQLiteHelper.TABLE_INCOME_EXPENSE,
				SQLiteHelper.IE_FLAG + "=" + "'1'", null);
		return deleted;

	}

}

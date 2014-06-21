package com.arstron.am.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.arstron.am.Shared.PaymentModeInfo;

public class PaymentModeDataSource {
	private SQLiteHelper dbHelper;
	private SQLiteDatabase database;
	public String[] allColumns = { SQLiteHelper.PAY_MODE_ID,
			SQLiteHelper.PAY_MODE_NAME };

	public PaymentModeDataSource(Context context) {
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

	public PaymentModeInfo createPaymentModeInfo(PaymentModeInfo paymentModeInfo) {

		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.PAY_MODE_NAME,
				paymentModeInfo.getPaymentModeName());

		long insertID = database.insert(SQLiteHelper.TABLE_PAYMENTMODE, null,
				values);

		Cursor cursor = database.query(SQLiteHelper.TABLE_PAYMENTMODE,
				allColumns, SQLiteHelper.PAY_MODE_ID + "=" + insertID, null,
				null, null, null);
		cursor.moveToFirst();

		PaymentModeInfo newPaymentModeInfo = cursorToPaymentModeInfo(cursor);
		return newPaymentModeInfo;
	}

	private PaymentModeInfo cursorToPaymentModeInfo(Cursor cursor) {
		PaymentModeInfo newPaymentModeInfo = new PaymentModeInfo();
		newPaymentModeInfo.setId(cursor.getLong(0));
		newPaymentModeInfo.setPaymentModeName(cursor.getString(1));
		return newPaymentModeInfo;
	}

	public void updatePaymentModeInfo(PaymentModeInfo paymentModeInfo) {

	}

	public List<Object> getAllPaymentModes() {
		List<Object> paymentModeList = new ArrayList<Object>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_PAYMENTMODE,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PaymentModeInfo newPaymentMode = cursorToPaymentModeInfo(cursor);
			paymentModeList.add(newPaymentMode);
			cursor.moveToNext();
		}
		cursor.close();
		return paymentModeList;
	}

	public List<PaymentModeInfo> getAllPaymentMode() {
		List<PaymentModeInfo> paymentModeList = new ArrayList<PaymentModeInfo>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_PAYMENTMODE,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PaymentModeInfo newPaymentMode = cursorToPaymentModeInfo(cursor);
			paymentModeList.add(newPaymentMode);
			cursor.moveToNext();
		}
		cursor.close();
		return paymentModeList;
	}

	public int deletePaymentModeInfo(Object paymentModeInfo) {
		int deleted = database.delete(SQLiteHelper.TABLE_PAYMENTMODE,
				SQLiteHelper.PAY_MODE_ID + "="
						+ ((PaymentModeInfo) paymentModeInfo).getId(), null);
		return deleted;
	}

	public PaymentModeInfo getPaymentModeIdByName(String paymentModeName) {
		PaymentModeInfo paymentModeInfo = new PaymentModeInfo();

		Cursor cursor = database.query(SQLiteHelper.TABLE_PAYMENTMODE,
				allColumns, SQLiteHelper.PAY_MODE_NAME + "='" + paymentModeName
						+ "'", null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			paymentModeInfo = cursorToPaymentModeInfo(cursor);
			cursor.moveToNext();
		}
		cursor.close();
		return paymentModeInfo;
	}
}

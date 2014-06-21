package com.arstron.am.DAO;

import java.util.ArrayList;
import java.util.List;
import com.arstron.am.Shared.CategoryInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;

public class CategoryDataSource {

	private SQLiteHelper dbHelper;
	private SQLiteDatabase database;
	public String[] allColumns = { SQLiteHelper.CAT_ID, SQLiteHelper.CAT_NAME };

	public CategoryDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public CategoryDataSource(Fragment fragment) {
		dbHelper = new SQLiteHelper(fragment.getActivity());
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

	public CategoryInfo createCategoryInfo(CategoryInfo categoryInfo) {

		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.CAT_NAME, categoryInfo.getCategoryName());

		long insertID = database.insert(SQLiteHelper.TABLE_CATEGORY, null,
				values);

		Cursor cursor = database.query(SQLiteHelper.TABLE_CATEGORY, allColumns,
				SQLiteHelper.CAT_ID + "=" + insertID, null, null, null, null);
		cursor.moveToFirst();

		CategoryInfo newCategoryInfo = cursorToCategoryInfo(cursor);
		return newCategoryInfo;
	}

	private CategoryInfo cursorToCategoryInfo(Cursor cursor) {
		CategoryInfo newCategoryInfo = new CategoryInfo();
		newCategoryInfo.setId(cursor.getLong(0));
		newCategoryInfo.setCategoryName(cursor.getString(1));
		return newCategoryInfo;
	}

	public int updateCategoryInfo(CategoryInfo categoryInfo) {

		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.CAT_NAME, categoryInfo.getCategoryName());

		int updateID = database.update(SQLiteHelper.TABLE_CATEGORY, values,
				SQLiteHelper.CAT_ID + "=" + categoryInfo.getId(), null);

		// Cursor cursor = database.query(SQLiteHelper.TABLE_CATEGORY,
		// allColumns,
		// SQLiteHelper.CAT_ID + "=" + updateID, null, null, null, null);
		// cursor.moveToPosition((int) updateID);
		//
		// CategoryInfo newCategoryInfo = cursorToCategoryInfo(cursor);
		return updateID;
	}

	public List<Object> getAllCategories() {
		List<Object> categoryList = new ArrayList<Object>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_CATEGORY, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CategoryInfo newCategory = cursorToCategoryInfo(cursor);
			categoryList.add(newCategory);
			cursor.moveToNext();
		}
		cursor.close();
		return categoryList;
	}

	public List<CategoryInfo> getAllCategory() {
		List<CategoryInfo> categoryList = new ArrayList<CategoryInfo>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_CATEGORY, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CategoryInfo newCategory = cursorToCategoryInfo(cursor);
			categoryList.add(newCategory);
			cursor.moveToNext();
		}
		cursor.close();
		return categoryList;
	}

	public List<CategoryInfo> getCategoryId(CategoryInfo categoryInfo) {

		List<CategoryInfo> categoryList = new ArrayList<CategoryInfo>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_CATEGORY, allColumns,
				SQLiteHelper.CAT_NAME + "=" + categoryInfo.getCategoryName(),
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CategoryInfo newCategory = cursorToCategoryInfo(cursor);
			categoryList.add(newCategory);
			cursor.moveToNext();
		}
		cursor.close();
		return categoryList;
	}

	public int deleteCategoryInfo(Object categoryInfo) {
		int deleted = database.delete(
				SQLiteHelper.TABLE_CATEGORY,
				SQLiteHelper.CAT_ID + "="
						+ ((CategoryInfo) categoryInfo).getId(), null);
		return deleted;
	}
}

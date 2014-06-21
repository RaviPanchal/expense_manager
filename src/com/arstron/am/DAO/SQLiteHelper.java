package com.arstron.am.DAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private String DB_PATH = "";
	private SQLiteDatabase database;
	Context context;

	private static final String DATABASE_NAME = "account_manager";
	private static final int DATABASE_VERSION = 1;

	// account master table creation
	public static final String TABLE_ACCOUNT_MASTER = "acc_master";
	public static final String ACCOUNT_ID = "id";
	public static final String ACCOUNT_NAME = "name";
	public static final String ACCOUNT_CURRENCY = "currency";
	public static final String ACCOUNT_DATE_FORMAT = "dateformat";

	public static final String CREATE_TABLE_ACCOUNT_MASTER = "CREATE TABLE "
			+ TABLE_ACCOUNT_MASTER + "(" + ACCOUNT_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NAME + " TEXT, "
			+ ACCOUNT_CURRENCY + " TEXT, " + ACCOUNT_DATE_FORMAT + " TEXT);";

	// currency list
	public static final String RUPEES = "India Rs.";
	public static final String DOLLAR = "US Dollar $";
	public static final String DEHRAM = "UAE";

	// category table creation
	public static final String TABLE_CATEGORY = "category";
	public static final String CAT_ID = "id";
	public static final String CAT_NAME = "category_name";

	public static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
			+ TABLE_CATEGORY + "( " + CAT_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAT_NAME + " TEXT);";

	// payment mode table creation
	public static final String TABLE_PAYMENTMODE = "paymentmode";
	public static final String PAY_MODE_ID = "id";
	public static final String PAY_MODE_NAME = "paymentmode_name";

	public static final String CREATE_TABLE_PAYMENTMODE = "CREATE TABLE "
			+ TABLE_PAYMENTMODE + "( " + PAY_MODE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + PAY_MODE_NAME
			+ " TEXT);";

	// add income/expense table creation
	public static final String TABLE_INCOME_EXPENSE = "income_expense";
	public static final String IE_ID = "id";
	public static final String IE_PRICE = "price";
	public static final String IE_CATEGORY = "category_id";
	public static final String IE_DATE = "ie_date";
	public static final String IE_DESCRIPTION = "description";
	public static final String IE_PAYMENTMODE = "paymentmode_id";
	public static final String IE_PHOTO = "photo";
	public static final String IE_FLAG = "flag";
	public static final String IE_ACCOUNT = "account_id";

	public static final String CREATE_TABLE_INCOME_EXPENSE = "CREATE TABLE "
			+ TABLE_INCOME_EXPENSE + "( " + IE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + IE_FLAG
			+ " TEXT NOT NULL, " + IE_PRICE + " TEXT NOT NULL, " + IE_CATEGORY
			+ " INTEGER REFERENCES " + TABLE_CATEGORY + "(" + CAT_ID
			+ ") ON DELETE CASCADE, " + IE_DATE + " TEXT NOT NULL, "
			+ IE_DESCRIPTION + " TEXT, " + IE_PAYMENTMODE
			+ " INTEGER REFERENCES " + TABLE_PAYMENTMODE + "(" + PAY_MODE_ID
			+ ") ON DELETE CASCADE, " + IE_ACCOUNT + " INTEGER REFERENCES "
			+ TABLE_ACCOUNT_MASTER + "(" + ACCOUNT_ID + ") ON DELETE CASCADE);";

	// manage account table creation
	public static final String TABLE_ACCOUNT_MANAGE = "acc_manage";
	public static final String MANAGE_ID = "id";
	public static final String MANAGE_ACCOUNT_ID = "account_id";
	public static final String MANAGE_IE_ID = "ie_id";

	public static final String CREATE_TABLE_ACCOUNT_MANAGE = "CREATE TABLE "
			+ TABLE_ACCOUNT_MANAGE + "(" + MANAGE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + MANAGE_ACCOUNT_ID
			+ " INTEGER REFERENCES " + TABLE_ACCOUNT_MASTER + "(" + ACCOUNT_ID
			+ ") ON DELETE CASCADE, " + MANAGE_IE_ID + " INTEGER REFERENCES "
			+ TABLE_INCOME_EXPENSE + "(" + IE_ID + ") ON DELETE CASCADE);";

	@SuppressLint("SdCardPath")
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		boolean dbexist = checkdatabase();
		if (dbexist) {
			// System.out.println("Database exists");
			opendatabase();
		} else {
			System.out.println("Database doesn't exist");
			try {
				createdatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createdatabase() throws IOException {
		boolean dbexist = checkdatabase();
		if (dbexist) {

		} else {
			this.getReadableDatabase();
			try {
				copydatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private void copydatabase() throws IOException {
		// Open your local db as the input stream
		InputStream myinput = context.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outfilename = DB_PATH + DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myoutput = new FileOutputStream(outfilename);

		// transfer byte to inputfile to outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myinput.read(buffer)) > 0) {
			myoutput.write(buffer, 0, length);
		}

		// Close the streams
		myoutput.flush();
		myoutput.close();
		myinput.close();
	}

	private void opendatabase() {
		String mypath = DB_PATH + DATABASE_NAME;
		database = SQLiteDatabase.openDatabase(mypath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public synchronized void close() {
		if (database != null) {
			database.close();
		}
		super.close();
	}

	private boolean checkdatabase() {
		boolean checkdb = false;
		try {
			String myPath = DB_PATH + DATABASE_NAME;
			File dbfile = new File(myPath);
			checkdb = dbfile.exists();
		} catch (SQLiteException e) {
			System.out.println("Database doesn't exist");
		}
		return checkdb;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_ACCOUNT_MASTER);
		db.execSQL(CREATE_TABLE_INCOME_EXPENSE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ACCOUNT_MASTER);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_INCOME_EXPENSE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PAYMENTMODE);
		onCreate(db);
	}
}

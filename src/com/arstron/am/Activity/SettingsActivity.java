package com.arstron.am.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.arstron.am.Adapter.ExpandableListAdapter;
import com.arstron.am.DAO.CategoryDataSource;
import com.arstron.am.DAO.PaymentModeDataSource;
import com.example.accountmanager.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class SettingsActivity extends Activity {

	CategoryDataSource categoryDatabase;
	PaymentModeDataSource paymentModeDatabase;
	ExpandableListAdapter listAdapter;
	public ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<Object>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		expListView = (ExpandableListView) findViewById(R.id.exListView);
		PrepareList();
		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);
		expListView.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		listAdapter.setSettingsListActivity(this);
	}

	public void PrepareList() {
		try {
			categoryDatabase = new CategoryDataSource(this);
			paymentModeDatabase = new PaymentModeDataSource(this);
			categoryDatabase.openReadMode();
			paymentModeDatabase.openReadMode();

			listDataHeader = new ArrayList<String>();
			listDataChild = new HashMap<String, List<Object>>();

			listDataHeader.add("Category");
			listDataHeader.add("Payment Mode");

			listDataChild.put(listDataHeader.get(0),
					categoryDatabase.getAllCategories());
			listDataChild.put(listDataHeader.get(1),
					paymentModeDatabase.getAllPaymentModes());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			categoryDatabase.close();
			paymentModeDatabase.close();
		}
	}

	public ExpandableListView getCategoryInfoList() {
		try {
			if (categoryDatabase == null) {
				categoryDatabase = new CategoryDataSource(this);
			}
			categoryDatabase.openReadMode();
			listDataChild.put(listDataHeader.get(0),
					categoryDatabase.getAllCategories());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			categoryDatabase.close();
		}
		return expListView;
	}

	public ExpandableListView getPaymentModeInfoList() {
		try {
			if (paymentModeDatabase == null) {
				paymentModeDatabase = new PaymentModeDataSource(this);
			}
			paymentModeDatabase.openReadMode();
			listDataChild.put(listDataHeader.get(1),
					paymentModeDatabase.getAllPaymentModes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			paymentModeDatabase.close();
		}
		return expListView;
	}

	public ExpandableListView getExpListView() {
		return expListView;
	}

	public void setExpListView(ExpandableListView expListView) {
		this.expListView = expListView;
	}

}

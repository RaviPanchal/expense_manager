package com.arstron.am.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.arstron.am.Adapter.ExpenseExpandableListAdapter;
import com.arstron.am.DAO.CategoryDataSource;
import com.arstron.am.Shared.CategoryInfo;
import com.example.accountmanager.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ExpenseFragment extends android.support.v4.app.Fragment {

	CategoryDataSource categoryDataSource;
	ExpenseExpandableListAdapter listAdapter;
	public ExpandableListView expListView;
	List<CategoryInfo> listDataHeader;
	HashMap<CategoryInfo, List<CategoryInfo>> listDataChild;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.tab_expense, container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		expListView = (ExpandableListView) getView().findViewById(
				R.id.expCategory);
		
		PrepareList();
		listAdapter = new ExpenseExpandableListAdapter(this, listDataHeader,
				listDataChild);
		expListView.setAdapter(listAdapter);
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup) {
					expListView.collapseGroup(previousGroup);
				}
				previousGroup = groupPosition;
			}
		});
	}

	private void PrepareList() {
		try {
			categoryDataSource = new CategoryDataSource(this);
			categoryDataSource.openReadMode();
			
			listDataHeader = categoryDataSource.getAllCategory();
			listDataChild = new HashMap<CategoryInfo, List<CategoryInfo>>();

			for (int index = 0; index < listDataHeader.size(); index++) {
				List<CategoryInfo> list = new ArrayList<CategoryInfo>();
				list.add(listDataHeader.get(index));
				listDataChild.put(listDataHeader.get(index), list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			categoryDataSource.close();
		}
	}
}

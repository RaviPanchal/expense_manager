package com.arstron.am.Adapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.arstron.am.DAO.IncomeExpenseDataSource;
import com.arstron.am.DAO.PaymentModeDataSource;
import com.arstron.am.Fragment.DatePickerFragment;
import com.arstron.am.Fragment.ExpenseFragment;
import com.arstron.am.Shared.CategoryInfo;
import com.arstron.am.Shared.IncomeExpenseInfo;
import com.arstron.am.Shared.PaymentModeInfo;
import com.example.accountmanager.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseExpandableListAdapter extends BaseExpandableListAdapter {

	private List<CategoryInfo> listDataHeader;
	private HashMap<CategoryInfo, List<CategoryInfo>> listDataChild;
	private ExpenseFragment expenseFragment;
	private PaymentModeDataSource paymentModeDataSource;
	private IncomeExpenseDataSource incomeExpenseDataSource;
	private TextView textHeader, textlblDate, textChild, textAmount,
			textPaymentmode, textTotalAmount;
	private ImageButton btnCalender;
	private int year, month, day;
	private long categoryID, paymentModeID;
	private String amount;
	private StringBuilder currentDate;
	private ImageView ivIcon;

	public ExpenseExpandableListAdapter(ExpenseFragment expenseFragment,
			List<CategoryInfo> listDataHeader,
			HashMap<CategoryInfo, List<CategoryInfo>> listDataChild) {
		this.expenseFragment = expenseFragment;
		this.listDataHeader = listDataHeader;
		this.listDataChild = listDataChild;
	}

	@Override
	public int getGroupCount() {
		return this.listDataHeader.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.listDataChild.get(listDataHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.listDataHeader.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.listDataChild.get(this.listDataHeader.get(groupPosition))
				.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		final CategoryInfo headerTitle = (CategoryInfo) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.expenseFragment
					.getActivity().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_parent_expense,
					null);
		}

		textHeader = (TextView) convertView.findViewById(R.id.tvExpenseParent);
		textTotalAmount = (TextView) convertView.findViewById(R.id.tvTotal);
		ivIcon = (ImageView) convertView.findViewById(R.id.imageView1);
		textTotalAmount.setTypeface(null, Typeface.BOLD);
		textHeader.setTypeface(null, Typeface.BOLD);
		textHeader.setText(headerTitle.getCategoryName());
		setCurrentDateOnView();
		refreshAmountOnGruopView(headerTitle.getId());

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		final CategoryInfo childText = (CategoryInfo) getChild(groupPosition,
				childPosition);

		if (convertView == null) {
			LayoutInflater infltInflater = (LayoutInflater) this.expenseFragment
					.getActivity().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			convertView = infltInflater.inflate(R.layout.list_child_expense,
					null);
			holder = new ViewHolder();
			holder.btnAddAmount = (Button) convertView
					.findViewById(R.id.btnSaveAmount);
			holder.editAmount = (EditText) convertView
					.findViewById(R.id.etAmount);
			holder.spinnerPaymentMode = (Spinner) convertView
					.findViewById(R.id.spinnerPaymentMode);
			holder.textshowDate = (TextView) convertView
					.findViewById(R.id.tvDate);
			setCurrentDateOnView();
			holder.textshowDate.setText(currentDate);
			setSpinnerData(holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

			holder.btnAddAmount.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					amount = holder.editAmount.getText().toString();
					getPaymentModeIdfromSelectedItem(holder);
					addExpenseintoDatabase(holder);
					showData();
					holder.editAmount.setText("");
					refreshAmountOnGruopView(childText.toLong());
				}
			});

		}

		// textChild = (TextView) convertView.findViewById(R.id.tvCategory);
		// textChild.setTypeface(null, Typeface.ITALIC);
		// textChild.setText(childText.toString());
		categoryID = childText.toLong();
		setChildView(convertView);

		return convertView;
	}

	protected void refreshAmountOnGruopView(long catID) {
		try {
			incomeExpenseDataSource = new IncomeExpenseDataSource(
					this.expenseFragment.getActivity());
			incomeExpenseDataSource.openReadMode();
			List<IncomeExpenseInfo> incomeExpenseInfo = incomeExpenseDataSource
					.getAmountfromIncomeExpenseInfo(currentDate, catID);
			double total = 0.00;
			for (int index = 0; index < incomeExpenseInfo.size(); index++) {
				total = total
						+ Double.parseDouble(incomeExpenseInfo.get(index)
								.getPrice().toString());
			}
			textTotalAmount.setText(String.format("%.2f", total));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			incomeExpenseDataSource.close();
		}
	}

	protected void getPaymentModeIdfromSelectedItem(ViewHolder holder) {
		try {
			paymentModeDataSource = new PaymentModeDataSource(
					ExpenseExpandableListAdapter.this.expenseFragment
							.getActivity());
			paymentModeDataSource.openReadMode();
			PaymentModeInfo paymentModeInfo = paymentModeDataSource
					.getPaymentModeIdByName(holder.spinnerPaymentMode
							.getSelectedItem().toString());
			paymentModeID = paymentModeInfo.getId();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			paymentModeDataSource.close();
		}
	}

	private void setChildView(View convertView) {
		initVars(convertView);
		addListnerOnButton();
	}

	private void addListnerOnButton() {
		btnCalender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(v);
			}
		});
	}

	protected void showData() {
		try {
			incomeExpenseDataSource = new IncomeExpenseDataSource(
					this.expenseFragment.getActivity());
			incomeExpenseDataSource.openReadMode();

			List<IncomeExpenseInfo> list = incomeExpenseDataSource
					.getAllIncomeExpenseInfo();
			String str = list.get(list.size() - 1).toString();
			Toast.makeText(this.expenseFragment.getActivity(), str,
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			incomeExpenseDataSource.close();
		}
	}

	protected void addExpenseintoDatabase(ViewHolder holder) {
		try {
			incomeExpenseDataSource = new IncomeExpenseDataSource(
					this.expenseFragment.getActivity());
			incomeExpenseDataSource.openWriteMode();

			IncomeExpenseInfo incomeExpenseInfo = new IncomeExpenseInfo();
			incomeExpenseInfo.setCategoryId(categoryID);
			incomeExpenseInfo.setPaymentmodeId(paymentModeID);
			incomeExpenseInfo.setPrice(amount);
			incomeExpenseInfo.setDate(holder.textshowDate.getText().toString());
			incomeExpenseInfo.setFlag("1");
			incomeExpenseInfo.setDescription("Ravi Panchal");

			incomeExpenseInfo = incomeExpenseDataSource
					.createIncomeExpenseInfo(incomeExpenseInfo);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			incomeExpenseDataSource.close();
		}
	}

	protected void showDatePickerDialog(View v) {
		DatePickerFragment datePickerFragment = new DatePickerFragment();
		datePickerFragment.show(this.expenseFragment.getFragmentManager(),
				"DatePicker");
	}

	private void setSpinnerData(ViewHolder holder) {
		try {
			paymentModeDataSource = new PaymentModeDataSource(
					this.expenseFragment.getActivity());
			paymentModeDataSource.openReadMode();

			List<PaymentModeInfo> listPaymentMode = paymentModeDataSource
					.getAllPaymentMode();

			ArrayAdapter<PaymentModeInfo> adapter = new ArrayAdapter<PaymentModeInfo>(
					this.expenseFragment.getActivity(),
					android.R.layout.simple_spinner_item, listPaymentMode);
			holder.spinnerPaymentMode.setAdapter(adapter);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			paymentModeDataSource.close();
		}
	}

	private void initVars(View convertView) {
		btnCalender = (ImageButton) convertView.findViewById(R.id.ibChangeDate);
		textPaymentmode = (TextView) convertView.findViewById(R.id.tvSpinner);
		textPaymentmode.setTypeface(null, Typeface.BOLD);
		textlblDate = (TextView) convertView.findViewById(R.id.lblDate);
		textlblDate.setTypeface(null, Typeface.BOLD);
		textAmount = (TextView) convertView.findViewById(R.id.tvAmount);
		textAmount.setTypeface(null, Typeface.BOLD);
	}

	private void setCurrentDateOnView() {

		final Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		currentDate = new StringBuilder().append(day).append("-")
				.append(month + 1).append("-").append(year);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private class ViewHolder {
		private Button btnAddAmount;
		private EditText editAmount;
		private Spinner spinnerPaymentMode;
		private TextView textshowDate;
	}
}

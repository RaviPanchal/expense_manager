package com.arstron.am.Adapter;

import java.util.HashMap;
import java.util.List;

import com.arstron.am.Activity.SettingsActivity;
import com.arstron.am.DAO.CategoryDataSource;
import com.arstron.am.DAO.PaymentModeDataSource;
import com.arstron.am.Shared.CategoryInfo;
import com.arstron.am.Shared.PaymentModeInfo;
import com.example.accountmanager.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private CategoryDataSource categoryDatabase;
	private PaymentModeDataSource paymentModeDatabase;
	private Context context;
	private List<String> listDataHeader;
	private HashMap<String, List<Object>> listDataChild;
	private SettingsActivity settingsActivity;
	private Dialog dialog;

	public void setSettingsListActivity(SettingsActivity settingsActivity) {
		this.settingsActivity = settingsActivity;
	}

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<Object>> listDataChild) {
		this.context = context;
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

		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_parent, null);
		}

		ImageButton btnAdd = (ImageButton) convertView
				.findViewById(R.id.imageAdd);
		btnAdd.setFocusable(false);
		TextView textListHeader = (TextView) convertView
				.findViewById(R.id.lblParentText);
		textListHeader.setTypeface(null, Typeface.BOLD);
		textListHeader.setText(headerTitle);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog = new Dialog(context);
				dialog.setContentView(R.layout.dialog_add);
				final EditText addText = (EditText) dialog
						.findViewById(R.id.etDialogAdd);

				Button dialogButtonAdd = (Button) dialog
						.findViewById(R.id.btnDialogAdd);
				Button dialogButtonCancel = (Button) dialog
						.findViewById(R.id.btnDialogCancel);

				dialogButtonCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				if (groupPosition == 0) {

					dialog.setTitle("Add Category");

					// if button is clicked, close the custom dialog
					dialogButtonAdd.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								String category = addText.getText().toString();
								CategoryInfo categoryInfo = new CategoryInfo();
								categoryInfo.setCategoryName(category);

								categoryDatabase = new CategoryDataSource(
										context);
								categoryDatabase.openWriteMode();
								categoryInfo = categoryDatabase
										.createCategoryInfo(categoryInfo);
								refreshList(groupPosition);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								categoryDatabase.close();
								dialog.dismiss();
							}
						}
					});
				} else if (groupPosition == 1) {

					dialog.setTitle("Add Payment Mode");

					// if button is clicked, close the custom dialog
					dialogButtonAdd.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								String paymentMode = addText.getText()
										.toString();
								PaymentModeInfo paymentModeInfo = new PaymentModeInfo();
								paymentModeInfo.setPaymentModeName(paymentMode);

								paymentModeDatabase = new PaymentModeDataSource(
										context);
								paymentModeDatabase.openWriteMode();
								paymentModeInfo = paymentModeDatabase
										.createPaymentModeInfo(paymentModeInfo);

								refreshList(groupPosition);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								paymentModeDatabase.close();
								dialog.dismiss();
							}
						}
					});
				}
				dialog.show();
			}
		});

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final Object childText = (Object) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infltInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infltInflater.inflate(R.layout.list_child, null);
		}

		ImageButton editItem = (ImageButton) convertView
				.findViewById(R.id.imageEdit);
		ImageButton deleteItem = (ImageButton) convertView
				.findViewById(R.id.imageDelete);
		TextView textListChild = (TextView) convertView
				.findViewById(R.id.lblChildText);
		textListChild.setText(childText.toString());

		editItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog = new Dialog(context);
				dialog.setContentView(R.layout.dialog_add);
				final EditText addText = (EditText) dialog
						.findViewById(R.id.etDialogAdd);

				addText.setText(childText.toString());

				Button dialogButtonSave = (Button) dialog
						.findViewById(R.id.btnDialogAdd);
				dialogButtonSave.setText("Save");
				Button dialogButtonCancel = (Button) dialog
						.findViewById(R.id.btnDialogCancel);

				dialogButtonCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				if (groupPosition == 0) {

					dialog.setTitle("Update Category");

					// if button is clicked, close the custom dialog
					dialogButtonSave.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								String category = addText.getText().toString();
								CategoryInfo categoryInfo = new CategoryInfo();
								categoryInfo.setCategoryName(category);

								categoryDatabase = new CategoryDataSource(
										context);
								categoryDatabase.openWriteMode();
								int update = categoryDatabase
										.updateCategoryInfo(categoryInfo);
								notifyDataSetChanged();
								if (update > 0)
									refreshList(groupPosition);

							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								categoryDatabase.close();
								dialog.dismiss();
							}
						}
					});
				} else if (groupPosition == 1) {

					dialog.setTitle("Update Payment Mode");

					// if button is clicked, close the custom dialog
					dialogButtonSave.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								String paymentMode = addText.getText()
										.toString();
								PaymentModeInfo paymentModeInfo = new PaymentModeInfo();
								paymentModeInfo.setPaymentModeName(paymentMode);

								paymentModeDatabase = new PaymentModeDataSource(
										context);
								paymentModeDatabase.openWriteMode();
								paymentModeDatabase
										.updatePaymentModeInfo(paymentModeInfo);
								refreshList(groupPosition);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								paymentModeDatabase.close();
								dialog.dismiss();
							}
						}
					});
				}
				dialog.show();
			}
		});

		deleteItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(R.string.confirm);
				builder.setMessage(R.string.sureDelete)
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										if (groupPosition == 0) {
											categoryDatabase = new CategoryDataSource(
													context);
											try {
												categoryDatabase
														.openWriteMode();
												categoryDatabase
														.deleteCategoryInfo(childText);
												dialog.cancel();
												listDataChild.remove(childText);
												refreshList(groupPosition);
											} catch (Exception e) {
												e.printStackTrace();
											} finally {
												categoryDatabase.close();
											}
										} else if (groupPosition == 1) {
											paymentModeDatabase = new PaymentModeDataSource(
													context);
											try {
												paymentModeDatabase
														.openWriteMode();
												paymentModeDatabase
														.deletePaymentModeInfo(childText);
												dialog.cancel();
												listDataChild.remove(childText);
												refreshList(groupPosition);
											} catch (Exception e) {
												e.printStackTrace();
											} finally {
												paymentModeDatabase.close();
											}
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});
		return convertView;
	}

	public void refreshList(int groupPosition) {
		settingsActivity.getCategoryInfoList().setAdapter(
				ExpandableListAdapter.this);
		settingsActivity.getPaymentModeInfoList().setAdapter(
				ExpandableListAdapter.this);
		settingsActivity.getExpListView().expandGroup(groupPosition);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}

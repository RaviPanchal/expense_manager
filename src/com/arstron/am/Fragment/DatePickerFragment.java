package com.arstron.am.Fragment;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.accountmanager.R;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements
		OnDateSetListener {

	private int year, month, day;
	private TextView resultDate;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		setDateOnDatePickerDialog();
		DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
				this, year, month, day);
		datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == DialogInterface.BUTTON_NEGATIVE) {
							dialog.cancel();
							setDateOnDatePickerDialog();
							resultDate.setText(new StringBuilder().append(day)
									.append("-").append(month + 1).append("-")
									.append(year));
						}
					}
				});
		datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
		return datePickerDialog;
	}

	private void setDateOnDatePickerDialog() {
		resultDate = (TextView) getActivity().findViewById(R.id.tvDate);
		String strDate = resultDate.getText().toString();
		Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");
		Matcher matcher = pattern.matcher(strDate);
		if (matcher.find()) {
			day = Integer.parseInt(matcher.group(1));
			month = Integer.parseInt(matcher.group(2)) - 1;
			year = Integer.parseInt(matcher.group(3));
		}
	}

	@Override
	public void onDateSet(DatePicker view, int selectedYear, int monthOfYear,
			int dayOfMonth) {
		resultDate = (TextView) getActivity().findViewById(R.id.tvDate);
		resultDate.setText(new StringBuilder().append(dayOfMonth).append("-")
				.append(monthOfYear + 1).append("-").append(selectedYear));
		year = selectedYear;
		month = monthOfYear;
		day = dayOfMonth;
	}
}

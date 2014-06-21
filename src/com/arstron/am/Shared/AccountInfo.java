package com.arstron.am.Shared;

public class AccountInfo {

	private long id = 0;
	private String accName = "Select Account", accCurrency = "", dateFormat = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccCurrency() {
		return accCurrency;
	}

	public void setAccCurrency(String accCurrency) {
		this.accCurrency = accCurrency;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String toString() {
		return accName;
	}
}

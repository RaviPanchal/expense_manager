package com.arstron.am.Shared;

public class IncomeExpenseInfo {

	private long id, categoryId, paymentmodeId;
	private String description, price, date, flag;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public long getPaymentmodeId() {
		return paymentmodeId;
	}

	public void setPaymentmodeId(long paymentmodeId) {
		this.paymentmodeId = paymentmodeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return price + " " + paymentmodeId;
	}

}

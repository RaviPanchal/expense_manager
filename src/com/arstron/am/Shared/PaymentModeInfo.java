package com.arstron.am.Shared;

public class PaymentModeInfo {

	long id;
	String paymentModeName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPaymentModeName() {
		return paymentModeName;
	}

	public void setPaymentModeName(String paymentModeName) {
		this.paymentModeName = paymentModeName;
	}

	@Override
	public String toString() {
		return paymentModeName;
	}

	public Long toLong() {
		return id;
	}
}

package com.lendico.plangenerator.model;

import java.math.BigDecimal;

public class Repayment {
	
	BigDecimal borrowerPaymentAmount;
	String date;
	BigDecimal initialOutstandingPrincipal;
	BigDecimal interest;
	BigDecimal principal;
	BigDecimal  remainingOutstandingPrincipal;
	
	
	
	public Repayment(BigDecimal borrowerPaymentAmount, BigDecimal initialOutstandingPrincipal, BigDecimal interest,
			BigDecimal principal, BigDecimal remainingOutstandingPrincipal, String date) {
		super();
		this.borrowerPaymentAmount = borrowerPaymentAmount;
		this.initialOutstandingPrincipal = initialOutstandingPrincipal;
		this.interest = interest;
		this.principal = principal;
		this.remainingOutstandingPrincipal = remainingOutstandingPrincipal;
		this.date = date;
	}



	public BigDecimal getBorrowerPaymentAmount() {
		return borrowerPaymentAmount;
	}



	public void setBorrowerPaymentAmount(BigDecimal borrowerPaymentAmount) {
		this.borrowerPaymentAmount = borrowerPaymentAmount;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public BigDecimal getInitialOutstandingPrincipal() {
		return initialOutstandingPrincipal;
	}



	public void setInitialOutstandingPrincipal(BigDecimal initialOutstandingPrincipal) {
		this.initialOutstandingPrincipal = initialOutstandingPrincipal;
	}



	public BigDecimal getInterest() {
		return interest;
	}



	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}



	public BigDecimal getPrincipal() {
		return principal;
	}



	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}



	public BigDecimal getRemainingOutstandingPrincipal() {
		return remainingOutstandingPrincipal;
	}



	public void setRemainingOutstandingPrincipal(BigDecimal remainingOutstandingPrincipal) {
		this.remainingOutstandingPrincipal = remainingOutstandingPrincipal;
	}


	
}

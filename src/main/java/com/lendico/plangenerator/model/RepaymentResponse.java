package com.lendico.plangenerator.model;

import java.util.List;

public class RepaymentResponse {
	
	List<Repayment> repayments;
	
	public RepaymentResponse() {
	}

	public RepaymentResponse(List<Repayment> repayments) {
		super();
		this.repayments = repayments;
	}

	public List<Repayment> getRepayments() {
		return repayments;
	}

	public void setRepayments(List<Repayment> repayments) {
		this.repayments = repayments;
	}
	
	

}

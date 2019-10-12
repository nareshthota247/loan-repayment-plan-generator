package com.lendico.plangenerator.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class LoanInfoRequest {

	private BigDecimal loanAmount;
	private BigDecimal nominalRate;
	private Integer duration;
	private String startDate;

	public LoanInfoRequest() {
	}

	public LoanInfoRequest(@NotNull String loanAmount, @NotNull String nominalRate, @NotNull Integer duration,
			@NotNull String startDate) {
		super();
		if (!loanAmount.isEmpty())
			this.loanAmount = new BigDecimal(loanAmount);
		if (!nominalRate.isEmpty())
			this.nominalRate = new BigDecimal(nominalRate);
		if (duration != ' ')
			this.duration = duration;
		if (!startDate.isEmpty())
			this.startDate = startDate;
	}
	
	public LoanInfoRequest(@NotNull String loanAmount, @NotNull String nominalRate, @NotNull String startDate) {
		super();
		if (!loanAmount.isEmpty())
			this.loanAmount = new BigDecimal(loanAmount);
		if (!nominalRate.isEmpty())
			this.nominalRate = new BigDecimal(nominalRate);
		if (!startDate.isEmpty())
			this.startDate = startDate;
	}

	@NotNull(message = "Loan Amount may not be null")
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	@NotNull(message = "Nominal Rate may not be null")
	public BigDecimal getNominalRate() {
		return nominalRate;
	}

	public void setNominalRate(BigDecimal nominalRate) {
		this.nominalRate = nominalRate;
	}

	@NotNull(message = "Duration may not be null")
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@NotNull(message = "Start Date may not be null")
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

}

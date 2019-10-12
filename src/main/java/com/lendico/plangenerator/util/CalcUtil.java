package com.lendico.plangenerator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CalcUtil {

	private static final Logger logger = LoggerFactory.getLogger(CalcUtil.class);
	
	private CalcUtil() {
	}

	public static BigDecimal getAnnuityPayment(BigDecimal loanAmount, BigDecimal rate, int time) {
		BigDecimal powNum = (new BigDecimal(1).add(rate)).pow(time);
		BigDecimal annuityPayment = (loanAmount.multiply(rate).multiply(powNum)).divide((powNum.subtract(new BigDecimal(1))), 2,
				RoundingMode.HALF_UP);
		logger.debug("getAnnuityPayment {} ",annuityPayment);
		return annuityPayment;
	}

	public static BigDecimal getMonthlyInterest(BigDecimal initialOutstandingPrincipal, BigDecimal normalRate) {
		BigDecimal monthlyInterest = initialOutstandingPrincipal.multiply(Constants.DAYS_IN_MONTH).multiply(normalRate)
				.divide(Constants.DAYS_IN_YEAR, 2, RoundingMode.HALF_UP);
		logger.debug("getMonthlyInterest {}",monthlyInterest);
		return monthlyInterest;
	}

	public static Date getNextRepaymentDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		date = cal.getTime();
		logger.debug("getNextRepaymentDate {} ",date);
		return date;
	}

}

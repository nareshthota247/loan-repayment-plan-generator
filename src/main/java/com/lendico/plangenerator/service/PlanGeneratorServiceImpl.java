package com.lendico.plangenerator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lendico.plangenerator.exception.ApplicationParseException;
import com.lendico.plangenerator.model.LoanInfoRequest;
import com.lendico.plangenerator.model.Repayment;
import com.lendico.plangenerator.util.CalcUtil;

@Service
public class PlanGeneratorServiceImpl implements PlanGeneratorService {

	private static final Logger logger = LoggerFactory.getLogger(PlanGeneratorServiceImpl.class);

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Override
	public List<Repayment> planGenerator(LoanInfoRequest loanInfoRequest) {

		List<Repayment> list = new ArrayList<>();
		BigDecimal interest;
		BigDecimal emiPriciple;
		BigDecimal borrowerPaymentAmount;
		BigDecimal remainingOutstandingPrincipal;
		BigDecimal monthlyRateOfInterest = loanInfoRequest.getNominalRate().divide(new BigDecimal(1200), 8,
				RoundingMode.HALF_UP);
		logger.debug("monthlyRateOfInterest {} ", monthlyRateOfInterest);
		BigDecimal rateOfInterestPercentage = loanInfoRequest.getNominalRate().divide(new BigDecimal(100));
		logger.debug("rateOfInterestPercentage {} ", rateOfInterestPercentage);
		BigDecimal initialOutstandingPrincipal = loanInfoRequest.getLoanAmount();
		Date startDate = parseStartDate(loanInfoRequest.getStartDate());
		borrowerPaymentAmount = getAnnuityPayment(initialOutstandingPrincipal, monthlyRateOfInterest,
				loanInfoRequest.getDuration());
		logger.debug("borrowerPaymentAmount {} ", borrowerPaymentAmount);
		for (int i = 1; i <= loanInfoRequest.getDuration(); i++) {
			interest = CalcUtil.getMonthlyInterest(initialOutstandingPrincipal, rateOfInterestPercentage);
			if (borrowerPaymentAmount.compareTo(initialOutstandingPrincipal) < 0) {
				emiPriciple = borrowerPaymentAmount.subtract(interest);
			} else {
				borrowerPaymentAmount = initialOutstandingPrincipal.add(interest);
				emiPriciple = initialOutstandingPrincipal;
				logger.debug("borrowerPaymentAmount modified{} ", borrowerPaymentAmount);
			}
			logger.debug("emiPriciple {} ", emiPriciple);
			remainingOutstandingPrincipal = initialOutstandingPrincipal.subtract(emiPriciple);
			Repayment repaymentPay = new Repayment(borrowerPaymentAmount, initialOutstandingPrincipal, interest,
					emiPriciple, remainingOutstandingPrincipal, dateFormat.format(startDate));
			list.add(repaymentPay);

			if (remainingOutstandingPrincipal.compareTo(new BigDecimal(0)) > 0) {
				initialOutstandingPrincipal = remainingOutstandingPrincipal;
			}
			startDate = getNextRepaymentDate(startDate);
		}
		return list;

	}

	@Override
	public Date getNextRepaymentDate(Date startDate) {
		return CalcUtil.getNextRepaymentDate(startDate);
	}

	@Override
	public BigDecimal getAnnuityPayment(BigDecimal loanAmount, BigDecimal rate, int time) {
		return CalcUtil.getAnnuityPayment(loanAmount, rate, time);
	}

	@Override
	public BigDecimal getMonthlyInterest(BigDecimal initialOutstandingPrincipal, BigDecimal normalRate) {
		return CalcUtil.getMonthlyInterest(initialOutstandingPrincipal, normalRate);
	}
	
	private Date parseStartDate(String startDateString) {
		Date startDate = null;
		try {
			startDate = dateFormat.parse(startDateString);
		} catch (ParseException e) {
			logger.error("Exception in Parseing date :: {} ", startDateString);
			throw new ApplicationParseException(e.getMessage());
		}
		return startDate;
	}

}

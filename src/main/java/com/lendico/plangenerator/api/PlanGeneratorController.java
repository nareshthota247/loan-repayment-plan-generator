package com.lendico.plangenerator.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lendico.plangenerator.exception.ApplicationParseException;
import com.lendico.plangenerator.exception.ErrorResponse;
import com.lendico.plangenerator.exception.PlanGeneratorException;
import com.lendico.plangenerator.model.LoanInfoRequest;
import com.lendico.plangenerator.model.Repayment;
import com.lendico.plangenerator.model.RepaymentResponse;
import com.lendico.plangenerator.service.PlanGeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(value="Generate Loan Repayment Plan")
public class PlanGeneratorController {

	private static final Logger logger = LoggerFactory.getLogger(PlanGeneratorController.class);

	@Autowired
	PlanGeneratorService planGeneratorService;

	@ApiOperation(value = "Rest endpoint to generate Loan Repayment Plan", response = RepaymentResponse.class)
	@PostMapping(value = "/generateRepaymentPlan", consumes = { "application/json" }, produces = { "application/json" })
	public RepaymentResponse planGenerator(@Valid @RequestBody LoanInfoRequest loaninfo) {
		List<Repayment> list = null;
		try {
			list = planGeneratorService.planGenerator(loaninfo);
		} catch (ApplicationParseException e) {
			throw e;
		} catch (Exception e) {
			throw new PlanGeneratorException(e.getMessage());
		}
		return new RepaymentResponse(list);
	}

	@ExceptionHandler(ApplicationParseException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(ApplicationParseException ex) {
		logger.error("Exception ApplicationParseException :: {} ", ex.getMessage());
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(Integer.valueOf(1001));
		error.setErrorDesc(ex.getMessage());
		error.setTimestamp(new Date());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		logger.error("Exception MethodArgumentNotValidException :: {} ", ex.getMessage());
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}

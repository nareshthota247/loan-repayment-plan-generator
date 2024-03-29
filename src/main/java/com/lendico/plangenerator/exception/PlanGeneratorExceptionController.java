package com.lendico.plangenerator.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class PlanGeneratorExceptionController {
	
	 @ExceptionHandler(PlanGeneratorException.class)
	  public final ResponseEntity<ErrorResponse> handleUserNotFoundException(PlanGeneratorException ex, WebRequest request) {
	    ErrorResponse errorDetails = new ErrorResponse(new Date(), Integer.valueOf(1000),request.getDescription(false));
	    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	  }

}

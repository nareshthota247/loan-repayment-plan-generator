package com.lendico.plangenerator.exception;

public class ApplicationParseException extends RuntimeException {
	
	private static final long serialVersionUID = -4278057746617473364L;
	
	public ApplicationParseException() {
		super();
	}
	public ApplicationParseException(String errorDescription) {
		super(errorDescription);
	}

}

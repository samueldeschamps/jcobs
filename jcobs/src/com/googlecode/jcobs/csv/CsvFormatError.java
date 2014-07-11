package com.googlecode.jcobs.csv;

public class CsvFormatError extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CsvFormatError(String message) {
		super(message);
	}

	public CsvFormatError(Exception cause) {
		super(cause);
	}

}

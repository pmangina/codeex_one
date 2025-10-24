package com.codeex.salarycheck.input.file.exception;

import java.io.IOException;

import com.opencsv.exceptions.CsvValidationException;


/**
 *  ScFileReaderException
 *  
 *  	- Custom Exception that is thrown during file read. All exceptions are converted to this exception in CsvBeanFileReader. 
 */
public class ScFileReaderException extends Exception {

	private static final long serialVersionUID = 5248233141654487257L;

	public ScFileReaderException(CsvValidationException ex) {
		super(ex);
	}

	public ScFileReaderException(IOException ex) {
		super(ex);
	}
	
	public ScFileReaderException(IllegalStateException ex) {
		super(ex);
	}

	public ScFileReaderException(String msg) {
		super(msg);
	}

}

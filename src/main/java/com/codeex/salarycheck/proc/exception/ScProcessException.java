package com.codeex.salarycheck.proc.exception;

import java.io.IOException;

import com.codeex.salarycheck.input.file.exception.ScFileReaderException;

public class ScProcessException extends Exception {

	public ScProcessException(IOException ex) {
		super(ex);
	}

	public ScProcessException(NumberFormatException ex) {
		super(ex);
	}

	public ScProcessException(ScFileReaderException ex) {
		super(ex);
	}

	public ScProcessException(String msg) {
		super(msg);
	}

}

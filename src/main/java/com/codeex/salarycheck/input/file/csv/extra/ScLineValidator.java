package com.codeex.salarycheck.input.file.csv.extra;

import java.util.regex.Pattern;

import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.LineValidator;


/**
 *  ScLineValidator
 *  
 *  	- Valiate a line in data file
 */
public class ScLineValidator implements LineValidator {

	private static final String SPECIAL_CHARS = "[!@#$%&*()_+=|<>?{}\\[\\]~-]";
    Pattern special = Pattern.compile (SPECIAL_CHARS);
    private final String MESSAGE;

    public ScLineValidator() {
        this.MESSAGE = "Line should not contain " + SPECIAL_CHARS;
    }

    @Override
    public boolean isValid(String line) {
        
    	if (line == null) {
            return true;
        }
        
        // match for special chars
        return !special.matcher(line).find();
    }

    @Override
    public void validate(String line) throws CsvValidationException {
        if (!isValid(line)) {
            throw new CsvValidationException(MESSAGE);
        }
    }

    String getMessage() {
        return MESSAGE;
    }
}

package com.codeex.salarycheck.input.file.csv.extra;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.codeex.salarycheck.input.file.exception.ScFileReaderException;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.slf4j.Slf4j;


/**
 *  CSVFileReader
 *  
 *   	- This Csv File Reader has ability to a) validate input data rows like, number of tokens, invalid special characters, data type checks.
 *   		It also has option to read line by line that would be useful for large data files, instead of reading the whole file at once.
 *   		Reading large input data file at once can cause JVM memory issues.
 */
@Component
@Slf4j
public class CSVFileReader {

	CSVReader csvReader;
    int rowCounter = 0;
    private static final int MIN_TOKENS = 4;
    private static final int MAX_TOKENS = 5;
    /**
     * 	Opens file and if successful, keeps the csvReader variable for further calls to read the file
     * 
     * @param filePath - absolute path of the input file
     * @return boolean - if open file is success return true, else false
     * @throws FileNotFoundException
     */
	public boolean openFile(String filePath) throws FileNotFoundException {
		csvReader = new CSVReaderBuilder(new FileReader(filePath))
				 .withCSVParser(new CSVParserBuilder()
	                        .withSeparator(',')
	                        .build())
                    .withLineValidator(new ScLineValidator())
	                .build();
		
		// initialize
		rowCounter = 0;
		return true;
	}
	
	/**
	 * 	Read a line and parse it into String tokens or null if there is no more input.
	 * 
	 * @return String tokens
	 * @throws ScFileReaderException
	 */
	public String[] readNext() throws ScFileReaderException {
		String [] rowAsTokens;
		
	      try {
	    	  
	    	// row counter
    	    rowCounter++;
			
    	    // read line as tokens
    	    rowAsTokens = csvReader.readNext();

    	    // verify number of tokens
    	    if (rowAsTokens != null) {
				if (!(rowAsTokens.length >= MIN_TOKENS && rowAsTokens.length <= MAX_TOKENS)) {
					throw new ScFileReaderException(String.format("Number of tokens should be either %d or %d. Found tokens: %d at Row# %d", MIN_TOKENS, MAX_TOKENS, rowAsTokens.length, rowCounter));
				}
    	    }

    	    // debug print
    	    if (log.isDebugEnabled()) {
	    	    if (rowAsTokens != null) {
					log.debug("Number of Tokens = " + rowAsTokens.length + " Row# " + rowCounter);
					if (rowAsTokens != null) {
					      for (String token : rowAsTokens) {
					        System.out.print(token + " * ");
					      }
					      System.out.println();
					}
				}
    	    }
    	    
			return rowAsTokens;
			
		} catch (CsvValidationException e) {
			e.printStackTrace();
			throw new ScFileReaderException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ScFileReaderException(e);
		}
	}
}

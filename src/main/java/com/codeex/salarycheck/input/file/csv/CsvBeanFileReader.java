package com.codeex.salarycheck.input.file.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.codeex.salarycheck.input.file.ScFileReader;
import com.codeex.salarycheck.input.file.csv.model.InputData;
import com.codeex.salarycheck.input.file.exception.ScFileReaderException;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.extern.slf4j.Slf4j;


/**
 * CsvBeanFileReader
 * 
 * 		- Reads CSV file and returns a list of InputData type beans.
 */
@Slf4j
@Component
public class CsvBeanFileReader implements ScFileReader {
	
	// Number of lines to be skipped at the beginning of the file. ie., headers
	private static final int SKIP_LINES = 1;
	
	/**
	 *	readFile
	 *		- Logic:
	 *				The method uses OpenCSV framework to read input rows as beans. We supply necessary configurations to the builder.
	 *
	 * @param filePath - absolute path of the input csv file
     * @return list of InputData objects
     * @throws ScFileReaderException
	 */
	@Override
	public List<InputData> readFile(String filePath) throws ScFileReaderException {
		
		// empty/null check
		if (filePath==null) {
			throw new ScFileReaderException("filePath is null.");
		} else if (filePath.isEmpty()) {
			throw new ScFileReaderException("filePath is empty.");
		}
		
		try (FileReader fileReader = new FileReader(filePath)) {
			List<InputData> beans = new CsvToBeanBuilder<InputData>(fileReader)
					.withType(InputData.class)
					.withSkipLines(SKIP_LINES)
					.build()
					.parse();
			
			// debug print
			log.debug(String.format("Beans from Input File (%s) :", filePath));
			
			// debug print
			for (InputData bean : beans) {
				log.debug(bean.toString());
			}
			
			log.info("Input file read. returning the list of input data elements.");
			
			return beans;
			
		} catch (IllegalStateException e) {
			log.error(String.format("IllegalStateException thrown. %s. Cause: %s.", e.getMessage(), e.getCause()));
			throw new ScFileReaderException(e);
		} catch (FileNotFoundException e) {
			log.error(String.format("FileNotFoundException thrown. %s. Cause: %s.", e.getMessage(), e.getCause()));
			throw new ScFileReaderException(e);
		} catch (IOException e) {
			log.error(String.format("IOException thrown. %s. Cause: %s.", e.getMessage(), e.getCause()));
			throw new ScFileReaderException(e);
		}
	}
}

package com.codeex.salarycheck.input.file;

import java.util.List;

import com.codeex.salarycheck.input.file.csv.model.InputData;
import com.codeex.salarycheck.input.file.exception.ScFileReaderException;


/**
 *  ScFileReader
 *  
 *  	- Interface for Component that will read input data files
 */
public interface ScFileReader {

	/**
	 *  Read File
	 *  		- Read input file rows and return list of beans
	 *  
	 * @param filePath
	 * @return List of Input data beans
	 * @throws ScFileReaderException
	 */
	List<InputData> readFile(String filePath) throws ScFileReaderException;
}

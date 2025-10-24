package com.codeex.salarycheck.proc;

import com.codeex.salarycheck.proc.exception.ScProcessException;

/**
 *  Processor Interface
 *  
 *  	- Process Input data file and print 
 *  			1) Salary corrections.
 *  					- Criteria1: should be 20% more than average subordinate salary
 *  					- Criteria2: should be 50% less than average subordinate salary
 *  			2) Too long Reporting lines.
 *  					- levels that have more than 4 managers from the ceo
 */
public interface Processor {

	/**
	 * Process logic
	 * 	
	 * @param filePath
	 * @return true if successful, false on failure
	 * @throws ScProcessException
	 */
	boolean process(String filePath) throws ScProcessException;
}

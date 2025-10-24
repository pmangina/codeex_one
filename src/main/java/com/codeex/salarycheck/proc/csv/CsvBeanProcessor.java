package com.codeex.salarycheck.proc.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalDouble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeex.salarycheck.input.file.csv.CsvBeanFileReader;
import com.codeex.salarycheck.input.file.csv.model.InputData;
import com.codeex.salarycheck.input.file.exception.ScFileReaderException;
import com.codeex.salarycheck.model.EmpNode;
import com.codeex.salarycheck.proc.Processor;
import com.codeex.salarycheck.proc.exception.ScProcessException;

import lombok.extern.slf4j.Slf4j;

/**
 *  CsvBeanProcessor
 *  
 *  	- Processes CSV Input data files. 
 *  	- Prints a) Salary corrections, b) Long reporting lines
 */
@Slf4j
@Service
public class CsvBeanProcessor implements Processor {

	@Autowired
	private CsvBeanFileReader csvBeanFileReader;
		
	// Organization data nodes - all employees are stored in this DS
	private HashMap<Long, EmpNode> orgNode = new HashMap<>();
	
	// Average salaries at each reporting level
	private List<Double> avgSalaries = new ArrayList<>();
	
	// Max reporting level from input data
	private int maxLevel = 0;
	
	// Max permissible employee reporting level
	private int PERMISSIBLE_EMP_LEVEL = 6;
	
	// Minimum Salary level percent of the Average Subordinate salary
	private final float MIN_SAL_PERCENT = 0.2f; 	// 20%

	// Maximum Salary level percent of the Average Subordinate salary
	private final float MAX_SAL_PERCENT = 0.5f;		// 50%

	/**
	 *  process
	 *  	- Read input csv files and print statistics
	 */
	@Override
	public boolean process(String filePath) throws ScProcessException {
		try {
			
			// read inout file into list of beans
			List<InputData> rows = csvBeanFileReader.readFile(filePath);
			
			// ceo
			EmpNode ceo = null;
			
			// iterate rows and create employee nodes in organization DS
			for (InputData row : rows) {
				
				// employee node
				EmpNode empNode = new EmpNode();
				
				// set id
				try {
					empNode.setId(Long.parseLong(row.getId()));
				} catch (NumberFormatException e) {
					log.error(String.format("NumberFormatException thrown while setting employee id. %s. Cause: %s.", e.getMessage(), e.getCause()));
					throw new ScProcessException(e);
				}
				
				// set first name
				empNode.setFirstName(row.getFirstName());
				
				// set last name
				empNode.setLastName(row.getLastName());
				
				// set salary
				try {
					empNode.setSalary(Double.parseDouble(row.getSalary()));
				} catch (NumberFormatException e) {
					log.error(String.format("NumberFormatException thrown while setting employee salary. %s. Cause: %s.", e.getMessage(), e.getCause()));
					throw new ScProcessException(e);
				}
				
				// set manager id
				if (!row.getMgrId().isEmpty()) {
					try {
						empNode.setMgrId(Long.parseLong(row.getMgrId()));
					} catch (NumberFormatException e) {
						log.error(String.format("NumberFormatException thrown while setting employee's manager id. %s. Cause: %s.", e.getMessage(), e.getCause()));
						throw new ScProcessException(e);
					}
				}
				
				// put in DS
				orgNode.put(empNode.getId(), empNode);
				
				// ceo node
				if (row.getMgrId().isEmpty()) {
					
					ceo = empNode;
					
					// set reporting level
					ceo.setLevel(1);
				}
			}

			// set subordinates
			setSubordinates();

			// set levels
			setLevels();

			// find average salaries
			calculateAverageSalariesAtEachLevel();

			// print average salaries
			//printAverageSalariesAtEachLevel();

			// print output
			printOutput();

		} catch (ScFileReaderException e) {
			log.error(String.format("ScFileReaderException thrown. %s. Cause: %s.", e.getMessage(), e.getCause()));
			throw new ScProcessException(e);
		}
		return false;
	}

	/**
	 * 
	 */
	private void printOutput() {

		System.out.println("\n---------------------START------------------------------------------------");

		// iterate employee nodes
		for (Long key : orgNode.keySet()) {
			
			EmpNode empNode = orgNode.get(key);
			printEmpNode(empNode);
		}

		System.out.println("\n----------------------END-------------------------------------------------");

	}
	
	private void printEmpNode(EmpNode empNode) {

		// print employee data
		System.out.println("Id: " + empNode.getId());
		System.out.println("First Name: " + empNode.getFirstName());
		System.out.println("Last Name: " + empNode.getLastName());
		System.out.println("Salary: " + empNode.getSalary());
		System.out.println("Level: " + empNode.getLevel());
		
		// print manager info
		EmpNode mgr = orgNode.get(empNode.getMgrId());
		if (mgr!=null) {
			System.out.println(String.format("Manager: (%d) %s, %s", empNode.getMgrId(), mgr.getLastName(), mgr.getFirstName()));
		}
		
		// print subordinates info
		System.out.print("Subordinates: ");
		for (Long subId: empNode.getSubIds()) {
			EmpNode sub = orgNode.get(subId);
			if (sub!=null) {
				System.out.print(String.format("(%d) %s, %s | ", subId, sub.getLastName(), sub.getFirstName()));
			}
		}
		
		System.out.println("\n---------------------------------------------------------------------");
		
		// print salary correction
		printSalaryCorrection(empNode);
		
		System.out.println("---------------------------------------------------------------------");
		
		// print reporting level check
		printReportingLevelCheck(empNode);	
	}

	/**
	 * Print Reporting level Check
	 * @param empNode
	 */
	private void printReportingLevelCheck(EmpNode empNode) {
		if (empNode.getLevel() > PERMISSIBLE_EMP_LEVEL) {
			System.out.println(String.format("Employee's level - %d, exceeds Max permissable level - %d.", empNode.getLevel(), PERMISSIBLE_EMP_LEVEL));
			System.out.println("---------------------------------------------------------------------");
		}
	}

	/**
	 * Print Salary Correction
	 * 
	 * @param empNode
	 */
	private void printSalaryCorrection(EmpNode empNode) {
		Double avgSubSal = findAvgSalary(empNode.getLevel()+1);
		System.out.println(String.format("Average Salary at level %d is : %.2f", empNode.getLevel()+1, avgSubSal));
		if (avgSubSal > 0) {
			Double minSalExp = avgSubSal + avgSubSal*MIN_SAL_PERCENT;
			Double maxSalExp = avgSubSal + avgSubSal*MAX_SAL_PERCENT;
			System.out.println(String.format("Min Salary expected: %.2f. Max Salary expected: %.2f", minSalExp, maxSalExp));
			if (empNode.getSalary() < minSalExp) {
				System.out.println(String.format("Salary less by %.2f", (minSalExp - empNode.getSalary())));
			} else if (empNode.getSalary() > maxSalExp) {
				System.out.println(String.format("Salary greater by %.2f", (empNode.getSalary() - maxSalExp)));
			} else {
				System.out.println("Salary is within the expected range.");
			}
		}
	}

	/**
	 *  Print Average Salaries at each level
	 */
	private void printAverageSalariesAtEachLevel() {
		
		int lCtr = 1;
		
		// print average salaries
		for (Double sal : avgSalaries) {
			log.info(String.format("Level: %d Average Salary: %.2f ", lCtr, sal));
			lCtr++;
		}
	}

	/**
	 *  Calculate Average Salaries at each level
	 */
	private void calculateAverageSalariesAtEachLevel() {
		
		// iterate until max level
		for (int level=0; level <= maxLevel; level++) {
			
			// find average salary at a level
			Double avgSal = findAvgSalary(level);
			
			// add to list
			avgSalaries.add(level, avgSal);
		}
	}

	/**
	 *  Print Employee details of complete Organization
	 */
	private void printOrg() {
		
		// iterate and print
		for (Long key : orgNode.keySet()) {
			EmpNode empNode = orgNode.get(key);
			log.info("Key: " + key + " Emp Node: " + empNode.toString());
		}
	}

	/**
	 * Set Levels
	 *   - Set Level of each employee node with CEO at level=1. Employee directly reporting to a Manager would be at next level to the Manager.
	 *   - Logic: Iterate and first set level=2 and them level=3, etc., until all levels are set. Max iterations would be max levels. 
	 *            Time complexity would be O(n-1) 
	 */
	private void setLevels() throws ScProcessException {

		// init
		boolean allLevelsSet = false;
		long MAX_ITERATION = orgNode.size();
		long iterCtr = MAX_ITERATION;
		
		// iterate until level is set for all employees or, max iterations
		while (!allLevelsSet && iterCtr > 0) {
			
			// init
			allLevelsSet = true;
			
			// iterate all nodes
			for (Long key : orgNode.keySet()) {
				
				EmpNode empNode = orgNode.get(key);
				
				// if level not set
				if (empNode.getLevel()==0) {
					
					allLevelsSet = false;
					
					// fetch manager level
					EmpNode mgrNode = orgNode.get(empNode.getMgrId());
					if (mgrNode==null) {
						throw new ScProcessException(String.format("Non existent manager id found. Manager Id: %d. Employee Id: %d", empNode.getMgrId(), empNode.getId()));
					}
					int mgrLevel = mgrNode.getLevel();
					
					// manager level has valid value
					if (mgrLevel!=0) {
						
						// manager level + 1
						int levelToBeSet = mgrLevel+1;
						
						// update max level
						maxLevel = (maxLevel < levelToBeSet) ? levelToBeSet : maxLevel;
						
						// set employee node level
						empNode.setLevel(levelToBeSet);
					}
				}
			}
			
			// decrement max iter counter
			iterCtr--;
		}
	}

	/**
	 * Set Subordinates
	 *   - Set Subordinates of each employee node with CEO at level=1. Employee directly reporting to a Manager would be at next level to the Manager.
	 *   - Logic: Iterate and first set level=2 and them level=3, etc., until all levels are set. Max iterations would be max levels. 
	 *            Time complexity would be O(n-1) 
	 */
	private void setSubordinates() {
		boolean allSubsSet = false;
		while (!allSubsSet) {
			allSubsSet = true;
			for (Long key: orgNode.keySet()) {
				
				EmpNode empNode = orgNode.get(key);
				
				if (empNode.getMgrId()!=0) {
					long mgrId = empNode.getMgrId();
					EmpNode mgr = orgNode.get(mgrId);
					if (mgr!=null) {
						mgr.addSubId(empNode.getId());
					}
				}
			}
		}
	}

	/**
	 *  Find Average Salary at a given level
	 *  
	 * @param level
	 * @return
	 */
	private Double findAvgSalary(int level) {
		// list of salaries at a level
		List<Double> salariesAtLevel = new ArrayList<>();
		
		// iterate employee list
		for (Long key : orgNode.keySet()) {
			
			// employee node
			EmpNode empNode = orgNode.get(key);
			
			// if level found, add to salaries list
			if (empNode.getLevel()==level) {
				salariesAtLevel.add(empNode.getSalary());
			}
		}
		
		// if no employees found, print warning
		if (salariesAtLevel.size()==0) {
			log.debug(String.format("No Employees found at level : %d", level));
		}
		
		// calculate average
		OptionalDouble avgOpt = salariesAtLevel.stream().mapToDouble(a -> a).average();
		
		// return value
		return avgOpt.isPresent() ? avgOpt.getAsDouble() : 0;
	}
}

package com.codeex.salarycheck.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 	Employee Node
 * 
 * 		- From Input File, each row is read into this node
 * 			Additionally, we also maintain a) Subordinate Ids b) Reporting level
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EmpNode {

	// Id
	private long id;
	
	// First Name
	private String firstName;
	
	// Last Name
	private String lastName;
	
	// Salary
	private double salary;
	
	// Manager Id
	private long mgrId=0;
	
	// Subordinate Ids
	private List<Long> subIds = new ArrayList<>();
	
	// Employee Reporting Level
	private int level=0;
	
	// Add Subordinate Id
	public void addSubId(long empId) {
		this.subIds.add(empId);
	}

	@Override
	public String toString() {
		return "EmpNode [ mgrId=" + mgrId + ", id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", salary=" + salary + ", subIds=" + subIds + ", level=" + level + "]";
	}

}

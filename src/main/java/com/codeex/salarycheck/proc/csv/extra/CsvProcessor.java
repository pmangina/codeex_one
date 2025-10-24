package com.codeex.salarycheck.proc.csv.extra;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeex.salarycheck.input.file.csv.extra.CSVFileReader;
import com.codeex.salarycheck.input.file.exception.ScFileReaderException;
import com.codeex.salarycheck.model.EmpNode;
import com.codeex.salarycheck.proc.Processor;
import com.codeex.salarycheck.proc.exception.ScProcessException;

@Service
public class CsvProcessor implements Processor {

	@Autowired
	private CSVFileReader csvFileReader;
	
	private HashMap<Integer, EmpNode> org = new HashMap<>();
	
	@Override
	public boolean process(String filePath) throws ScProcessException {
		try {
			csvFileReader.openFile(filePath);
			String[] rowTokens = csvFileReader.readNext();
			while (rowTokens != null) {
				
				rowTokens = csvFileReader.readNext();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ScFileReaderException e) {
			e.printStackTrace();
		}
		return false;
	}

}

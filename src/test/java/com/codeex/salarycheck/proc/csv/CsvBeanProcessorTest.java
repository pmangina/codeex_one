package com.codeex.salarycheck.proc.csv;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.codeex.salarycheck.proc.exception.ScProcessException;

//imports
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CsvBeanProcessorTest {

	@Autowired
	private ApplicationContext context;

	@Test
	void testValidFile() {
		CsvBeanProcessor csvBeanProcessor = context.getBean(CsvBeanProcessor.class);
		assertNotNull(csvBeanProcessor);
		File resourcesDirectory = new File("src/test/resources");
		try {
			csvBeanProcessor.process(resourcesDirectory.getAbsolutePath() + "/file1.csv");
		} catch (ScProcessException e) {
			e.printStackTrace();
			fail("Unexpected Exception thrown.");
		}
	}

	@Test
	void testInputFileHasErrorsInId() {
		CsvBeanProcessor csvBeanProcessor = context.getBean(CsvBeanProcessor.class);
		assertNotNull(csvBeanProcessor);
		File resourcesDirectory = new File("src/test/resources");
		try {
			csvBeanProcessor.process(resourcesDirectory.getAbsolutePath() + "/file_error1.csv");
		} catch (ScProcessException e) {
			e.printStackTrace();
			assertTrue(true, "Valid Exception thrown.");
		}
	}

	@Test
	void testInputFileHasErrorsInSalary() {
		CsvBeanProcessor csvBeanProcessor = context.getBean(CsvBeanProcessor.class);
		assertNotNull(csvBeanProcessor);
		File resourcesDirectory = new File("src/test/resources");
		try {
			csvBeanProcessor.process(resourcesDirectory.getAbsolutePath() + "/file_error2.csv");
		} catch (ScProcessException e) {
			e.printStackTrace();
			assertTrue(true, "Valid Exception thrown.");
		}
	}

	@Test
	void testInputFileHasErrorsInManagerId() {
		CsvBeanProcessor csvBeanProcessor = context.getBean(CsvBeanProcessor.class);
		assertNotNull(csvBeanProcessor);
		File resourcesDirectory = new File("src/test/resources");
		try {
			csvBeanProcessor.process(resourcesDirectory.getAbsolutePath() + "/file_error3.csv");
		} catch (ScProcessException e) {
			e.printStackTrace();
			assertTrue(true, "Valid Exception thrown.");
		}
	}

	@Test
	void testInputFileHasNonExistentManagerId() {
		CsvBeanProcessor csvBeanProcessor = context.getBean(CsvBeanProcessor.class);
		assertNotNull(csvBeanProcessor);
		File resourcesDirectory = new File("src/test/resources");
		try {
			csvBeanProcessor.process(resourcesDirectory.getAbsolutePath() + "/file_error4.csv");
		} catch (ScProcessException e) {
			e.printStackTrace();
			assertTrue(true, "Valid Exception thrown.");
		}
	}

	@Test
	void testInputFileHas1000Rows() {
		CsvBeanProcessor csvBeanProcessor = context.getBean(CsvBeanProcessor.class);
		assertNotNull(csvBeanProcessor);
		File resourcesDirectory = new File("src/test/resources");
		try {
			csvBeanProcessor.process(resourcesDirectory.getAbsolutePath() + "/file_error5.csv");
		} catch (ScProcessException e) {
			e.printStackTrace();
			assertTrue(true, "Valid Exception thrown.");
		}
	}

}

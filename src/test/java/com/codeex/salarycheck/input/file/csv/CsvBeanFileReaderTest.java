package com.codeex.salarycheck.input.file.csv;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codeex.salarycheck.input.file.csv.model.InputData;
import com.codeex.salarycheck.input.file.exception.ScFileReaderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class CsvBeanFileReaderTest {

	private CsvBeanFileReader csvBeanFileReader;
	
	@BeforeEach
	public void setUp() throws Exception {
		csvBeanFileReader = new CsvBeanFileReader();
	}

	@Test
	void testReadFileValid() {
		try {
			File resourcesDirectory = new File("src/test/resources");
			List<InputData> inpData = csvBeanFileReader.readFile(resourcesDirectory+"/file1.csv");
			assertTrue(inpData.size()==5);
			assertTrue("123".equals(inpData.get(0).getId()));
		} catch (ScFileReaderException ex) {
			log.info(ex.getMessage());
		}
	}

	@Test
	void testReadFileEmpty() {
		try {
			csvBeanFileReader.readFile("");
			fail("Unexpected line run. Exception should be thrown when input filepath is empty.");
		} catch (ScFileReaderException ex) {
			log.info(ex.getMessage());
		}
	}

	@Test
	void testReadFileNull() {
		try {
			csvBeanFileReader.readFile(null);
			fail("Unexpected line run. Exception should be thrown when input filepath is null.");
		} catch (ScFileReaderException ex) {
			log.info(ex.getMessage());
		}
	}

	@Test
	void testReadFileIncorrectFilePath() {
		try {
			csvBeanFileReader.readFile("jjhgsjdfgsdjfg");
			fail("Unexpected line run. Exception should be thrown when input filepath is incorrect.");
		} catch (ScFileReaderException ex) {
			log.info(ex.getMessage());
		}
	}

}

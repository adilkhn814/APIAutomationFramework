package com.api.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

	private static final Logger logger = LogManager.getLogger(TestListener.class);

	public void onTestStart(ITestResult result) {
		// not implemented
		logger.info("Started!!!" + result.getMethod().getMethodName());
		logger.info("Description!!!" + result.getMethod().getMethodName());
		
	}
	
	public void onStart(ITestContext context) {
	    // not implemented
		logger.info("Test Suite Started!!!");
	  }

	public void onTestSuccess(ITestResult result) {
		// not implemented
		logger.info("Passed!!!" + result.getMethod().getMethodName());
		logger.info("Description!!!" + result.getMethod().getMethodName());

	}

	public void onTestFailure(ITestResult result) {
		// not implemented
		logger.error("Failed!!!" + result.getMethod().getMethodName());
	}

	public void onTestSkipped(ITestResult result) {
		// not implemented
		logger.info("Skipped!!!" + result.getMethod().getMethodName());
	}

	public void onFinish(ITestContext context) {
		// not implemented
		logger.info("Test Suite Completed!!!");
	}

}

package com.parabank.utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.createTest(result.getName(), result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().pass("Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().fail("Test failed: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReport();
    }
}

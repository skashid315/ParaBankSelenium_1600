package com.parabank.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    public static synchronized ExtentReports createInstance() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportName = "ParaBank_Automation_Report_" + timestamp + ".html";
        String reportPath = System.getProperty("user.dir") + "/test-output/reports/" + reportName;

        File reportDir = new File(System.getProperty("user.dir") + "/test-output/reports");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("ParaBank Automation Report");
        sparkReporter.config().setReportName("Login Module Test Execution");
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("URL", "https://parabank.parasoft.com/");

        logger.info("Extent Report initialized at: " + reportPath);
        return extent;
    }

    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest extentTest = getInstance().createTest(testName, description);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void logStep(String testStep, String testDescription, String expectedResult, String actualResult, String status) {
        String tableHtml = "<table border='1' style='border-collapse: collapse; width: 100%;'>" +
                "<tr style='background-color: #f2f2f2;'>" +
                "<th style='padding: 8px; text-align: left;'>Test Step</th>" +
                "<th style='padding: 8px; text-align: left;'>Test Description</th>" +
                "<th style='padding: 8px; text-align: left;'>Expected Result</th>" +
                "<th style='padding: 8px; text-align: left;'>Actual Result</th>" +
                "<th style='padding: 8px; text-align: left;'>Status</th></tr>" +
                "<tr>" +
                "<td style='padding: 8px;'>" + testStep + "</td>" +
                "<td style='padding: 8px;'>" + testDescription + "</td>" +
                "<td style='padding: 8px;'>" + expectedResult + "</td>" +
                "<td style='padding: 8px;'>" + actualResult + "</td>" +
                "<td style='padding: 8px;'>" + status + "</td></tr></table>";

        if (status.equalsIgnoreCase("Pass")) {
            getTest().pass(tableHtml);
        } else if (status.equalsIgnoreCase("Fail")) {
            getTest().fail(tableHtml);
        } else {
            getTest().info(tableHtml);
        }
    }

    public static void attachScreenshot(String screenshotPath) {
        try {
            getTest().addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            logger.error("Failed to attach screenshot: " + e.getMessage());
        }
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            logger.info("Extent Report flushed successfully");
        }
    }
}
